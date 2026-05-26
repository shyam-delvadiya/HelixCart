# Quality Gates

## Purpose

Quality gates define the minimum bar before code is merged. They convert the project rules into concrete checks.

## Required Before Merge

Every pull request must pass:

| Gate | Command / Check |
|------|------------------|
| Compile and tests | `./mvnw verify` from `services/helixcart-app` |
| Application context | Testcontainers-backed Spring context test |
| Docker build | Build `services/helixcart-app/Dockerfile` |
| Dependency scan | OWASP Dependency Check |
| Static analysis | Semgrep Java/Spring/OWASP rules |
| Container scan | Trivy image scan |
| Documentation | Relevant README, ADR, runbook, or roadmap updates |

## Branch Protection

- `main` is protected.
- Do not commit directly to `main`.
- All changes must be made on a feature, fix, docs, infra, chore, or security branch.
- Changes reach `main` only through a merge request or pull request.
- Local hooks block direct commits on `main`; remote branch protection should also be enabled in the Git hosting platform.

## Local Verification

Before opening a PR:

```bash
cd services/helixcart-app
./mvnw verify
docker build -t helixcart-app:local .
```

If Docker is unavailable, say so in the PR and rely on CI for container validation.

## CI Policy

- Unit and integration tests run on every push and pull request.
- SAST runs on pull requests.
- Dependency scans run on pull requests and mainline pushes.
- Container build and image scanning run on mainline pushes.
- Warn-only security checks in Phase 1 must become enforced gates by Phase 6.

## Merge Readiness Checklist

- [ ] Work is on a non-`main` branch.
- [ ] Tests pass locally or the reason they could not run is documented.
- [ ] CI passes.
- [ ] New behavior has focused tests.
- [ ] Public APIs are reflected in OpenAPI documentation.
- [ ] Operationally meaningful changes update runbooks or docs.
- [ ] Architecture changes include an ADR.
- [ ] No secrets, credentials, or local-only files are included.
