package com.helixcart.common.exception;

/**
 * Thrown when a business rule or domain invariant is violated.
 * Maps to HTTP 422 Unprocessable Entity.
 *
 * <p>Use this for domain-level failures that are the caller's responsibility to handle,
 * such as insufficient inventory, invalid state transitions, or constraint violations.
 *
 * <p>Each exception carries an error code for programmatic handling by clients.
 */
public class BusinessException extends RuntimeException {

    private final String errorCode;

    public BusinessException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
