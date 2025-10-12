## Running the Project with Docker

This project provides Docker and Docker Compose configurations for easy setup and deployment.

### Project-Specific Docker Requirements
- **Java Version:** Uses Eclipse Temurin JDK 21 for building and JRE 21 for running the application.
- **Build Tool:** Maven Wrapper (`mvnw`) is used for building the project inside the container.
- **Ports:** The application exposes port **8080** (default Spring Boot port).

### Environment Variables
- The Dockerfile sets `JAVA_OPTS` for container-aware JVM memory settings:
  - `-XX:MaxRAMPercentage=80.0`
  - `-XX:+UseContainerSupport`
- No required environment variables are specified by default. If you need to set custom environment variables, you can use a `.env` file and uncomment the `env_file` line in `docker-compose.yml`.

### Build and Run Instructions
1. **Build and Start the Application:**
   ```sh
   docker compose up --build
   ```
   This will build the application using Maven and start the container.

2. **Access the Application:**
   - The service will be available at [http://localhost:8080](http://localhost:8080).

### Special Configuration
- The container runs as a non-root user (`appuser`) for improved security.
- If you need to add additional services (e.g., a database), update `docker-compose.yml` and use the `depends_on` section.
- The project uses a custom Docker network (`appnet`) for inter-service communication.

### Exposed Ports
- **java-app:** `8080:8080` (host:container)

---
*Update this section if you add new services, environment variables, or change port mappings in your Docker setup.*
