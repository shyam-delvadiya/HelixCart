package com.helixcart.catalog.domain;

/**
 * Lifecycle state for a catalog product.
 *
 * <p>The status is intentionally small in Phase 2. It supports the core product
 * publishing workflow without introducing pricing, merchandising, or approval
 * states that are not implemented yet.
 */
public enum ProductStatus {
    ACTIVE,
    INACTIVE,
    DISCONTINUED
}
