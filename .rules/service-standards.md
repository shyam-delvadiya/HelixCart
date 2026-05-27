# Service Standards

## Every Service Must Include

| Artifact | Location | Purpose |
|----------|----------|---------|
| `Dockerfile` | service root | Production-ready container build |
| `README.md` | service root | Service documentation |
| Health endpoint | `GET /actuator/health` | Liveness and readiness |
| Metrics endpoint | `GET /actuator/prometheus` | Prometheus scraping |
| OpenAPI spec | `GET /api-docs` or `openapi.yaml` | API documentation |
| Structured logging | All log statements | Observability |
| `.env.example` | service root | Configuration reference |
| Tests | `src/test/` | Unit + integration coverage |

## Health Endpoint Contract

```json
GET /actuator/health

{
  "status": "UP",
  "components": {
    "db": { "status": "UP" },
    "redis": { "status": "UP" }
  }
}
```

Kubernetes probes must use this endpoint:
- **Liveness probe:** `/actuator/health/liveness`
- **Readiness probe:** `/actuator/health/readiness`

## Metrics Requirements

- Expose Prometheus metrics at `/actuator/prometheus`.
- Include custom business metrics where meaningful (e.g., orders created, auth failures).
- Standard JVM and HTTP metrics are provided automatically by Spring Boot Actuator.

## Logging Requirements

- JSON structured logging (Logback with `logstash-logback-encoder`).
- Every log entry includes: `timestamp`, `level`, `service`, `correlationId`.
- Log at startup: service name, version, active profile, key config (non-sensitive).

## Dockerfile Standards

```dockerfile
# Stage 1: Build
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app
COPY . .
RUN ./mvnw package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
COPY --from=builder /app/target/*.jar app.jar
USER appuser
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

Requirements:
- Multi-stage build (no build tools in runtime image)
- Non-root user
- Minimal base image (Alpine or distroless)
- No secrets baked into image
- EXPOSE the correct port

## Environment Configuration

All services must support these standard environment variables:

| Variable | Description |
|----------|-------------|
| `SERVER_PORT` | HTTP port (default: 8080) |
| `SPRING_PROFILES_ACTIVE` | Active Spring profile |
| `DATABASE_URL` | JDBC connection URL |
| `DATABASE_USERNAME` | DB username |
| `DATABASE_PASSWORD` | DB password |
| `REDIS_HOST` | Redis hostname |
| `REDIS_PORT` | Redis port |
| `LOG_LEVEL` | Root log level |

## Package Structure

```
com.helixcart.<service>/
├── domain/           # Entities, value objects, domain events
├── application/      # Use cases, service interfaces
├── infrastructure/   # DB, cache, messaging adapters
├── api/              # Controllers, DTOs, mappers
└── config/           # Spring configuration classes
```

## Readiness Checklist

Before a service is considered phase-complete:

- [ ] Dockerfile present and builds successfully
- [ ] Health endpoint returns UP
- [ ] Metrics endpoint accessible
- [ ] OpenAPI documentation complete
- [ ] README written
- [ ] Structured logging configured
- [ ] Correlation ID propagation working
- [ ] `.env.example` present
- [ ] Unit tests passing
- [ ] Integration tests passing
- [ ] No hardcoded secrets
- [ ] Container runs as non-root
