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
| Trivy scan in CI | ✅ | Warn-only in Phase 1 |
| Semgrep SAST in CI | ✅ | Warn-only in Phase 1 |
| JWT validation | ⬜ | Phase 2 — Zitadel integration |
| mTLS service-to-service | ⬜ | Phase 5 — service mesh |
| Secrets manager | ⬜ | Phase 5 — Kubernetes Secrets / Vault |

## Security Incidents

| Date | Incident | Severity | Resolution |
|------|----------|----------|-----------|
| *(none)* | | | |
