# Architecture Documentation

This directory contains architecture documentation for the HelixCart platform.

## Current Architecture: Phase 1 — Modular Monolith

### Overview

HelixCart begins as a **modular monolith** — a single deployable unit with clearly separated internal modules. This is a deliberate starting point, not a limitation.

The modular monolith gives us:
- Fast development iteration
- Simple deployment and debugging
- Clear domain boundaries that can be extracted later
- No distributed systems complexity before it is needed

### High-Level Architecture

```
┌─────────────────────────────────────────────────────────┐
│                    HelixCart Monolith                   |
│                                                         │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌────────┐   │
│  │   Auth   │  │ Catalog  │  │  Order   │  │  Inv.  │   │
│  │  Module  │  │  Module  │  │  Module  │  │ Module │   │
│  └──────────┘  └──────────┘  └──────────┘  └────────┘   │
│                                                         │
│  ┌─────────────────────────────────────────────────┐    │
│  │              Shared Infrastructure              │    │
│  │  (Logging, Correlation IDs, Error Handling)     │    │
│  └─────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────┘
         │                              │
         ▼                              ▼
   ┌──────────┐                   ┌──────────┐
   │PostgreSQL│                   │  Redis   │
   └──────────┘                   └──────────┘
```

### Module Boundaries

Each module follows clean architecture internally:

```
module/
├── domain/        # Entities, value objects, domain events, repository interfaces
├── application/   # Use cases, application services
├── infrastructure/# JPA repositories, cache adapters, external clients
└── api/           # REST controllers, DTOs, request/response mappers
```

Modules communicate through well-defined interfaces — never by directly accessing another module's internal classes or database tables.

### Technology Decisions

| Concern | Technology | Rationale |
|---------|-----------|-----------|
| Language | Java 21 | LTS, virtual threads, mature ecosystem |
| Framework | Spring Boot 3.x | Industry standard, excellent observability support |
| Database | PostgreSQL 16 | Reliable, feature-rich, excellent JSON support |
| Cache | Redis 7 | Fast, versatile, supports multiple data structures |
| Migrations | Flyway | Version-controlled, repeatable DB migrations |
| Logging | Logback + logstash-logback-encoder | JSON structured logging |
| Metrics | Spring Boot Actuator + Micrometer | Prometheus-compatible out of the box |

### Evolution Path

This architecture is designed to evolve:

1. **Phase 1-2:** Modular monolith — single deployment, multiple modules
2. **Phase 3:** API Gateway introduced — auth extracted to gateway layer
3. **Phase 4:** Event-driven communication — Kafka for cross-domain events
4. **Phase 5:** Service extraction — modules become independent services when justified

See `docs/adr/` for the decision records behind each evolution step.

## Architecture Diagrams

- `diagrams/phase1-modular-monolith.md` — Current architecture
- `diagrams/phase3-gateway.md` — Target architecture for Phase 3 (planned)
- `diagrams/phase5-microservices.md` — Long-term target (planned)
