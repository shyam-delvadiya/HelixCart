# Commit Conventions

## Format

```
<type>(<scope>): <description>

[optional body]

[optional footer]
```

## Types

| Type | When to Use |
|------|-------------|
| `feat` | New feature or capability |
| `fix` | Bug fix |
| `refactor` | Code change that neither fixes a bug nor adds a feature |
| `docs` | Documentation only changes |
| `test` | Adding or updating tests |
| `ci` | CI/CD pipeline changes |
| `infra` | Infrastructure or IaC changes |
| `chore` | Maintenance tasks, dependency updates |
| `perf` | Performance improvements |
| `security` | Security fixes or hardening |
| `revert` | Reverting a previous commit |

## Scopes

Use the service or area name as scope:

- `auth`, `catalog`, `order`, `inventory`
- `gateway`
- `infra`, `k8s`, `helm`, `terraform`
- `observability`
- `ci`, `cd`
- `docs`, `adr`
- `rules`

## Examples

```
feat(auth): add JWT token validation filter
fix(order): resolve duplicate inventory reservation on retry
refactor(gateway): simplify routing filter chain
docs(adr): document Kafka adoption rationale
ci(actions): add Trivy container image scan
infra(k8s): add liveness and readiness probes to catalog deployment
test(order): add integration test for order creation flow
security(auth): enforce HTTPS-only cookie flags
chore(deps): bump Spring Boot to 3.3.1
```

## Rules

- Subject line must be 72 characters or fewer.
- Use imperative mood: "add feature" not "added feature".
- Do not end subject line with a period.
- Reference issue numbers in footer when applicable: `Closes #42`.
- Breaking changes must include `BREAKING CHANGE:` in footer.

## Branch Naming

```
<type>/<scope>-<short-description>
```

Examples:
- `feat/auth-jwt-validation`
- `fix/order-duplicate-reservation`
- `infra/k8s-health-probes`
- `docs/adr-kafka-adoption`

## Main Branch Protection

- Do not commit directly to `main`.
- All changes must be developed on a branch and merged through a pull request or merge request.
- Local Git hooks block direct commits on `main`.
- Remote branch protection must be enabled in the Git hosting platform before collaborative development starts.
- Emergency fixes still use a branch and an expedited review.
