terraform {
  required_version = ">= 1.5.0"
  required_providers {
    google = {
      source  = "hashicorp/google"
      version = "~> 5.0"
    }
  }
}

provider "google" {
  project = var.gcp_project_id
  region  = var.gcp_region
}

variable "gcp_project_id" {
  type        = string
  description = "ID del proyecto en Google Cloud Platform"
  default     = "enterprise-xavier-cloud"
}

variable "gcp_region" {
  type        = string
  description = "Región de despliegue en GCP"
  default     = "us-central1"
}

# Despliegue Serverless en Google Cloud Run v2
resource "google_cloud_run_v2_service" "enterprise_backend" {
  name     = "enterprise-backend-api"
  location = var.gcp_region
  ingress  = "INGRESS_TRAFFIC_ALL"

  template {
    scaling {
      min_instance_count = 0  # 0 instancias cuando no hay tráfico (cero costo)
      max_instance_count = 5  # Escala automáticamente hasta 5 contenedores bajo demanda
    }

    containers {
      image = "gcr.io/${var.gcp_project_id}/enterprise-backend:latest"

      ports {
        container_port = 8080
      }

      resources {
        limits = {
          cpu    = "1000m"
          memory = "512Mi"
        }
      }

      env {
        name  = "SPRING_PROFILES_ACTIVE"
        value = "prod"
      }

      # Health check nativo de Cloud Run apuntando a Spring Boot Actuator
      startup_probe {
        http_get {
          path = "/actuator/health/liveness"
          port = 8080
        }
        initial_delay_seconds = 10
        period_seconds        = 5
        failure_threshold     = 3
      }
    }
  }
}

# Permite acceso público seguro a la API
resource "google_cloud_run_v2_service_iam_member" "public_access" {
  name     = google_cloud_run_v2_service.enterprise_backend.name
  location = google_cloud_run_v2_service.enterprise_backend.location
  role     = "roles/run.invoker"
  member   = "allUsers"
}

output "api_service_url" {
  description = "URL pública generada automáticamente por Google Cloud Run"
  value       = google_cloud_run_v2_service.enterprise_backend.uri
}

