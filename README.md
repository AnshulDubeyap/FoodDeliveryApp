## Running the Project with Docker

This project provides Docker and Docker Compose configurations for easy setup and deployment of the Java Spring Boot application.

### Project-Specific Docker Requirements
- **Base Image:** Uses `eclipse-temurin:21-jdk` for building and `eclipse-temurin:21-jre` for runtime (Java 21 required).
- **Build Tool:** Maven Wrapper (`mvnw`) is used for building the project inside the container.
- **Ports:** The application exposes port **8080** (Spring Boot default).
- **User:** Runs as a non-root user (`appuser`) for improved security.

### Environment Variables
- No environment variables are strictly required by default in the provided Dockerfiles or Compose file.
- If your application requires environment variables (e.g., database credentials), you can add them to a `.env` file and uncomment the `env_file` line in `docker-compose.yml`.

### Build and Run Instructions
1. **Build and Start the Application:**
   ```sh
   docker compose up --build
   ```
   This will build the Docker image and start the `java-app` service on port **8080**.

2. **Access the Application:**
   - The application will be available at `http://localhost:8080`.

### Special Configuration
- **Database:**
  - By default, no database service is included. If your application requires a database (e.g., PostgreSQL), uncomment and configure the `postgres` service section in `docker-compose.yml`.
  - Update your application's configuration (e.g., `application.properties`) to match the database connection details.
- **Networks:**
  - All services are connected to the custom `appnet` bridge network for isolation.

### Exposed Ports
- **java-app:** `8080:8080` (host:container)
- **postgres (optional):** `5432:5432` (host:container) if enabled

---
_Ensure you have Docker and Docker Compose installed. For any additional configuration (such as environment variables or database setup), refer to the comments in the provided `docker-compose.yml` file._