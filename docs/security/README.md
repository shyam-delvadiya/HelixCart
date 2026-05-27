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
| OWASP Dependency Check in CI | ✅ | Warn-only in Phase 1; uses NVD API |
| JWT validation | ⬜ | Phase 2 — Zitadel integration |
| mTLS service-to-service | ⬜ | Phase 5 — service mesh |
| Secrets manager | ⬜ | Phase 5 — Kubernetes Secrets / Vault |

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
