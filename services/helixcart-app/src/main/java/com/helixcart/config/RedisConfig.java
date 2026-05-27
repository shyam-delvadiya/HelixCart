package com.helixcart.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Redis Cache Configuration
 *
 * <p>Configures Spring's cache abstraction to use Redis as the backing store.
 * Cache regions are defined with explicit TTLs to prevent stale data accumulation.
 *
 * <p>Design decisions:
 * - Values are serialized as JSON (not Java serialization) for portability and debuggability.
 * - Keys are plain strings for readability in Redis CLI.
 * - Null values are not cached to prevent caching "not found" results that may change.
 * - Each cache region has an explicit TTL — no implicit "cache forever" behavior.
 */
@Configuration
@EnableCaching
public class RedisConfig {

    /**
     * Cache TTL constants — explicit and documented.
     * Adjust these based on observed data change frequency, not guesswork.
     */
    private static final Duration DEFAULT_TTL = Duration.ofMinutes(10);
    private static final Duration PRODUCT_CACHE_TTL = Duration.ofMinutes(30);
    private static final Duration CATEGORY_CACHE_TTL = Duration.ofHours(1);

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(DEFAULT_TTL)
                .disableCachingNullValues()
                .serializeKeysWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(
                                new GenericJackson2JsonRedisSerializer()));

        // Per-cache TTL overrides
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        cacheConfigurations.put("products", defaultConfig.entryTtl(PRODUCT_CACHE_TTL));
        cacheConfigurations.put("categories", defaultConfig.entryTtl(CATEGORY_CACHE_TTL));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(cacheConfigurations)
                .build();
    }
}
