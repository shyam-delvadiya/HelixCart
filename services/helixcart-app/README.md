# helixcart-app

The HelixCart modular monolith — the core application for Phase 1 and Phase 2.

## Purpose

This service is the single deployable unit containing all four domain modules:
- **auth** — Authentication and authorization (Phase 2)
- **catalog** — Product and category management (Phase 2)
- **order** — Order lifecycle management (Phase 2)
- **inventory** — Stock and reservation management (Phase 2)

Phase 1 established the foundation: project structure, configuration, observability, and infrastructure integration. Phase 2 is now in progress, starting with the Catalog product slice.

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

### Catalog API

The first Phase 2 Catalog slice exposes product and category management:

| Method | Path | Purpose |
|--------|------|---------|
| `POST` | `/api/v1/products` | Create a product |
| `PUT` | `/api/v1/products/{productId}` | Replace product details |
| `PATCH` | `/api/v1/products/{productId}/status` | Change product status |
| `GET` | `/api/v1/products/{productId}` | Fetch product by ID |
| `GET` | `/api/v1/products/sku/{sku}` | Fetch product by SKU |
| `GET` | `/api/v1/products` | Search products by text/status with pagination |
| `POST` | `/api/v1/categories` | Create a category |
| `PUT` | `/api/v1/categories/{categoryId}` | Replace category details |
| `GET` | `/api/v1/categories/{categoryId}` | Fetch category by ID |
| `GET` | `/api/v1/categories/slug/{slug}` | Fetch category by slug |
| `GET` | `/api/v1/categories` | Search categories by text with pagination |

These endpoints are protected by the Phase 1 security skeleton. JWT validation and RBAC are planned in the Phase 2 Auth slice.

### Inventory API

The Phase 2 Inventory slice exposes stock and reservation management:

| Method | Path | Purpose |
|--------|------|---------|
| `POST` | `/api/v1/inventory` | Create inventory for a product |
| `GET` | `/api/v1/inventory/products/{productId}` | Fetch inventory by product |
| `PATCH` | `/api/v1/inventory/products/{productId}/stock` | Adjust stock on hand |
| `PATCH` | `/api/v1/inventory/products/{productId}/reservations` | Reserve stock |
| `PATCH` | `/api/v1/inventory/products/{productId}/reservations/release` | Release reserved stock |

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

# Run the catalog service unit tests only (no Docker required)
./mvnw -Dtest=ProductCatalogServiceTest,CategoryCatalogServiceTest,InventoryCatalogServiceTest test

# Run with coverage report
./mvnw verify jacoco:report
```

## Architecture Notes

- Clean architecture: domain → application → infrastructure → api layers
- Catalog product/category and Inventory stock/reservation functionality are implemented as Phase 2 vertical slices
- Flyway manages all schema migrations in `src/main/resources/db/migration/`
- Correlation IDs are propagated via `X-Correlation-ID` header and SLF4J MDC
- JSON structured logging via logstash-logback-encoder
- Redis cache uses JSON serialization (not Java serialization) for portability

See `docs/architecture/README.md` and `docs/adr/` for design decisions.
