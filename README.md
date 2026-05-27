# HelixCart

> Cloud-Native Commerce Platform

HelixCart is a deeply engineered cloud-native commerce platform built to demonstrate senior-level backend engineering, distributed systems design, DevOps maturity, observability, and platform engineering.

This is not a frontend-heavy ecommerce clone. It is an **engineering evolution journey** — a system that grows intentionally from a modular monolith to a distributed, observable, production-grade platform.

---

## What This Project Demonstrates

| Discipline | What You'll See |
|-----------|----------------|
| **Backend Engineering** | Clean architecture, domain modeling, API design, data patterns |
| **Distributed Systems** | Service communication, eventual consistency, fault tolerance |
| **Cloud-Native** | Containerization, 12-factor principles, Kubernetes-native design |
| **DevOps** | CI/CD pipelines, GitOps, automated testing, release management |
| **DevSecOps** | SAST, container scanning, secrets management, zero-trust |
| **Observability** | Structured logging, Prometheus metrics, distributed tracing |
| **Reliability** | Health checks, graceful degradation, retry logic, circuit breakers |

---

## Current Phase: Phase 1 — Foundation Complete

The foundation is complete. No domain features yet — only structure, standards, infrastructure integration, and operational skeleton.

Next planned work is **Phase 2 — Core Backend**, starting with the domain modules documented in the roadmap.

See [docs/roadmap/README.md](docs/roadmap/README.md) for the full evolution plan.

---

## Repository Structure

```
helixcart-platform/
│
├── .rules/                    # Engineering standards and principles
│   ├── architecture-principles.md
│   ├── ai-collaboration.md
│   ├── coding-guidelines.md
│   ├── commit-conventions.md
│   ├── database-migration-rules.md
│   ├── documentation-rules.md
│   ├── module-boundary-rules.md
│   ├── quality-gates.md
│   ├── security-rules.md
│   ├── service-standards.md
│   ├── testing-policy.md
│   ├── decision-framework.md
│   └── project-scope.md
│
├── docs/
│   ├── roadmap/               # Phase-by-phase evolution plan
│   ├── architecture/          # Architecture documentation and diagrams
│   ├── adr/                   # Architecture Decision Records
│   ├── tradeoffs/             # Technology tradeoff analyses
│   ├── runbooks/              # Operational runbooks
│   ├── postmortems/           # Incident postmortems
│   ├── learnings/             # Engineering learnings per milestone
│   └── security/              # Security documentation
│
├── services/
│   └── helixcart-app/         # Phase 1-2: Modular monolith (Spring Boot)
│
├── gateway/                   # Phase 3: Spring Cloud Gateway
├── infrastructure/
│   ├── docker/                # Docker Compose for local development
│   ├── helm/                  # Phase 5: Helm charts
│   ├── kubernetes/            # Phase 5: Kubernetes manifests
│   └── terraform/             # Phase 5: Infrastructure as Code
│
├── observability/             # Phase 6: Prometheus, Grafana, OpenTelemetry configs
├── scripts/                   # Utility scripts
└── .github/
    └── workflows/             # GitHub Actions CI/CD pipelines
```

---

## Technology Stack

| Area | Technology | Phase |
|------|-----------|-------|
| Language | Java 21 | 1 |
| Framework | Spring Boot 3.5.14 | 1 |
| Database | PostgreSQL 16 | 1 |
| Cache | Redis 7 | 1 |
| Migrations | Flyway | 1 |
| Containerization | Docker | 1 |
| Security | Spring Security + Zitadel | 2 |
| API Gateway | Spring Cloud Gateway | 3 |
| Messaging | Kafka | 4 |
| Orchestration | Kubernetes + Helm | 5 |
| GitOps | ArgoCD | 5 |
| CI/CD | GitHub Actions | 1+ |
| Metrics | Prometheus + Grafana | 6 |
| Logging | ELK / OpenSearch | 6 |
| Tracing | OpenTelemetry | 6 |
| Security Scanning | Trivy + Semgrep | 1+ |

---

## Getting Started

### Prerequisites

- Java 21
- Docker and Docker Compose
- Git

### Start Local Infrastructure

```bash
docker compose -f infrastructure/docker/docker-compose.yml up -d
```

### Run the Application

```bash
cd services/helixcart-app
cp .env.example .env
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

### Verify

```bash
# Health check
curl http://localhost:8080/actuator/health

# API documentation
open http://localhost:8080/swagger-ui.html
```

### CI Secrets (GitHub Actions)

For the full CI pipeline to run with all features, configure these secrets in **GitHub → Settings → Secrets and variables → Actions**:

| Secret | Purpose | Required |
|--------|---------|----------|
| `NVD_API_KEY` | Authenticates OWASP Dependency Check against the NVD API — prevents rate limiting and speeds up CVE scans. Get one free at [nvd.nist.gov/developers/request-an-api-key](https://nvd.nist.gov/developers/request-an-api-key). | Optional (recommended) |
| `SEMGREP_APP_TOKEN` | Enables Semgrep Cloud dashboard and PR annotations. Get one at [semgrep.dev](https://semgrep.dev). | Optional |

The pipeline degrades gracefully without these — scans still run in a limited mode.

---

## Engineering Standards

All engineering standards are documented in `.rules/`:

- [Architecture Principles](.rules/architecture-principles.md)
- [Coding Guidelines](.rules/coding-guidelines.md)
- [Commit Conventions](.rules/commit-conventions.md)
- [Module Boundary Rules](.rules/module-boundary-rules.md)
- [Database Migration Rules](.rules/database-migration-rules.md)
- [Quality Gates](.rules/quality-gates.md)
- [Security Rules](.rules/security-rules.md)
- [Testing Policy](.rules/testing-policy.md)
- [Decision Framework](.rules/decision-framework.md)
- [AI Collaboration Rules](.rules/ai-collaboration.md)

---

## Architecture Decisions

All significant decisions are recorded as ADRs in `docs/adr/`:

- [ADR-001: Modular Monolith](docs/adr/ADR-001-modular-monolith.md)
- [ADR-002: PostgreSQL as Primary Database](docs/adr/ADR-002-postgresql-primary-database.md)
- [ADR-003: Redis for Caching](docs/adr/ADR-003-redis-caching.md)

---

## Contributing

This project follows strict engineering standards. Before contributing:

1. Read `.rules/coding-guidelines.md`
2. Read `.rules/commit-conventions.md`
3. Read `.rules/decision-framework.md`
4. Ensure all tests pass: `./mvnw verify`
5. Follow the ADR process for any architectural changes

---

## License

MIT License — see [LICENSE](LICENSE) for details.
