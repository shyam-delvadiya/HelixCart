package com.helixcart.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI / Swagger Configuration
 *
 * <p>Configures the API documentation available at:
 * - Swagger UI: /swagger-ui.html
 * - OpenAPI JSON: /api-docs
 *
 * <p>All API endpoints must be documented with OpenAPI annotations.
 * This is a non-negotiable service standard (see .rules/service-standards.md).
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI helixCartOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("HelixCart API")
                        .description("""
                                HelixCart — Cloud-Native Commerce Platform
                                
                                Phase 1: Modular Monolith
                                
                                This API provides access to the HelixCart commerce platform including:
                                - Product catalog management
                                - Order lifecycle management
                                - Inventory management
                                - Authentication and authorization
                                """)
                        .version("v0.1.0")
                        .contact(new Contact()
                                .name("HelixCart Engineering")
                                .url("https://github.com/shyam-delvadiya/HelixCart"))
                        .license(new License()
                                .name("MIT")
                                .url("https://opensource.org/licenses/MIT")))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("JWT token issued by Zitadel identity provider")));
    }
}
