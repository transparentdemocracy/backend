import os
from fabric import task


@task
def init(c):
    c.run("aws s3 sync s3://wddp-deploy-scripts-dev/ /home/ec2-user/")
    c.run("chmod +x ./init.sh")
    c.run("sudo ./init.sh")

@task
def start(c):
    # TODO: get rid of harcoded account id here, maybe pass it via a template init script
    c.run("aws ecr get-login-password --region eu-west-1 | docker login --username AWS --password-stdin 820650340922.dkr.ecr.eu-west-1.amazonaws.com")
    c.run("docker-compose up -d", pty=True)


@task
def remote_echo(c):
    result = c.run('echo "Hello, from the remote server"', hide=True)
    print("Output of 'echo' command on the remote server:")
    print(result.stdout)

