# ADR-001: Start with Modular Monolith

**Date:** 2026-05-27
**Status:** Accepted

---

## Context

HelixCart is a greenfield commerce platform. We need to decide the initial deployment architecture before writing any application code.

The two primary options are:
1. **Microservices from day one** — each domain (auth, catalog, order, inventory) as a separate deployable service
2. **Modular monolith** — all domains in a single deployable unit with clear internal module boundaries

This decision is made at Phase 1, where we have zero production traffic, zero operational data, and zero validated domain boundaries.

---

## Decision

Start with a **modular monolith**.

All domain modules (auth, catalog, order, inventory) will be developed within a single Spring Boot application. Each module will have strict internal boundaries — its own package structure, its own database schema (via Flyway), and no direct cross-module class dependencies.

---

## Rationale

The modular monolith is the correct starting point because:

1. **Domain boundaries are unproven.** We do not yet know exactly where the seams between services should be. Extracting services prematurely locks in boundaries that may be wrong.

2. **Distributed systems complexity is real.** Microservices introduce network latency, partial failures, distributed transactions, service discovery, and operational overhead. None of these problems exist yet.

3. **Refactoring a monolith into services is well-understood.** The reverse — merging poorly designed microservices — is painful and rare.

4. **Speed of iteration.** A monolith is faster to develop, debug, and test in early phases.

5. **The module boundaries we establish now will become service boundaries later.** Good modular design is the prerequisite for good microservices.

---

## Alternatives Considered

| Option | Pros | Cons |
|--------|------|------|
| **Microservices from day one** | Demonstrates microservices architecture early | Premature complexity, wrong boundaries, distributed systems overhead before any value delivered |
| **Modular monolith** (selected) | Fast iteration, proven boundaries, simple ops | Single deployment unit, vertical scaling only, shared process space |
| **Unstructured monolith** | Fastest to start | No clear path to extraction, technical debt accumulates quickly |

---

## Consequences

### Positive
- Fast development in Phase 1 and 2.
- Simple local development (single `docker-compose up`).
- Easy debugging — single process, single log stream.
- Module boundaries established before extraction.
- No distributed systems complexity until it is earned.

### Negative
- Single deployment unit — all modules deploy together.
- Shared JVM process — a memory leak in one module affects all.
- Requires discipline to maintain module boundaries (no cross-module direct calls).

### Risks
- Team discipline required to prevent module boundary violations.
- Mitigation: ArchUnit tests to enforce package boundaries (added in Phase 2).

---

## Review Trigger

This decision should be revisited when:
- A specific module has significantly different scaling requirements than others.
- A module needs to be deployed independently (different release cadence).
- A module needs a different technology stack.
- The team grows large enough that independent deployment becomes a productivity concern.

---

## References

- [Martin Fowler — Modular Monolith](https://martinfowler.com/bliki/MonolithFirst.html)
- [Sam Newman — Monolith to Microservices](https://samnewman.io/books/monolith-to-microservices/)
- Architecture Principles: `/.rules/architecture-principles.md`
