
variable "region" {
  type = string
  default = "eu-west-1"
}

variable "availability_zone" {
  type = string
  default = "eu-west-1c"
}

variable "ami" {
  type = string
  # TODO: automatically use latest version?
  default = "ami-08ba52a61087f1bd6" #Amazon linux eu-west 1
}

variable "wddp_image" {
  type = string
}

variable "zone_name" {
  type = string
  # zone name, example 'watdoetdepolitiek.be'
}

variable "domain_name" {
  type = string
  # domain name, example 'www.watdoetdepolitiek.be'
}