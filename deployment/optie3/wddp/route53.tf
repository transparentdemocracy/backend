
# Create a Route 53 zone
resource "aws_route53_zone" "wddp" {
  name = var.zone_name
}

# Create a CNAME record to point to the load balancer
resource "aws_route53_record" "wddp" {
  zone_id = aws_route53_zone.wddp.zone_id
  name    = var.domain_name
  type    = "CNAME"
  ttl     = 300
  records = [aws_elb.wddp.dns_name]
}