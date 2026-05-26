package com.helixcart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * HelixCart — Cloud-Native Commerce Platform
 *
 * <p>Phase 1: Modular Monolith
 *
 * <p>This application starts as a modular monolith with clearly separated domain modules:
 * - auth: Authentication and authorization
 * - catalog: Product and category management
 * - order: Order lifecycle management
 * - inventory: Stock and reservation management
 *
 * <p>Each module owns its own database schema, application logic, and API surface.
 * Cross-module communication happens through well-defined interfaces, not direct class coupling.
 * This design enables future service extraction without architectural rewrites.
 */
@SpringBootApplication
@EnableCaching
public class HelixCartApplication {

    public static void main(String[] args) {
        SpringApplication.run(HelixCartApplication.class, args);
    }
}
