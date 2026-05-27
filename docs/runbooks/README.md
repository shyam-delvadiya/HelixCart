# Runbooks

Operational runbooks for HelixCart services.

Runbooks document how to perform common operational tasks and respond to known failure scenarios.

## Index

| Runbook | Service | Description |
|---------|---------|-------------|
| [Local Startup and Verification](local-startup-and-verification.md) | helixcart-app | Start local dependencies, run the app, and verify health/metrics. |

## Runbook Template

```markdown
# Runbook: <Title>

**Service:** <service-name>
**Last Updated:** YYYY-MM-DD

## Purpose

What operational task or failure scenario does this runbook address?

## Prerequisites

What access, tools, or context is needed?

## Steps

1. Step one
2. Step two
3. ...

## Verification

How do you confirm the task completed successfully?

## Rollback

How do you undo this if something goes wrong?

## Escalation

Who to contact if this runbook does not resolve the issue?
```
