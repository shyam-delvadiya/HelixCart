# Project Scope

## Primary Focus Areas

These are the core engineering disciplines HelixCart is designed to demonstrate:

| Area | Description |
|------|-------------|
| **Backend Engineering** | Clean architecture, domain modeling, API design, data access patterns |
| **Distributed Systems** | Service communication, eventual consistency, fault tolerance |
| **Cloud-Native Architecture** | Containerization, orchestration, 12-factor app principles |
| **Kubernetes** | Deployments, services, config maps, secrets, probes, HPA |
| **DevOps** | CI/CD pipelines, GitOps, automated testing, release management |
| **DevSecOps** | Security scanning, SAST, container scanning, secrets management |
| **Observability** | Structured logging, metrics, distributed tracing, alerting |
| **Reliability Engineering** | Health checks, graceful degradation, retry logic, circuit breakers |

## Secondary Focus Areas

These are supported but not the primary showcase:

- Minimal frontend (admin dashboards, API interaction surfaces)
- Basic UI for demonstrating API functionality
- Simple reporting views

## Non-Goals

These are explicitly out of scope and will not be built:

- Pixel-perfect, production-quality frontend
- Enterprise-scale feature completeness (full ecommerce feature parity)
- Mobile applications (iOS, Android, React Native)
- Marketing site or landing pages
- Payment processing integration (beyond stub/mock)
- Recommendation engine or ML features
- Multi-tenant SaaS architecture
- Internationalization / localization

## Scope Boundary Test

When evaluating whether to build something, ask:

> "Does this demonstrate backend engineering depth, operational maturity, or system design evolution?"

If yes → consider it.
If no → it is out of scope.

## Phase Scope

Each phase has a defined scope. Work outside the current phase scope is deferred, not abandoned.

| Phase | In Scope |
|-------|----------|
| 1 | Repository setup, rules, docs, modular monolith skeleton, PostgreSQL, Redis, Docker |
| 2 | Auth, Catalog, Orders, Inventory modules, JWT, RBAC |
| 3 | API Gateway, service extraction, distributed communication |
| 4 | Kafka, Saga pattern, DLQ, idempotency, retry handling |
| 5 | Kubernetes, Helm, GitHub Actions, ArgoCD, GitOps |
| 6 | Prometheus, Grafana, ELK/OpenSearch, OpenTelemetry, Trivy, Semgrep |

## Engineering Depth Over Feature Breadth

A single well-engineered service with proper observability, security, testing, and documentation is worth more than five poorly engineered services.

Prefer:
- One service done right over three services done quickly
- Deep understanding over surface-level implementation
- Documented decisions over undocumented cleverness
- Operational maturity over feature count
