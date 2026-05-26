# Architecture Principles

## Core Principles

- Start with modular monolith before microservices.
- Introduce complexity only when justified by a real, observed problem.
- Every service owns its own data — no shared databases across domain boundaries.
- Prefer asynchronous communication for distributed workflows.
- Prefer event-driven architecture for cross-domain operations.
- Design for observability from day one (logs, metrics, traces).
- Reliability is more important than feature count.
- Infrastructure must be reproducible and version-controlled.
- All architecture changes require ADR updates before implementation.
- Avoid unnecessary technology adoption.

## Evolutionary Architecture

The system evolves through defined phases:

| Phase | Focus |
|-------|-------|
| 1 | Foundation — monolith, PostgreSQL, Redis, Docker |
| 2 | Core Backend — Auth, Catalog, Orders, Inventory |
| 3 | Architecture Evolution — API Gateway, service extraction |
| 4 | Event-Driven — Kafka, Saga, DLQ, idempotency |
| 5 | Infrastructure Maturity — Kubernetes, Helm, GitOps |
| 6 | Observability & Security — metrics, tracing, scanning |

## Decision Gate

Before any architectural change, answer:
1. What problem exists today?
2. Why does the current design fail to solve it?
3. What alternatives were considered?
4. What are the tradeoffs of this choice?
5. What operational complexity does this introduce?

If these questions cannot be answered clearly, the change is premature.

## Data Ownership

- Each domain module owns its schema.
- Cross-domain reads use APIs or events — never direct DB joins.
- Shared data models are a design smell.

## Communication Patterns

| Pattern | When to Use |
|---------|-------------|
| Synchronous REST | User-facing, low-latency reads |
| Async Messaging | Cross-domain state changes |
| Events | Notifications, audit, eventual consistency |

## Anti-Patterns to Avoid

- Distributed monolith (microservices with shared DB)
- Premature service extraction
- Chatty inter-service communication
- Synchronous chains across 3+ services
- Shared utility libraries that couple services
