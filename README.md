# TMDB Back-End Application

This project is a back-end application for TMDB, a system that integrates MongoDB, Redis, and Cloudinary for data management and storage. It uses Spring Boot for application development, Docker for containerization, and GitHub Actions for CI/CD.

---

## Table of Contents

1. [Prerequisites](#prerequisites)
2. [Environment Variables](#environment-variables)
3. [Build and Run](#build-and-run)
4. [Deployment](#deployment)
    - [Development Deployment](#development-deployment)
    - [Production Deployment](#production-deployment)
5. [Docker](#docker)
6. [CI/CD Workflow](#ci/cd-workflow)

---

## Prerequisites

Ensure you have the following installed:

- **Java 17**
- **Maven 3.8+**
- **Docker & Docker Compose**
- **Git**
- **A MongoDB instance**
- **Redis**

---

## Environment Variables

The application requires several environment variables for configuration. These are passed during runtime or configured in the CI/CD secrets.

| Variable               | Description                        |
|------------------------|------------------------------------|
| `MONGODB_URI`          | MongoDB connection URI            |
| `JWT_SECRET`           | Secret key for JWT authentication |
| `MAIL_ACCOUNT`         | Email account for SMTP            |
| `MAIL_PASSWORD`        | Password for the SMTP email       |
| `REDIS_HOST`           | Host for Redis instance           |
| `REDIS_PORT`           | Port for Redis instance           |
| `REDIS_PASSWORD`       | Redis instance password           |
| `CLIENT_ID`            | OAuth Client ID                   |
| `CLIENT_SECRET`        | OAuth Client Secret               |
| `REDIRECT_URI`         | OAuth Redirect URI                |
| `CLOUDINARY_URL`       | Cloudinary API endpoint           |
| `API_KEY`              | API key for external integrations |

---

## Build and Run

### Local Development

1. Clone the repository:
   ```bash
   git clone https://github.com/<your-repository>.git
   cd <your-repository>
2. Set up the required environment variables.

3. Build the project:
   ```bash
   mvn clean install
4. Run the application:
   ```bash
   java -jar target/*.jar
The server will run at http://localhost:8080.
---

## Development
## Development Deployment
1. Ensure Docker and Docker Compose are installed.
2. Use the provided docker-compose.yml to spin up the application and its dependencies:
   ```bash
   docker-compose up -d
3. Access the application at http://localhost:8080.

## Production Deployment
The CI/CD pipeline automates the deployment process. Ensure that all secrets are configured in GitHub Actions.

Steps:
1. Commit changes to the main branch.
2. GitHub Actions will:
 * Build the project
 * Run tests
 * Build and push a Docker image to Docker Hub
 * Deploy the image to Azure Web Apps using the configured publish profile.
---

## Docker
### Build the Docker Image
Manually build the Docker image:
   ```
   docker build -t <your-dockerhub-username>/tmdb-back-end:1.0
   ```
### Push Docker Image
```
docker push <your-dockerhub-username>/tmdb-back-end:1.0
```
---

## CI/CD Workflow
### Development Branch
* Defined in ```docker-ci-cd-dev.yaml```
* Triggers:
  * Push to ```dev``` branch
  * Pull requests targeting ```dev```
* Actions:
  * Build and test the application
  * Push Docker image tagged ```dev``` to Docker Hub
### Main Branch
* Defined in docker-ci-cd-main.yaml
* Triggers:
  * Push to `main` branch
  * Pull requests targeting ```main```
*Actions:
  * Build and test the application
  * Push Docker image tagged production to Docker Hub
  * Deploy to Azure Web App



