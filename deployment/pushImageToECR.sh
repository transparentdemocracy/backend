# Login
aws ecr get-login-password --region eu-west-3 | docker login --username AWS --password-stdin 767397876757.dkr.ecr.eu-west-3.amazonaws.com

# Tag it with the repository name : trans-demo-repo
docker tag transparent-democracy/voting-backend:latest 767397876757.dkr.ecr.eu-west-3.amazonaws.com/wddp-ecr-repo:latest

# Push using the tag containing the repo name
docker push 767397876757.dkr.ecr.eu-west-3.amazonaws.com/wddp-ecr-repo:latest
