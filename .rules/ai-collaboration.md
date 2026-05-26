# AI Collaboration Rules

## Philosophy

AI is used as an accelerator, not a replacement for engineering judgment.
Every AI-generated artifact must be understood before it is merged.

## Permitted AI Usage

- Generating boilerplate and repetitive code (DTOs, mappers, config classes)
- Scaffolding project structure
- Drafting documentation and ADRs
- Suggesting test cases
- Explaining unfamiliar APIs or patterns
- Reviewing code for obvious issues
- Generating Dockerfile and CI pipeline skeletons

## Restricted AI Usage

| Area | Restriction |
|------|-------------|
| Security implementations | Must be manually validated — JWT, auth filters, RBAC |
| Architecture decisions | AI may suggest, humans must decide and document |
| Infrastructure code | Must be verified before any deployment |
| Database migrations | Must be reviewed for correctness and reversibility |
| Secret management | Never delegate to AI |

## Review Requirements

- All AI-generated code must pass manual review before merge.
- Security-sensitive code requires explicit sign-off in PR description.
- AI-generated infrastructure code must be dry-run tested locally.
- Every major AI-generated implementation must be understood by the author.

## Prompt Versioning

- Significant prompts used to generate core implementations should be saved in `docs/ai-prompts/`.
- This enables reproducibility and audit of AI-assisted decisions.

## Quality Bar

AI-generated code must meet the same standards as human-written code:
- Clean architecture compliance
- Structured logging
- Correlation ID propagation
- Error handling
- Test coverage for business logic

## Red Lines

- Do not merge AI-generated code you cannot explain.
- Do not use AI to bypass security review.
- Do not use AI to make architecture decisions without documentation.
- Do not use AI-generated secrets, credentials, or cryptographic material.
