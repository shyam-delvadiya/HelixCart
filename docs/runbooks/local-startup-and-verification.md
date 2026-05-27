# Runbook: Local Startup and Verification

**Service:** helixcart-app
**Last Updated:** 2026-05-27

## Purpose

Start the Phase 1 HelixCart stack locally and verify that the application can connect to PostgreSQL and Redis, expose health checks, and publish Prometheus metrics.

## Prerequisites

- Java 21
- Docker and Docker Compose
- Repository checked out locally

## Steps

1. Start local infrastructure:

   ```bash
   docker compose -f infrastructure/docker/docker-compose.yml up -d
   ```

2. Run the application:

   ```bash
   cd services/helixcart-app
   cp .env.example .env
   ./mvnw spring-boot:run -Dspring-boot.run.profiles=local
   ```

3. Build the production image:

   ```bash
   cd services/helixcart-app
   docker build -t helixcart-app:local .
   ```

## Verification

Health must return `UP` and include healthy `db` and `redis` components:

```bash
curl http://localhost:8080/actuator/health
```

Metrics must be available:

```bash
curl http://localhost:8080/actuator/prometheus
```

OpenAPI documentation must be available:

```bash
curl http://localhost:8080/api-docs
```

## Rollback

Stop the application process with `Ctrl+C`, then stop local infrastructure:

```bash
docker compose -f infrastructure/docker/docker-compose.yml down
```

Use `down -v` only when local PostgreSQL and Redis data should be deleted.

## Escalation

If health is not `UP`, check application logs first. Common causes are Docker not running, ports `5432`, `6379`, or `8080` already being used, or local `.env` values not matching Docker Compose credentials.
