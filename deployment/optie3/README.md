# EC2 + docker-compose deployment

## Preparation

When setting up a new environment, just copy one of the existing ones, and update the variables.auto.tfvars file.
Install dependencies (OSX example):

    brew install tfenv

Configure an AWS profile (replace YOUR_PROFILE with something you choose)

    aws configure --profile YOUR_PROFILE

Set your AWS_PROFILE environment variable (replace YOUR_PROFILE with something you choose)

    export AWS_PROFILE=YOUR_PROFILE

## Deployment example

In this file we'll be using `karel-dev` as example environment.

Create infra

    cd karel-dev
    tfenv use
    tf apply

TODO: instructions for uploading docker-compose.yml or do that via TF
TODO: instructions for setting up docker & docker-compose on the ec2 instance (init.sh)
TODO: instructions for `docker-compose up -d`

TODO: automate the all ssh stuff, use fabric or capistrano? (Or is that too old school?)

    

