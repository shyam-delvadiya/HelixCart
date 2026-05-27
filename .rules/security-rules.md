# Security Rules

## Secrets Management

- No hardcoded secrets, credentials, API keys, or tokens anywhere in the codebase.
- Use environment variables for local development.
- Use Kubernetes Secrets (or a secrets manager like Vault) for deployed environments.
- Provide `.env.example` files with placeholder values only.
- `.env` files must be in `.gitignore` — never committed.

## Authentication & Authorization

- JWT validation is mandatory at the API Gateway layer.
- Services behind the gateway must not re-validate JWTs — trust the gateway context headers.
- Service-to-service authentication is mandatory (mTLS or signed tokens in later phases).
- RBAC must be enforced at the application layer, not just the gateway.
- Use Zitadel as the identity provider — do not build custom auth from scratch.

## Zero Trust Principles

- Never trust network location alone.
- Authenticate and authorize every request.
- Apply least privilege access to all service accounts, DB users, and IAM roles.
- Assume breach — design for detection and containment, not just prevention.

## Data Protection

- Sensitive data must never appear in logs: passwords, tokens, PII, card numbers, secrets.
- Encrypt sensitive data at rest where applicable.
- Use HTTPS for all external communication.
- Validate and sanitize all user inputs.

## Dependency Security

- Scan dependencies for known CVEs regularly (Dependabot or equivalent).
- Pin dependency versions — avoid open ranges.
- Review new dependencies before adoption.

## Container Security

- Scan container images with Trivy in CI pipeline.
- Use minimal base images (distroless or Alpine).
- Run containers as non-root users.
- Do not include build tools in production images (multi-stage builds).
- Set resource limits on all containers.

## Code Security

- Run Semgrep static analysis in CI pipeline.
- No SQL string concatenation — use parameterized queries / JPA.
- Validate all external inputs at service boundaries.
- Use CSRF protection for any browser-facing endpoints.

## Incident Response

- Security issues found in production must be documented as incidents.
- Rotate affected credentials immediately upon discovery.
- Document root cause and remediation in `docs/security/`.

## Compliance Checklist (per service)

- [ ] No hardcoded secrets
- [ ] Input validation present
- [ ] Sensitive data excluded from logs
- [ ] Parameterized queries used
- [ ] Container runs as non-root
- [ ] Image scanned with Trivy
- [ ] Dependencies scanned for CVEs
- [ ] Auth enforced on all protected endpoints
