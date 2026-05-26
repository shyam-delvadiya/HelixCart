# ADR-003: Redis for Caching and Session Storage

**Date:** 2025-05-25
**Status:** Accepted

---

## Context

HelixCart needs a caching layer for:
1. Reducing repeated database reads for frequently accessed data (product catalog, categories).
2. Storing distributed session or token metadata.
3. Supporting rate limiting at the gateway layer (Phase 3).
4. Potential future use: distributed locks, pub/sub, leaderboards.

---

## Decision

Use **Redis 7** as the caching and in-memory data store.

---

## Rationale

1. **Industry standard.** Redis is the de facto caching layer in Spring Boot applications.
2. **Spring Cache abstraction.** Spring's `@Cacheable`, `@CacheEvict`, and `@CachePut` annotations work natively with Redis via `spring-boot-starter-data-redis`.
3. **Versatility.** Redis supports strings, hashes, lists, sets, sorted sets, and streams — useful beyond simple key-value caching.
4. **Performance.** Sub-millisecond read/write latency for in-memory operations.
5. **TTL support.** Native key expiration for cache invalidation.
6. **Rate limiting.** Redis is the standard backing store for rate limiting algorithms (token bucket, sliding window) — needed in Phase 3.

---

## Alternatives Considered

| Option | Pros | Cons |
|--------|------|------|
| **Redis** (selected) | Versatile, fast, Spring-native, industry standard | Requires running Redis (Docker handles this), memory-bound |
| **Caffeine (in-process)** | Zero infrastructure, very fast | Not distributed, lost on restart, no sharing across instances |
| **Memcached** | Simple, fast | Limited data structures, no persistence, less ecosystem support |
| **Hazelcast** | Distributed, Java-native | Heavier, more complex, less common in Spring Boot stacks |

---

## Usage Patterns

### Phase 1-2: Application Cache
```java
@Cacheable(value = "products", key = "#productId")
public ProductDto getProduct(String productId) { ... }

@CacheEvict(value = "products", key = "#productId")
public void updateProduct(String productId, UpdateProductRequest request) { ... }
```

### Phase 3: Rate Limiting (planned)
Redis will back the rate limiter in Spring Cloud Gateway using the `RequestRateLimiter` filter.

### Phase 4+: Distributed Locks (planned)
Redisson or Spring Integration Redis for distributed locking in Saga coordination.

---

## Consequences

### Positive
- Reduced database load for read-heavy catalog operations.
- Fast session/token metadata lookups.
- Foundation for rate limiting and distributed coordination in later phases.

### Negative
- Additional infrastructure component to run and monitor.
- Cache invalidation complexity — stale data risk if eviction strategy is wrong.
- Memory-bound — requires capacity planning for large catalogs.

### Risks
- Cache stampede under high load when cache expires.
- Mitigation: Use probabilistic early expiration or cache warming strategies.
- Redis unavailability should degrade gracefully (fall through to DB), not cause outage.

---

## Configuration

Redis connection configured via environment variables:
```
REDIS_HOST=localhost
REDIS_PORT=6379
REDIS_PASSWORD=  # empty for local dev
```

Spring Cache configuration uses `RedisCacheManager` with default TTL of 10 minutes, configurable per cache region.

---

## References

- [Spring Data Redis Documentation](https://docs.spring.io/spring-data/redis/docs/current/reference/html/)
- [Spring Cache Abstraction](https://docs.spring.io/spring-framework/docs/current/reference/html/integration.html#cache)
- [Redis Documentation](https://redis.io/docs/)
