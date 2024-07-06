terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.51.1"
    }
  }

  required_version = ">= 1.5.0"
}

data "aws_caller_identity" "current" {}

data "http" "myip" {
  url = "https://ipv4.icanhazip.com"
}

locals {
  account_id = data.aws_caller_identity.current.account_id
  myip = chomp(data.http.myip.response_body)
  docker_compose_rendered = templatefile("${path.module}/docker-compose.yml", {
    wddp_image = var.wddp_image
  })
}

resource "aws_ecr_repository" "wddp_ecr_repo" {
  name = "wddp-ecr-repo"
}
resource "aws_ecs_cluster" "wddp_cluster" {
  name = "wddp-cluster"
}


resource "aws_vpc" "main" {
  cidr_block = "10.0.0.0/16"
}
resource "aws_subnet" "public" {
  vpc_id                  = aws_vpc.main.id
  cidr_block              = "10.0.1.0/24"
  map_public_ip_on_launch = true
}

resource "aws_subnet" "private" {
  cidr_block = "10.0.2.0/24"
  vpc_id     = aws_vpc.main.id
  availability_zone = var.availability_zone
}

resource "aws_internet_gateway" "gw" {
  vpc_id = aws_vpc.main.id
}
resource "aws_route_table" "public" {
  vpc_id = aws_vpc.main.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.gw.id
  }
}
resource "aws_route_table_association" "public" {
  subnet_id      = aws_subnet.public.id
  route_table_id = aws_route_table.public.id
}

resource "aws_security_group" "security_group" {
  name   = "instance-security-group"
  vpc_id = aws_vpc.main.id

  ingress {
    from_port = 8080
    to_port   = 8080
    protocol  = "tcp"
    # TODO: 0.0.0.0/0 is for testing only, should be limited to load balancer
#     cidr_blocks = [aws_subnet.private.cidr_block]
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port = 22
    to_port   = 22
    protocol  = "tcp"
    cidr_blocks = ["${local.myip}/32"]
  }

  egress {
    from_port = 0
    to_port   = 0
    protocol  = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_iam_role" "wddp" {
  name = "wddp"

  assume_role_policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Principal": {
        "Service": "ec2.amazonaws.com"
      },
      "Action": "sts:AssumeRole"
    }
  ]
}
EOF
}

resource "aws_iam_role_policy" "ecr_pull_policy" {
  name = "ecr-pull-policy"
  role = aws_iam_role.wddp.name

  policy = <<EOF
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": [
                "ecr:GetAuthorizationToken",
                "ecr:BatchCheckLayerAvailability",
                "ecr:GetDownloadUrlForLayer",
                "ecr:GetRepositoryPolicy",
                "ecr:DescribeRepositories",
                "ecr:ListImages",
                "ecr:DescribeImages",
                "ecr:BatchGetImage"
            ],
            "Resource": "arn:aws:ecr:${var.region}:${local.account_id}:repository/wddp-ecr-repo"
        },
        {
          "Effect": "Allow",
          "Action": [
            "ecr:GetAuthorizationToken"
          ],
          "Resource": "*"
        }
    ]
}
EOF
}

resource "aws_iam_role_policy" "s3_read_policy" {
  name = "s3-read-policy"
  role = aws_iam_role.wddp.name

  policy = <<EOF
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
              "Action": [
                "s3:GetObject",
                "s3:ListBucket"
            ],
            "Resource": [
                "${aws_s3_bucket.deploy_scripts_bucket.arn}",
                "${aws_s3_bucket.deploy_scripts_bucket.arn}/*"
            ]
        }
    ]
}
EOF
}

resource "aws_iam_instance_profile" "wddp" {
  name = "wddp_profile"
  role = aws_iam_role.wddp.name
}

# Create an EC2 instance
resource "aws_instance" "wddp" {
  ami = var.ami
  instance_type        = "t2.micro"
  subnet_id            = aws_subnet.public.id
  vpc_security_group_ids = [aws_security_group.security_group.id]
  iam_instance_profile = aws_iam_instance_profile.wddp.name
  key_name             = aws_key_pair.my_key_pair.key_name

  user_data = <<EOF
#!/bin/bash
yum install -y docker
systemctl start docker
systemctl enable docker
EOF
}

resource "tls_private_key" "my_private_key" {
  algorithm = "RSA"
  rsa_bits  = 4096
}

resource "aws_key_pair" "my_key_pair" {
  key_name = "myKey"       # Create "myKey" to AWS!!
  public_key = tls_private_key.my_private_key.public_key_openssh
}

resource "local_file" "key_file" {
  content         = tls_private_key.my_private_key.private_key_pem
  filename        = "myKey.pem"
  file_permission = "0600"
}

resource "aws_s3_bucket" "deploy_scripts_bucket" {
  bucket = "wddp-deploy-scripts-dev"
}

resource "aws_s3_object" "deploy_script_file" {
  for_each = fileset("${path.module}/files", "**/*")

  bucket = aws_s3_bucket.deploy_scripts_bucket.bucket
  key    = each.value
  source = "${path.module}/files/${each.value}"
  etag = filemd5("${path.module}/files/${each.value}")
}


resource "aws_s3_object" "docker_compose" {
  bucket  = aws_s3_bucket.deploy_scripts_bucket.bucket
  key     = "docker-compose.yml"
  content = local.docker_compose_rendered
  etag = md5(local.docker_compose_rendered)
}
