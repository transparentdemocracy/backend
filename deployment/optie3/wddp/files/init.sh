#!/bin/bash

set -e

setup_docker() {
  sudo usermod -aG docker ec2-user
  sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
  sudo chmod +x /usr/local/bin/docker-compose
}

setup_docker