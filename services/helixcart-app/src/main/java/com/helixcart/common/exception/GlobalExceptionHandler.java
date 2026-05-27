package com.helixcart.common.exception;

import com.helixcart.common.api.ApiResponse;
import com.helixcart.config.CorrelationIdFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Global Exception Handler
 *
 * <p>Centralizes exception-to-HTTP-response mapping for all controllers.
 * This ensures consistent error response structure across the entire API.
 *
 * <p>Design principles:
 * - Domain exceptions map to specific HTTP status codes.
 * - Validation errors return field-level detail to help clients fix requests.
 * - Unexpected exceptions return 500 without leaking internal details.
 * - All errors include the correlation ID for traceability.
 * - All errors are logged with appropriate severity.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles bean validation failures (@Valid, @Validated).
     * Returns 400 with field-level error details.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        Map<String, String> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fieldError -> fieldError.getDefaultMessage() != null
                                ? fieldError.getDefaultMessage()
                                : "Invalid value",
                        // Keep first error per field if multiple violations
                        (existing, replacement) -> existing
                ));

        log.warn("Validation failed for request {} {}: {}",
                request.getMethod(), request.getRequestURI(), fieldErrors);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(
                        ApiResponse.ApiError.of("VALIDATION_FAILED", "Request validation failed", fieldErrors),
                        getCorrelationId()
                ));
    }

    /**
     * Handles domain-specific not-found exceptions.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceNotFoundException(
            ResourceNotFoundException ex,
            HttpServletRequest request) {

        log.warn("Resource not found: {} - {} {}",
                ex.getMessage(), request.getMethod(), request.getRequestURI());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(
                        ApiResponse.ApiError.of("RESOURCE_NOT_FOUND", ex.getMessage()),
                        getCorrelationId()
                ));
    }

    /**
     * Handles domain-specific business rule violations.
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(
            BusinessException ex,
            HttpServletRequest request) {

        log.warn("Business rule violation: {} - {} {}",
                ex.getMessage(), request.getMethod(), request.getRequestURI());

        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(ApiResponse.error(
                        ApiResponse.ApiError.of(ex.getErrorCode(), ex.getMessage()),
                        getCorrelationId()
                ));
    }

    /**
     * Catch-all for unexpected exceptions.
     * Logs the full stack trace but returns a generic message to the client
     * to avoid leaking internal implementation details.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleUnexpectedException(
            Exception ex,
            HttpServletRequest request) {

        log.error("Unexpected error processing request {} {}: {}",
                request.getMethod(), request.getRequestURI(), ex.getMessage(), ex);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(
                        ApiResponse.ApiError.of("INTERNAL_ERROR",
                                "An unexpected error occurred. Please try again or contact support."),
                        getCorrelationId()
                ));
    }

    private String getCorrelationId() {
        return MDC.get(CorrelationIdFilter.MDC_CORRELATION_ID_KEY);
    }
}
