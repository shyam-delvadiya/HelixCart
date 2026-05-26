# Testing Policy

## Test Pyramid

```
        /\
       /  \
      / E2E \        ← Few, slow, high confidence
     /--------\
    / Integration\   ← Moderate, test boundaries
   /--------------\
  /   Unit Tests   \ ← Many, fast, test logic
 /------------------\
```

## Requirements by Layer

### Unit Tests
- **Required for:** All business logic, domain services, use cases, validators, mappers.
- **Not required for:** Simple getters/setters, framework boilerplate, configuration classes.
- **Framework:** JUnit 5 + Mockito.
- **Coverage target:** 80%+ on domain and application layers.

### Integration Tests
- **Required for:** Repository layer (DB queries), cache interactions, external API clients.
- **Framework:** Spring Boot Test + Testcontainers.
- **Scope:** Test real DB/cache behavior with containerized dependencies.

### End-to-End Tests
- **Required for:** Order creation workflow (critical path).
- **Framework:** REST Assured or similar HTTP client.
- **Scope:** Full request-response cycle through the API.

### Contract Tests
- **Preferred for:** Service-to-service communication (when microservices are introduced).
- **Framework:** Spring Cloud Contract or Pact.
- **Phase:** Introduced in Phase 3+.

### Security Tests
- **Required:** Dependency CVE scanning (Dependabot / OWASP Dependency Check).
- **Required:** Container image scanning (Trivy in CI).
- **Required:** Static analysis (Semgrep in CI).
- **Phase:** CI integration from Phase 1.

### Performance Tests
- **Added incrementally** as the system matures.
- **Framework:** Gatling or k6.
- **Phase:** Introduced in Phase 5+.

## Test Naming Convention

```java
// Pattern: methodName_stateUnderTest_expectedBehavior
@Test
void createOrder_withValidRequest_returnsCreatedOrder() { }

@Test
void createOrder_withInsufficientInventory_throwsInsufficientStockException() { }
```

## Testcontainers Usage

Use Testcontainers for integration tests requiring real infrastructure:

```java
@Testcontainers
@SpringBootTest
class OrderRepositoryIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }
}
```

## CI Integration

- Unit tests run on every push.
- Integration tests run on every pull request.
- E2E tests run on merge to main.
- Security scans run on every pull request.

## Test Data

- Use builders or factories for test data creation.
- Never use production data in tests.
- Clean up test data after each test (use `@Transactional` or explicit cleanup).
- Avoid shared mutable test state between tests.

## What NOT to Test

- Framework internals (Spring DI, JPA mapping boilerplate).
- Simple data transfer objects with no logic.
- Configuration classes with no conditional logic.

Focus test effort on code that contains decisions and business rules.
