# https://earthly.dev/blog/deploy-dockcontainers-to-awsecs-using-terraform/
terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.51.1"
    }
  }

  required_version = ">= 1.2.0"
}

provider "aws" {
  region = "eu-west-1"
}

/**
 * The Container and cluster setup
 */

## The ECR repo where we will store our docker image
resource "aws_ecr_repository" "trans-demo_ecr_repo" {
  name = "trans-demo-repo"
}

## The ECS cluster that will run the Docker container
resource "aws_ecs_cluster" "trans-demo_cluster" {
  name = "trans-demo-cluster"
}

## The ECS Task definition on how to run our container
resource "aws_ecs_task_definition" "trans-demo_task-def" {
  family                = "trans-demo-task"
  container_definitions = <<DEFINITION
  [
    {
      "name": "trans-demo-task",
      "image": "${aws_ecr_repository.trans-demo_ecr_repo.repository_url}",
      "essential": true,
      "portMappings": [
        {
          "containerPort": 8080,
          "hostPort": 8080
        }
      ],
      "healthcheck": {
        "command": ["CMD-SHELL", "echo 'healthy' || exit 1"]
      },
      "logConfiguration" : {
        "logDriver" : "awslogs",
        "options" : {
          "awslogs-region"        : "eu-west-1",
          "awslogs-group"         : "trans-demo-log-group",
          "awslogs-stream-prefix" : "voting-backend"
      }
    },
      "memory": 1024,
      "cpu": 512
    }
  ]
  DEFINITION
  requires_compatibilities = ["FARGATE"] # use Fargate as the launch type
  network_mode = "awsvpc"    # add the AWS VPN network mode as this is required for Fargate
  memory = 1024         # Specify the memory the container requires
  cpu = 512         # Specify the CPU the container requires
  execution_role_arn    = "${aws_iam_role.ecsTaskExecutionRole.arn}"

}

/**
 * RIGHTS for ECS
 *
 * We  will create a task and assign the resources needed to power up the container through this task.
 * Creating a task definition requires ecsTaskExecutionRole to be added to our IAM.
 * We allow the account to assume this role
 */

## Declare a new temp IAM policy to be able to assign a Role in ecs-tasks
data "aws_iam_policy_document" "assume_role_policy" {
  statement {
    actions = ["sts:AssumeRole"]

    principals {
      type        = "Service"
      identifiers = ["ecs-tasks.amazonaws.com"]
    }
  }
}

## Declare a IAM Role for TaskExecution with the assume role policy
resource "aws_iam_role" "ecsTaskExecutionRole" {
  name               = "ecsTaskExecutionRole"
  assume_role_policy = "${data.aws_iam_policy_document.assume_role_policy.json}"
}

## Attach the required AmazonECSTaskExecutionRolePolicy to the declared role, which they now can because we have the Assume Role
resource "aws_iam_role_policy_attachment" "ecsTaskExecutionRole_policy" {
  role       = "${aws_iam_role.ecsTaskExecutionRole.name}"
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy"
}

/**
 * Network
 */

# Provide a reference to your default VPC
resource "aws_default_vpc" "default_vpc" {
}

# Provide references to our two default subnets
resource "aws_default_subnet" "default_subnet_a" {
  # Use your own region here but reference to subnet 1a
  availability_zone = "eu-west-1a"
}

resource "aws_default_subnet" "default_subnet_b" {
  # Use your own region here but reference to subnet 1b
  availability_zone = "eu-west-1b"
}

# Declare our load balancer, balancing load over the application, in two subnets
resource "aws_alb" "trans-demo_load_balancer" {
  name = "load-balancer-trans-demo" #load balancer name
  load_balancer_type = "application"
  subnets = [
    # Referencing the default subnets
    "${aws_default_subnet.default_subnet_a.id}",
    "${aws_default_subnet.default_subnet_b.id}"
  ]
  # security group
  security_groups = ["${aws_security_group.load_balancer_security_group.id}"]
}

# Create a security group for the load balancer. This controls the traffic that is allowed to reach and leave the load balancer.
resource "aws_security_group" "load_balancer_security_group" {
  ingress {
    from_port   = 80
    to_port     = 8080
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"] # Allow traffic in from all sources
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

# Declare a load balancer targetgroup, sending http requests on port 80 to our vpc
resource "aws_lb_target_group" "target_group" {
  name        = "target-group"
  port        = 80
  protocol    = "HTTP"
  target_type = "ip"
  vpc_id      = "${aws_default_vpc.default_vpc.id}" # default VPC
  health_check {
    path                                    = "/actuator/health"
    port                                    = 8080
    protocol                                = "http"
    healthy_threshold                       = 3
    unhealthy_threshold                     = 3
    matcher                                 = "200-399"

  }
}

# Declare a listener on the load balance that will forward port 80 to the target group
resource "aws_lb_listener" "listener" {
  load_balancer_arn = "${aws_alb.trans-demo_load_balancer.arn}" #  load balancer
  port     = "80"
  protocol = "HTTP"
  default_action {
    type             = "forward"
    target_group_arn = "${aws_lb_target_group.target_group.arn}" # target group
  }
}

resource "aws_ecs_service" "trans-demo_service" {
  name = "voting-backend-service"     # Name the service
  cluster = "${aws_ecs_cluster.trans-demo_cluster.id}"   # Reference the created Cluster
  task_definition = "${aws_ecs_task_definition.trans-demo_task-def.arn}" # Reference the task that the service will spin up
  launch_type   = "FARGATE"
  desired_count = 3 # Set up the number of containers to 3

  load_balancer {
    target_group_arn = "${aws_lb_target_group.target_group.arn}" # Reference the target group
    container_name = "${aws_ecs_task_definition.trans-demo_task-def.family}"
    container_port = 8080 # Specify the container port
  }

  network_configuration {
    subnets         = ["${aws_default_subnet.default_subnet_a.id}", "${aws_default_subnet.default_subnet_b.id}"]
    # TODO
    #
    # I want this to be false but then
    # ResourceInitializationError:
    # unable to pull secrets or registry auth: execution resource retrieval failed:
    # unable to retrieve ecr registry auth: service call has been retried 3 time(s):
    # RequestError: send request failed caused by: Post "https://api.ecr.eu-west-1.amazonaws.com/": dial tcp 63.34.62.189:443: i/o timeout.
    # Please check your task network configuration.
    # See
    # https://stackoverflow.com/questions/75336316/how-to-to-launch-ecs-fargate-container-without-public-ip
    #
    assign_public_ip = true     # Provide the containers with public IPs
    security_groups = ["${aws_security_group.load_balancer_security_group.id}"] # Set up the security group
  }
}

resource "aws_security_group" "service_security_group" {
  ingress {
    from_port       = 0
    to_port         = 0
    protocol = "-1"
    # Only allowing traffic in from the load balancer security group
    security_groups = ["${aws_security_group.load_balancer_security_group.id}"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}


## add logs to ecs
resource "aws_cloudwatch_log_group" "ecs_trans_demo" {
  name              = "trans-demo-log-group"
  retention_in_days = 7
}


#Log the load balancer app URL
output "app_url" {
  value = aws_alb.trans-demo_load_balancer.dns_name
}