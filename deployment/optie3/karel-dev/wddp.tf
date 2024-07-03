terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.51.1"
    }
  }

  required_version = ">= 1.5.0"
}

provider "aws" {
  region = "eu-west-1"
  # DO NOT PUSH THESE KEYS TO GIT!!!!!!!
  access_key = ""
  secret_key = ""
}

module "wddp" {
  source     = "../wddp"
  wddp_image = "${var.aws_account_id}.dkr.ecr.eu-west-1.amazonaws.com/wddp-ecr-repo:latest"
}

output public_ip {
  value = module.wddp.public_ip
  description = "public ip of the ec2 instance"
}