#!/bin/bash

# init
export AWS_PROFILE=transparentdemocracy

# Make sure this matches variables.auto.tfvars
export AWS_REGION=eu-west-1

cd ../../..

# build & run tests
./mvnw install

# build docker image
docker buildx build --platform linux/amd64 -t transparent-democracy/voting-backend .

# push docker image to ECR
deployment/pushImageToECR.sh

# Go to deployment environment folder
cd deployment/optie3/karel-dev
tfenv use

# plan
terraform plan

# apply
terraform apply

# set up docker, docker-compose
./fabw init

# start backend
./fabw start

# upload documents
cd ../../..
export PUBLIC_IP=$(tf output -json|jq '.public_ip.value' -r)
export WDDP_BACKEND_URL="http://${PUBLIC_IP}:8080"
scripts/upload-all.sh