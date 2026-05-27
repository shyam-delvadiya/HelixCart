# Decision Framework

## Purpose

Every significant technology adoption or architectural change must pass through this framework before implementation. This prevents resume-driven architecture and ensures every decision is grounded in real engineering need.

## The Seven Questions

Before adopting any technology or making any architectural change, answer all seven:

### 1. What problem exists?
Describe the concrete, observed problem. Not a hypothetical future problem — a real one you are facing today or will face in the next phase.

### 2. Why does the current system fail to solve it?
Explain specifically why the existing approach is insufficient. If the current approach could be extended to solve the problem, that is usually preferable.

### 3. What alternatives exist?
List at least two alternatives. Include the option of doing nothing or extending the current approach.

### 4. What are the tradeoffs?
For each alternative, document:
- Operational complexity introduced
- Learning curve
- Maintenance burden
- Vendor lock-in risk
- Community and ecosystem health

### 5. Why was this option selected?
Explain the reasoning. Reference the tradeoffs. Be honest about what you are trading away.

### 6. What operational complexity is introduced?
Be specific:
- New infrastructure to run and monitor?
- New failure modes?
- New on-call burden?
- New deployment complexity?

### 7. What future constraints does this create?
What doors does this decision close? What becomes harder to change later?

---

## Decision Record

Every decision that passes this framework must be recorded as an ADR in `docs/adr/`.

See `documentation-rules.md` for the ADR format.

---

## Examples of Decisions Requiring This Framework

- Adopting Kafka for messaging
- Extracting a service from the monolith
- Switching from REST to gRPC for internal communication
- Adding a new database technology
- Adopting a new observability tool
- Changing the authentication mechanism
- Introducing a service mesh

## Examples of Decisions NOT Requiring This Framework

- Adding a new API endpoint to an existing service
- Updating a dependency version
- Refactoring internal code without changing behavior
- Adding a new test
- Updating documentation

---

## Red Flags

If any of these are true, pause and reconsider:

- "Everyone is using it" — popularity is not a justification.
- "It will scale better" — premature scaling optimization is waste.
- "It looks good on a resume" — this is explicitly against project principles.
- "We might need it later" — YAGNI applies here.
- "It's the modern way" — modernity without purpose is noise.
