# Phase 1 Learnings: Foundation

**Date Completed:** 2026-05-27
**Duration:** Initial bootstrap phase

## What Was Built

Phase 1 established the HelixCart foundation: repository standards, architecture docs, a Spring Boot modular monolith skeleton, PostgreSQL and Redis integration, Flyway migrations, structured logging, correlation IDs, Actuator health/metrics, Docker packaging, Docker Compose local infrastructure, and GitHub Actions CI with security scanning.

## Key Technical Learnings

### Foundation Before Features

The project benefits from completing operational basics before domain features. Health checks, logging, database migrations, and repeatable local infrastructure give Phase 2 a stable base instead of making every domain module solve platform concerns again.

### Testcontainers Compatibility Matters

The original Testcontainers 1.x setup failed against the local Docker 29 environment because the Docker API client was too old. Upgrading to Testcontainers 2.x restored the integration test gate and made the foundation compatible with a current Docker runtime.

### CI Cost Needs Early Attention

Dependency vulnerability scanning can dominate pipeline time because the vulnerability database must be downloaded and refreshed. Caching the OWASP Dependency Check database keeps the security control in place while making normal pull request feedback faster.

## Decisions That Proved Right

- Starting as a modular monolith kept deployment and local development simple.
- Flyway-owned schema validation caught database assumptions early.
- Testcontainers provided high confidence that PostgreSQL, Redis, and Spring configuration work together.
- Keeping security scans warn-only in Phase 1 let the pipeline mature without blocking every bootstrap change.

## Decisions That Would Change

- Dependency versions used for Docker/Testcontainers compatibility should be validated against the local runtime during bootstrap, not only during CI.
- Docker Compose warnings should be cleaned immediately because they are easy to fix and reduce noise for future contributors.
- Pin dependency versions conservatively at bootstrap — the OWASP gate failing on a PR merge is avoidable if versions are kept current from the start.

## Security Remediation (2026-05-27)

Before merging Phase 1, the OWASP Dependency Check CI gate (CVSS ≥ 7.0 threshold) flagged vulnerabilities across several transitive dependencies. All were resolved by version bumps — no code changes required:

| Dependency | Old | New | Root cause |
|---|---|---|---|
| Spring Boot parent | 3.3.1 | 3.5.14 | 3.3.x/3.4.x EOL — new CVEs kept landing with no patch. 3.5.14 ships Spring Security 6.5.10 (fixes CVE-2026-22732), Spring Framework 6.2.18, Tomcat 10.1.54 via BOM |
| PostgreSQL JDBC | 42.7.3 | 42.7.11 | CVE-2026-42198 (SCRAM PBKDF2 DoS) — not in Boot BOM, explicit pin required |
| springdoc-openapi | 2.5.0 | 2.8.17 | Ships swagger-ui 5.32.2 with patched DOMPurify; requires Spring Framework 6.2+ (Boot 3.4+) |
| Netty | 4.1.111.Final | 4.1.134.Final | Multiple CVEs — pinned via `netty.version` property (BOM ships 4.1.132) |

**Key learning:** Spring Boot's BOM handles most transitive security fixes automatically when the parent version is bumped. Stay on the latest supported minor line — 3.3.x and 3.4.x are EOL and accumulate CVEs with no patch. Upgrading to 3.5.x resolved Spring Security and Spring Framework CVEs. For CVEs with no upstream fix yet (Tomcat, Netty, log4j-api) and false positives (commons-lang3, Boot version-string misattributions), use an OWASP suppression file with documented justification and review dates. Only dependencies outside the BOM (PostgreSQL JDBC, springdoc) and Netty (BOM lags upstream) need explicit version declarations.

## Surprises

- The first full Docker build was slow because it had to pull base images and download Maven dependencies inside the builder stage.
- The local sandbox could compile code but needed elevated permissions for Maven cache writes and Docker access.

## Next Phase Preparation

Phase 2 should start with the core backend modules while preserving the package boundaries defined in `.rules/module-boundary-rules.md`. The safest first feature path is either Auth if Zitadel configuration is ready, or Catalog if the goal is to ship the first real domain API quickly.
