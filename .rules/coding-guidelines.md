# Coding Guidelines

## Architecture

- Follow clean architecture principles: separate domain, application, infrastructure, and presentation layers.
- Domain logic must not depend on frameworks (Spring, JPA, etc.).
- Use ports and adapters (hexagonal) where service complexity warrants it.
- Keep services independently deployable from day one.

## Naming Conventions

| Element | Convention | Example |
|---------|-----------|---------|
| Classes | PascalCase | `OrderService`, `ProductRepository` |
| Methods | camelCase | `createOrder()`, `findById()` |
| Constants | UPPER_SNAKE_CASE | `MAX_RETRY_COUNT` |
| Packages | lowercase.dot.separated | `com.helixcart.order.domain` |
| DB tables | snake_case | `order_items`, `product_catalog` |
| Env vars | UPPER_SNAKE_CASE | `DATABASE_URL`, `REDIS_HOST` |

## Code Quality

- Prefer composition over inheritance.
- Avoid large utility classes — split by responsibility.
- Avoid static state and singletons outside framework-managed beans.
- Minimize shared mutable state.
- Avoid premature optimization — profile before optimizing.
- Write self-documenting code; comments explain *why*, not *what*.

## Logging

- Use structured JSON logging in all services.
- Every log entry must include: `timestamp`, `level`, `service`, `correlationId`, `message`.
- Never log sensitive data: passwords, tokens, PII, card numbers.
- Use appropriate log levels: DEBUG for dev, INFO for operations, WARN for recoverable issues, ERROR for failures.

```json
{
  "timestamp": "2025-05-25T10:00:00Z",
  "level": "INFO",
  "service": "order-service",
  "correlationId": "abc-123-xyz",
  "message": "Order created successfully",
  "orderId": "ord-456"
}
```

## Correlation IDs

- Every inbound request must carry or generate a `X-Correlation-ID` header.
- Correlation ID must be propagated to all downstream calls and log entries.
- Use MDC (Mapped Diagnostic Context) for automatic propagation in Spring.

## Error Handling

- Use domain-specific exceptions, not generic `RuntimeException`.
- Map exceptions to appropriate HTTP status codes at the controller layer.
- Return structured error responses:

```json
{
  "status": 400,
  "error": "VALIDATION_FAILED",
  "message": "Product quantity must be greater than zero",
  "correlationId": "abc-123-xyz",
  "timestamp": "2025-05-25T10:00:00Z"
}
```

## Configuration

- All configuration via environment variables or externalized config files.
- No hardcoded values for URLs, credentials, timeouts, or feature flags.
- Use Spring profiles: `local`, `dev`, `staging`, `prod`.
- Provide `.env.example` files — never commit `.env` files.

## API Design

- Follow REST conventions: nouns for resources, HTTP verbs for actions.
- Version APIs from day one: `/api/v1/...`
- Use pagination for list endpoints.
- Document all endpoints with OpenAPI annotations.
- Return consistent response envelopes.

## Dependencies

- Pin dependency versions explicitly.
- Avoid pulling in large frameworks for small problems.
- Review transitive dependencies for known CVEs before adoption.
