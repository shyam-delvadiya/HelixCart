# Module Boundary Rules

## Purpose

HelixCart starts as a modular monolith. The code is deployed as one Spring Boot application, but each domain module must be designed as if it could become an independent service later.

These rules protect that future extraction path.

## Domain Modules

The primary modules are:

| Module | Responsibility |
|--------|----------------|
| `auth` | Identity context, authorization, roles, user security concerns |
| `catalog` | Products, categories, pricing, product search |
| `order` | Order lifecycle, order state transitions, customer order history |
| `inventory` | Stock levels, reservations, replenishment thresholds |

Shared technical code belongs under `common` or `config`. Shared code must stay generic and must not contain domain-specific business rules.

## Package Shape

Each domain module follows this shape:

```text
com.helixcart.<module>/
├── domain/           # Entities, value objects, domain events, domain services
├── application/      # Use cases, ports, application services
├── infrastructure/   # JPA, Redis, messaging, external adapters
└── api/              # Controllers, request/response DTOs, API mappers
```

## Allowed Dependencies

- `api` may call `application`.
- `application` may call `domain`.
- `application` may depend on ports/interfaces, not adapter implementations.
- `infrastructure` may implement application ports and use framework libraries.
- `domain` must not depend on Spring, JPA, Redis, HTTP, messaging, or another module.
- Any layer may use truly generic shared code from `common`.

## Cross-Module Communication

- A module must not access another module's `domain`, `infrastructure`, or persistence model directly.
- Cross-module reads must go through public application ports, APIs, or purpose-built query interfaces.
- Cross-module state changes should prefer events once the eventing foundation exists.
- Direct database joins across module-owned tables are not allowed in application code.
- Shared DTOs between modules are discouraged. Prefer module-owned request/response models.

## Database Ownership

- Each module owns its tables and migrations.
- A module may reference another module's identifier, but it must not rely on another module's internal table shape.
- Foreign keys across modules are acceptable in Phase 1 only when they protect core consistency. They must be revisited before service extraction.

## Enforcement

- Phase 1: enforce by review and package discipline.
- Phase 2: add ArchUnit tests for package/layer boundaries.
- Phase 3+: revisit module boundaries before extracting services.

## Review Checklist

- [ ] Does this change keep domain logic framework-free?
- [ ] Does this change avoid reaching into another module's internals?
- [ ] Is shared code generic, or should it belong to one module?
- [ ] Would this module still be extractable after this change?
- [ ] Are database ownership assumptions documented?
