package com.helixcart.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * Correlation ID Filter
 *
 * <p>Ensures every inbound HTTP request carries a correlation ID that is:
 * 1. Read from the incoming {@code X-Correlation-ID} header if present.
 * 2. Generated as a new UUID if not present.
 * 3. Stored in SLF4J MDC so all log entries for this request include it automatically.
 * 4. Written to the outbound response header so callers can trace their requests.
 *
 * <p>This is a foundational observability concern — every log line produced during
 * request processing will carry the correlation ID without any additional code.
 */
@Component
@Order(1)
public class CorrelationIdFilter extends OncePerRequestFilter {

    public static final String CORRELATION_ID_HEADER = "X-Correlation-ID";
    public static final String MDC_CORRELATION_ID_KEY = "correlationId";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String correlationId = request.getHeader(CORRELATION_ID_HEADER);

        if (correlationId == null || correlationId.isBlank()) {
            correlationId = UUID.randomUUID().toString();
        }

        // Store in MDC — automatically included in all log entries for this thread
        MDC.put(MDC_CORRELATION_ID_KEY, correlationId);

        // Echo back in response so callers can trace their request
        response.setHeader(CORRELATION_ID_HEADER, correlationId);

        try {
            filterChain.doFilter(request, response);
        } finally {
            // Always clean up MDC to prevent leakage in thread pool environments
            MDC.remove(MDC_CORRELATION_ID_KEY);
        }
    }
}
