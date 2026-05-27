# HelixCart Roadmap

This directory tracks the evolution of the HelixCart platform across phases.

## Phase Overview

| Phase | Name | Status |
|-------|------|--------|
| 1 | Foundation | ✅ Complete |
| 2 | Core Backend | ⬜ Planned |
| 3 | Architecture Evolution | ⬜ Planned |
| 4 | Event-Driven Architecture | ⬜ Planned |
| 5 | Infrastructure Maturity | ⬜ Planned |
| 6 | Observability & Security | ⬜ Planned |

---

## Phase 1 — Foundation

**Goal:** Establish the engineering foundation. No features yet — only structure, standards, and skeleton.

**Status:** Complete as of 2026-05-27.

### Objectives
- [x] Repository structure created
- [x] `.rules/` engineering standards documented
- [x] `docs/` structure established
- [x] Spring Boot modular monolith skeleton
- [x] PostgreSQL integration with Flyway migrations
- [x] Redis integration
- [x] Structured JSON logging configured
- [x] Correlation ID filter implemented
- [x] Health and metrics endpoints active
- [x] Dockerfile (multi-stage, non-root)
- [x] Docker Compose for local development
- [x] GitHub Actions CI skeleton
- [x] Initial ADR written (ADR-001: Modular Monolith)
- [x] Git repository initialized with `main` branch protection hook

**Exit Criteria:** The application starts, connects to PostgreSQL and Redis, logs in JSON, exposes health/metrics, and runs in Docker.

**Verification:**
- `./mvnw verify` passes from `services/helixcart-app`.
- `docker build -t helixcart-app:local .` passes from `services/helixcart-app`.
- `GET /actuator/health` returns `UP` for the app container with PostgreSQL and Redis healthy.

---

## Phase 2 — Core Backend

**Goal:** Implement the four core domain modules with full engineering standards.

### Objectives
- [ ] Auth module (Zitadel integration, JWT validation, RBAC)
- [ ] Catalog module (products, categories, search)
- [ ] Order module (order lifecycle, state machine)
- [ ] Inventory module (stock management, reservations)
- [ ] OpenAPI documentation for all modules
- [ ] Integration tests with Testcontainers
- [ ] ADRs for key design decisions

**Exit Criteria:** All four modules functional, tested, documented, and observable.

---

## Phase 3 — Architecture Evolution

**Goal:** Extract the API Gateway and begin service boundary definition.

### Objectives
- [ ] Spring Cloud Gateway setup
- [ ] JWT validation at gateway layer
- [ ] Rate limiting at gateway
- [ ] Service extraction planning (ADR required)
- [ ] Inter-service communication patterns defined

---

## Phase 4 — Event-Driven Architecture

**Goal:** Introduce Kafka and implement event-driven patterns for cross-domain workflows.

### Objectives
- [ ] Kafka cluster (local via Docker Compose)
- [ ] Domain events defined and published
- [ ] Saga pattern for order workflow
- [ ] Dead Letter Queue (DLQ) handling
- [ ] Idempotency implementation
- [ ] Retry with exponential backoff

---

## Phase 5 — Infrastructure Maturity

**Goal:** Deploy to Kubernetes with full GitOps workflow.

### Objectives
- [ ] Kubernetes manifests for all services
- [ ] Helm charts
- [ ] GitHub Actions full CI/CD pipeline
- [ ] ArgoCD GitOps setup
- [ ] Horizontal Pod Autoscaler
- [ ] Resource limits and requests defined

---

## Phase 6 — Observability & Security

**Goal:** Full production-grade observability and security posture.

### Objectives
- [ ] Prometheus + Grafana dashboards
- [ ] ELK / OpenSearch log aggregation
- [ ] OpenTelemetry distributed tracing
- [ ] Alerting rules defined
- [ ] Trivy image scan: enforce as hard gate (currently warn-only in Phase 1)
- [ ] Semgrep SAST: enforce as hard gate (currently warn-only in Phase 1)
- [ ] Security runbooks written
