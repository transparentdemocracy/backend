# Login

AWS_ACCOUNT_ID="$(aws sts get-caller-identity|jq .Account -r)"
AWS_REGION="${AWS_REGION-:eu-west-1}"

aws ecr get-login-password --region "${AWS_REGION}" | docker login --username AWS --password-stdin "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com"

# Tag it with the repository name : trans-demo-repo
docker tag transparent-democracy/voting-backend:latest "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/wddp-ecr-repo:latest"

# Push using the tag containing the repo name
docker push "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/wddp-ecr-repo:latest"
