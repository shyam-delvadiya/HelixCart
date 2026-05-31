package com.helixcart.catalog.api;

import com.helixcart.catalog.domain.ProductStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * HTTP request body for changing only the product lifecycle status.
 */
@Schema(description = "Product status update request")
public record ProductStatusRequest(
        @NotNull(message = "Status is required")
        ProductStatus status
) {
}
