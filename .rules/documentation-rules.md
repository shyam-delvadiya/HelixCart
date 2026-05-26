# Documentation Rules

## Mandatory Documentation

| Event | Required Documentation |
|-------|----------------------|
| New service created | Service README |
| Architecture change | Updated diagram + ADR |
| New technology adopted | Tradeoff analysis doc |
| Production-like incident | Postmortem |
| Phase milestone completed | Engineering learnings doc |
| New API endpoint | OpenAPI spec update |
| Infrastructure change | Runbook update if operational impact |

## ADR (Architecture Decision Record) Format

All ADRs live in `docs/adr/` and follow this format:

```markdown
# ADR-NNN: Title

**Date:** YYYY-MM-DD
**Status:** Proposed | Accepted | Deprecated | Superseded by ADR-NNN

## Context

What is the situation that requires a decision?

## Decision

What was decided?

## Rationale

Why was this decision made over alternatives?

## Alternatives Considered

| Option | Pros | Cons |
|--------|------|------|
| Option A | ... | ... |
| Option B | ... | ... |

## Consequences

### Positive
- ...

### Negative
- ...

### Risks
- ...

## References
- Links to relevant docs, issues, or prior art
```

## Service README Format

Every service must have a `README.md` containing:

1. **Purpose** — what problem this service solves
2. **Domain Responsibilities** — what it owns
3. **API Overview** — key endpoints (link to OpenAPI spec)
4. **Dependencies** — databases, caches, external services
5. **Configuration** — environment variables reference
6. **Running Locally** — step-by-step local setup
7. **Testing** — how to run tests
8. **Architecture Notes** — key design decisions

## Tradeoff Analysis Format

Lives in `docs/tradeoffs/`:

```markdown
# Tradeoff: <Technology or Decision>

**Date:** YYYY-MM-DD
**Phase:** Phase N

## Problem Being Solved

## Options Evaluated

## Selected Option

## Why This Option

## Operational Cost

## Scaling Implications

## Maintenance Burden

## Future Constraints

## Conclusion
```

## Postmortem Format

Lives in `docs/postmortems/`:

```markdown
# Postmortem: <Incident Title>

**Date:** YYYY-MM-DD
**Severity:** P1 / P2 / P3
**Duration:** X hours Y minutes

## Summary

## Timeline

## Root Cause

## Contributing Factors

## Impact

## Resolution

## Action Items

| Action | Owner | Due Date |
|--------|-------|----------|

## Learnings
```

## Quality Standards

- Documentation must be written in clear, professional English.
- Diagrams must be kept up to date with code changes.
- Outdated documentation is worse than no documentation — delete or update.
- Use Mermaid or PlantUML for diagrams where possible (version-controllable).
