# Create an ALB
resource "aws_lb" "speech_analyzer" {
  name               = "speech-analyzer-alb"
  internal           = false
  load_balancer_type = "application"
  security_groups    = [aws_security_group.alb_sg.id]
  subnets           = data.aws_subnets.subnets.ids
}

# Create Target Group for frontend
resource "aws_lb_target_group" "backend_tg" {
  name        = "backend-tg"
  port        = 8080
  protocol    = "HTTPS"
  vpc_id      = data.aws_vpc.vpc.id
  target_type = "ip"
}

# Create ALB Listener for frontend
resource "aws_lb_listener" "backend_listener" {
  load_balancer_arn = aws_lb.speech_analyzer.arn
  port              = 8080
  protocol          = "HTTPS"

  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.backend_tg.arn
  }
}

# Security Group for ALB
resource "aws_security_group" "alb_sg" {
  name        = "alb-security-group"
  vpc_id      = data.aws_vpc.vpc.id

  ingress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]  # Allow all traffic
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}