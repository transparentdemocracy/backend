# Create a load balancer
resource "aws_elb" "wddp" {
  name = "wddp-elb"
  subnets = [aws_subnet.public.id]
  # TODO: register security group via lb instead of directly?

  listener {
    instance_port     = 8080
    instance_protocol = "http"
    lb_port           = 80
    lb_protocol       = "http"
  }

  health_check {
    healthy_threshold   = 2
    unhealthy_threshold = 2
    timeout             = 3
    target              = "HTTP:8080/ping."
    interval            = 30
  }
}

resource "aws_elb_attachment" "wddp" {
  elb      = aws_elb.wddp.id
  instance = aws_instance.wddp.id
}