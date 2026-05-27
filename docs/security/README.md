# Security Documentation

Security posture, decisions, and incident records for HelixCart.

## Security Principles

See `.rules/security-rules.md` for the full security policy.

## Current Security Posture (Phase 1)

| Control | Status | Notes |
|---------|--------|-------|
| No hardcoded secrets | ✅ | `.env.example` pattern enforced |
| `.gitignore` covers secrets | ✅ | `.env`, `*.pem`, `*.key` excluded |
| Container runs as non-root | ✅ | `appuser` in Dockerfile |
| Multi-stage Docker build | ✅ | No build tools in runtime image |
| Trivy scan in CI | ✅ | Warn-only in Phase 1; pinned to `0.35.0` (safe post-compromise) |
| Semgrep SAST in CI | ✅ | Warn-only in Phase 1 |
| OWASP Dependency Check in CI | ✅ | Warn-only in Phase 1 (continue-on-error); enforces CVSS ≥ 7.0 gate in Phase 6 |
| JWT validation | ⬜ | Phase 2 — Zitadel integration |
| mTLS service-to-service | ⬜ | Phase 5 — service mesh |
| Secrets manager | ⬜ | Phase 5 — Kubernetes Secrets / Vault |

## Dependency Version Policy

All third-party dependencies are kept at the latest patched release. The OWASP Dependency Check gate in CI enforces this — builds fail on any CVE with CVSS ≥ 7.0.

### Key dependency versions (as of 2026-05-27)

| Dependency | Version | Managed by |
|---|---|---|
| Spring Boot | 3.5.14 | `pom.xml` parent |
| Spring Security | via Boot BOM | auto via Spring Boot parent |
| Spring Framework | via Boot BOM | auto via Spring Boot parent |
| Apache Tomcat (embedded) | via Boot BOM | auto via Spring Boot parent |
| commons-lang3 | via Boot BOM | auto via Spring Boot parent |
| Netty | 4.1.134.Final | explicit `netty.version` property (transitive via Lettuce/Redis) |
| PostgreSQL JDBC | 42.7.11 | explicit in `pom.xml` (not in Boot BOM) |
| springdoc-openapi | 2.8.17 | explicit in `pom.xml` (not in Boot BOM) |

> Spring Boot's BOM manages most transitive versions automatically when the parent is updated. Only dependencies outside the BOM (PostgreSQL JDBC, springdoc) and Netty (pinned ahead of BOM lag) require explicit version declarations.

### OWASP Suppression File

CVEs with no upstream fix or confirmed false positives are tracked in `services/helixcart-app/owasp-suppressions.xml`. Each suppression includes the reason and date added. Suppressions must be reviewed and removed when upstream patches become available.

### Trivy Action Supply Chain Note

`aquasecurity/trivy-action` was compromised in March 2026 (CVE-2026-33634) — 76 of 77 version tags were poisoned with credential-stealing malware. The CI is pinned to `@0.35.0`, the only tag confirmed safe. Do not use `@master` or any floating tag. Review when Aqua Security publishes a verified clean release.

## CI Secrets

The following secrets must be configured in GitHub repository settings under **Settings → Secrets and variables → Actions**:

| Secret | Required | Purpose | How to obtain |
|--------|----------|---------|---------------|
| `NVD_API_KEY` | Optional (recommended) | Speeds up OWASP Dependency Check by authenticating against the NVD API. Without it the scan still runs but is heavily rate-limited and slow. | Free — request at [nvd.nist.gov/developers/request-an-api-key](https://nvd.nist.gov/developers/request-an-api-key). Delivered by email within minutes. |
| `SEMGREP_APP_TOKEN` | Optional | Enables Semgrep Cloud dashboard and PR annotations. Without it Semgrep runs in OSS mode with no dashboard. | Create a free account at [semgrep.dev](https://semgrep.dev) and generate a token under Settings. |

> The CI pipeline degrades gracefully when these secrets are absent — scans still run, just without the enhanced features.

## Security Incidents

| Date | Incident | Severity | Resolution |
|------|----------|----------|-----------|
| *(none)* | | | |
