#!/bin/bash

# init
export AWS_PROFILE=transparentdemocracy
export AWS_REGION=eu-west-1

cd ../..

# run tests
./mvnw install

# build docker image
docker buildx build --platform linux/amd64 -t transparent-democracy/voting-backend .

# push docker image to ECR
deployment/pushImageToECR.sh

# plan
terraform plan

# apply
terraform apply

# set up docker, docker-compose
./wddp init

# start backend
./wddp start

# TODO: upload documents
