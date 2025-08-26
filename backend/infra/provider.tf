provider "aws" {
  region = var.AWS_REGION
}

terraform {
  required_version = ">= 0.12"
  backend "s3" {
    bucket         = "terraform-tfstate-bucket-234242"
    key            = "speech-analyzer"
    region         = "us-east-1"
    encrypt        = true
  }
}
