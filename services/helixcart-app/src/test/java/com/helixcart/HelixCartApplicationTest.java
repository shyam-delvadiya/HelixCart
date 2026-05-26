package com.helixcart;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

/**
 * Application Context Integration Test
 *
 * <p>Verifies that the Spring application context loads successfully with real
 * PostgreSQL and Redis instances (via Testcontainers).
 *
 * <p>This is the most fundamental integration test — if the context fails to load,
 * all other tests will also fail. It validates:
 * - All Spring beans are correctly configured
 * - Database connectivity and Flyway migrations succeed
 * - Redis connectivity succeeds
 * - Security configuration is valid
 */
@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
class HelixCartApplicationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres:16-alpine"))
            .withDatabaseName("helixcart_test")
            .withUsername("helixcart")
            .withPassword("helixcart");

    @Container
    @SuppressWarnings("resource")
    static GenericContainer<?> redis = new GenericContainer<>(
            DockerImageName.parse("redis:7-alpine"))
            .withExposedPorts(6379);

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", () -> redis.getMappedPort(6379));
    }

    @Test
    void contextLoads() {
        // If the application context loads without throwing, this test passes.
        // This validates the entire Spring configuration, DB connectivity,
        // Flyway migrations, and Redis connectivity.
    }
}
