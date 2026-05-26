package com.helixcart.common.api;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;

/**
 * Standard API Response Envelope
 *
 * <p>All API responses are wrapped in this envelope to provide consistent structure
 * across all endpoints. This makes client-side handling predictable and enables
 * uniform error handling.
 *
 * <p>Success response:
 * <pre>
 * {
 *   "success": true,
 *   "data": { ... },
 *   "timestamp": "2025-05-25T10:00:00Z"
 * }
 * </pre>
 *
 * <p>Error response:
 * <pre>
 * {
 *   "success": false,
 *   "error": {
 *     "code": "VALIDATION_FAILED",
 *     "message": "Product quantity must be greater than zero"
 *   },
 *   "correlationId": "abc-123-xyz",
 *   "timestamp": "2025-05-25T10:00:00Z"
 * }
 * </pre>
 *
 * @param <T> the type of the response data
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
        boolean success,
        T data,
        ApiError error,
        String correlationId,
        Instant timestamp
) {

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, data, null, null, Instant.now());
    }

    public static <T> ApiResponse<T> error(ApiError error, String correlationId) {
        return new ApiResponse<>(false, null, error, correlationId, Instant.now());
    }

    /**
     * Structured error detail included in error responses.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record ApiError(
            String code,
            String message,
            Object details
    ) {
        public static ApiError of(String code, String message) {
            return new ApiError(code, message, null);
        }

        public static ApiError of(String code, String message, Object details) {
            return new ApiError(code, message, details);
        }
    }
}
