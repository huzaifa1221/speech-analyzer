data "aws_vpc" "vpc" {
  id = var.vpc_id
}

# Get all subnets in the VPC
data "aws_subnets" "subnets" {
  filter {
    name   = "vpc-id"
    values = [data.aws_vpc.vpc.id]
  }
}

# Create an ECS fargate cluster
resource "aws_ecs_cluster" "fargate_cluster" {
  name = "speech-analyzer-cluster"
}

# Define an ECS Task Execution Role
resource "aws_iam_role" "ecs_task_execution_role" {
  name               = "AWSServiceRoleEcs"
  assume_role_policy = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Effect = "Allow",
        Principal = {
          "Service": "ecs-tasks.amazonaws.com"
        },
        Action = "sts:AssumeRole"
      }
    ]
  })
}

# Attach the ECS Task Execution Policy to the Role
resource "aws_iam_role_policy_attachment" "ecs_task_execution_role_policy" {
  role       = aws_iam_role.ecs_task_execution_role.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy"
}
resource "aws_iam_role_policy_attachment" "ecs_transcribe_policy" {
  role       = aws_iam_role.ecs_task_execution_role.name
  policy_arn = aws_iam_policy.transcribe_streaming_policy.arn
}

resource "aws_iam_policy" "transcribe_streaming_policy" {
  name        = "streaming_transcription_policy"
  description = "To allow speech-analyzer backend to perform streaming transcriptions"
  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = [
          "transcribe:StartStreamTranscription",
        ]
        Effect   = "Allow"
        Resource = "*"
      },
    ]
  })
}

# Define ECR repository for storing Docker images
resource "aws_ecr_repository" "speech-analyzer" {
  name                 = "speech-analyzer"
  image_tag_mutability = "MUTABLE"

  image_scanning_configuration {
    scan_on_push = true
  }
}