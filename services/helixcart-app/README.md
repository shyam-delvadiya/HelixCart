# helixcart-app

The HelixCart modular monolith — the core application for Phase 1 and Phase 2.

## Purpose

This service is the single deployable unit containing all four domain modules:
- **auth** — Authentication and authorization (Phase 2)
- **catalog** — Product and category management (Phase 2)
- **order** — Order lifecycle management (Phase 2)
- **inventory** — Stock and reservation management (Phase 2)

Phase 1 establishes the foundation: project structure, configuration, observability, and infrastructure integration. Domain modules are scaffolded but not yet implemented.

## Domain Responsibilities

| Module | Owns |
|--------|------|
| auth | User identity, JWT validation, RBAC roles |
| catalog | Products, categories, pricing |
| order | Order creation, state transitions, order history |
| inventory | Stock levels, reservations, reorder thresholds |

## API Overview

- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/api-docs
- Health: http://localhost:8080/actuator/health
- Metrics: http://localhost:8080/actuator/prometheus

## Dependencies

| Dependency | Purpose | Local Port |
|-----------|---------|-----------|
| PostgreSQL 16 | Primary database | 5432 |
| Redis 7 | Cache and session store | 6379 |

## Configuration

All configuration is via environment variables. See `.env.example` for the full reference.

Key variables:

| Variable | Default | Description |
|----------|---------|-------------|
| `DATABASE_URL` | `jdbc:postgresql://localhost:5432/helixcart` | PostgreSQL JDBC URL |
| `DATABASE_USERNAME` | `helixcart` | DB username |
| `DATABASE_PASSWORD` | `helixcart` | DB password |
| `REDIS_HOST` | `localhost` | Redis hostname |
| `REDIS_PORT` | `6379` | Redis port |
| `SPRING_PROFILES_ACTIVE` | `local` | Active Spring profile |
| `SERVER_PORT` | `8080` | HTTP port |

### CI Secrets

These are not local environment variables — they are GitHub Actions secrets configured in the repository settings. They are not needed to run the application locally.

| Secret | Purpose |
|--------|---------|
| `NVD_API_KEY` | Authenticates OWASP Dependency Check against the NVD API. Without it the scan still runs but is rate-limited. Free key at [nvd.nist.gov/developers/request-an-api-key](https://nvd.nist.gov/developers/request-an-api-key). |
| `SEMGREP_APP_TOKEN` | Enables Semgrep Cloud dashboard. Without it Semgrep runs in OSS mode. |

## Running Locally

### Prerequisites
- Java 21
- Docker and Docker Compose

### 1. Start infrastructure

```bash
docker compose -f ../../infrastructure/docker/docker-compose.yml up -d
```

### 2. Copy environment file

```bash
cp .env.example .env
```

### 3. Run the application

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

The application starts at http://localhost:8080.

### 4. Verify

```bash
curl http://localhost:8080/actuator/health
```

Expected:
```json
{"status": "UP"}
```

## Testing

```bash
# Run all tests (requires Docker for Testcontainers)
./mvnw verify

# Run unit tests only (no Docker required)
./mvnw test -Dgroups=unit

# Run with coverage report
./mvnw verify jacoco:report
```

## Architecture Notes

- Clean architecture: domain → application → infrastructure → api layers
- Flyway manages all schema migrations in `src/main/resources/db/migration/`
- Correlation IDs are propagated via `X-Correlation-ID` header and SLF4J MDC
- JSON structured logging via logstash-logback-encoder
- Redis cache uses JSON serialization (not Java serialization) for portability

See `docs/architecture/README.md` and `docs/adr/` for design decisions.
