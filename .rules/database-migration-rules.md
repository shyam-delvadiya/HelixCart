# Database Migration Rules

## Purpose

Flyway owns schema evolution. Hibernate must validate schema only and must never create, update, or drop production schema.

## Migration Location

All application migrations live under:

```text
services/helixcart-app/src/main/resources/db/migration/
```

## Naming

Use Flyway versioned migration naming:

```text
V<version>__<short_snake_case_description>.sql
```

Examples:

```text
V1__init_schema.sql
V2__create_catalog_tables.sql
V3__add_inventory_reservations.sql
```

## Ownership

- Each domain module owns its schema objects.
- Table names use `snake_case`.
- Primary keys use UUID unless a decision record justifies otherwise.
- Common timestamp columns are `created_at` and `updated_at`.
- Money values use `NUMERIC`, not floating point types.
- Schema changes that affect module boundaries require an ADR or an ADR update.

## Safety Rules

- Never edit an already-applied migration after it has been shared.
- Add a new migration for every schema change.
- Destructive changes require an explicit rollback plan in the PR description.
- Production-impacting migrations must be backward compatible where possible.
- Large data migrations must be separated from schema migrations when operationally useful.
- Avoid long locks on hot tables. Use phased migrations for risky changes.

## Rollback Expectations

Flyway Community does not provide automatic rollback. Every risky migration must document:

- What can go wrong.
- How to verify success.
- How to manually roll back or forward-fix.
- Whether the change is backward compatible with the previous application version.

## Seed Data

- Test data belongs in tests, not production migrations.
- Local-only seed data must be clearly profile-gated or scripted outside production migrations.
- Reference data may be migrated only when it is required for the application to function.

## Review Checklist

- [ ] Migration name follows Flyway conventions.
- [ ] Migration is additive or has a rollback/forward-fix plan.
- [ ] Hibernate `ddl-auto` remains `validate`.
- [ ] Indexes support new query paths.
- [ ] Constraints protect important invariants.
- [ ] No production secrets or environment-specific values are included.
