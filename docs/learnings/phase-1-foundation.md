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

## Surprises

- The first full Docker build was slow because it had to pull base images and download Maven dependencies inside the builder stage.
- The local sandbox could compile code but needed elevated permissions for Maven cache writes and Docker access.

## Next Phase Preparation

Phase 2 should start with the core backend modules while preserving the package boundaries defined in `.rules/module-boundary-rules.md`. The safest first feature path is either Auth if Zitadel configuration is ready, or Catalog if the goal is to ship the first real domain API quickly.
