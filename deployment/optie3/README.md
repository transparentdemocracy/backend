# EC2 + docker-compose deployment

## Preparation

When setting up a new environment, just copy one of the existing ones, and update the variables.auto.tfvars file.
Install dependencies (OSX example):

    brew install tfenv

Configure an AWS profile (replace YOUR_PROFILE with something you choose)

    aws configure --profile YOUR_PROFILE

Export variables

    export AWS_PROFILE=YOUR_PROFILE
    export AWS_REGION=eu-west-1

## Deployment example

In this file we'll be using `karel-dev` as example environment.

Create infra

    cd karel-dev
    tfenv use
    tf apply

Download dependencies

    fab init

Start backend

    fab start

TODO: instructions for uploading
