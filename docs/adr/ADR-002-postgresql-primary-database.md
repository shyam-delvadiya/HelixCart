# ADR-002: PostgreSQL as Primary Database

**Date:** 2025-05-25
**Status:** Accepted

---

## Context

HelixCart requires a relational database for storing structured commerce data: products, orders, inventory, and user records. We need to select a primary database technology for Phase 1.

---

## Decision

Use **PostgreSQL 16** as the primary relational database for all domain modules.

---

## Rationale

1. **Proven reliability.** PostgreSQL is battle-tested in production at scale across the industry.
2. **Rich feature set.** JSONB support, full-text search, window functions, CTEs, and advanced indexing cover most use cases without additional tools.
3. **ACID compliance.** Commerce data requires strong consistency guarantees — orders, inventory, and payments cannot tolerate eventual consistency at the data layer.
4. **Excellent Spring/JPA integration.** First-class support in Spring Data JPA and Hibernate.
5. **Flyway compatibility.** Clean migration support for version-controlled schema evolution.
6. **Open source.** No licensing cost or vendor lock-in.
7. **Kubernetes-ready.** Well-supported Helm charts and operators available for Phase 5.

---

## Alternatives Considered

| Option | Pros | Cons |
|--------|------|------|
| **PostgreSQL** (selected) | Reliable, feature-rich, open source, excellent ecosystem | Requires operational knowledge for tuning at scale |
| **MySQL / MariaDB** | Widely used, simple | Fewer advanced features, weaker JSON support, historical quirks |
| **MongoDB** | Flexible schema, document model | Eventual consistency by default, less suited for transactional commerce data |
| **H2 (in-memory)** | Zero setup for dev | Not production-viable, behavior differences from real PostgreSQL |

---

## Consequences

### Positive
- Strong consistency for all commerce transactions.
- Rich query capabilities without additional tools.
- Testcontainers support for integration tests with real PostgreSQL.
- Clear migration path to managed services (AWS RDS, Cloud SQL) in later phases.

### Negative
- Requires running PostgreSQL locally (Docker Compose handles this).
- Schema migrations require care — Flyway enforces discipline here.

### Risks
- Connection pool exhaustion under high load.
- Mitigation: HikariCP connection pool tuning, monitored via Micrometer metrics.

---

## Schema Management

- All schema changes managed via **Flyway** migrations.
- Migration files versioned in `src/main/resources/db/migration/`.
- Naming convention: `V{version}__{description}.sql` (e.g., `V1__create_products_table.sql`).
- Migrations are applied automatically on startup in all environments.
- Rollback scripts documented for production migrations.

---

## References

- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [Flyway Documentation](https://flywaydb.org/documentation/)
- [Testcontainers PostgreSQL](https://testcontainers.com/modules/postgresql/)
