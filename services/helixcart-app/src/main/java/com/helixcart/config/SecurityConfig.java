package com.helixcart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security Configuration — Phase 1 Skeleton
 *
 * <p>Phase 1 establishes the security structure without full JWT integration.
 * JWT validation via Zitadel will be wired in Phase 2 (auth module).
 *
 * <p>Current posture:
 * - Actuator health and metrics endpoints are public (required for Kubernetes probes and Prometheus).
 * - OpenAPI documentation is public (developer tooling).
 * - All other endpoints require authentication.
 * - Stateless session — no server-side session state (JWT-based auth is stateless by design).
 * - CSRF disabled — appropriate for stateless REST APIs consumed by non-browser clients.
 *
 * <p>Phase 2 changes:
 * - Add OAuth2 resource server configuration pointing to Zitadel JWKS endpoint.
 * - Add JWT claims extraction for RBAC.
 * - Add method-level security annotations on service layer.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Stateless — no HTTP session, auth state carried in JWT
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // CSRF not needed for stateless REST APIs
            .csrf(AbstractHttpConfigurer::disable)

            .authorizeHttpRequests(auth -> auth
                // Infrastructure endpoints — must be public for Kubernetes probes and Prometheus
                .requestMatchers(
                    "/actuator/health",
                    "/actuator/health/**",
                    "/actuator/prometheus"
                ).permitAll()

                // API documentation — public for developer access
                .requestMatchers(
                    "/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html"
                ).permitAll()

                // All other requests require authentication
                // Phase 2: replace with JWT-based authentication
                .anyRequest().authenticated()
            );

        return http.build();
    }
}
