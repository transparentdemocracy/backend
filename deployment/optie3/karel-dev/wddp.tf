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
  region = var.region
#   DO NOT PUSH THESE KEYS TO GIT!!!!!!!
#   access_key = ""
#   secret_key = ""

  alias = "aws"
}

module "wddp" {
  source     = "../wddp"

  region = var.region
  availability_zone = var.availability_zone
  wddp_image = "${var.aws_account_id}.dkr.ecr.eu-west-1.amazonaws.com/wddp-ecr-repo:latest"
  zone_name = var.zone_name
  domain_name = var.domain_name
}

output public_ip {
  value = module.wddp.public_ip
  description = "public ip of the ec2 instance"
}