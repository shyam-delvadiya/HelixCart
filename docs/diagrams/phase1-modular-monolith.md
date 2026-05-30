# Diagram: Base Modular Monolith Architecture

```mermaid
graph TB
    Client["Client (Browser / API Consumer)"]

    subgraph Docker["Docker / Local / Kubernetes"]
        subgraph App["helixcart-app (Spring Boot)"]
            direction TB
            API["REST API Layer\n(Controllers, DTOs)"]
            
            subgraph Modules["Domain Modules"]
                Auth["auth\nmodule"]
                Catalog["catalog\nmodule"]
                Order["order\nmodule"]
                Inventory["inventory\nmodule"]
            end
            
            Shared["Shared Infrastructure\n(Logging, Correlation IDs,\nError Handling, Security)"]
            
            API --> Modules
            Modules --> Shared
        end
        
        PG[("PostgreSQL 16\nPrimary Database")]
        Redis[("Redis 7\nCache & Session")]
    end

    Client -->|"HTTP/REST\nX-Correlation-ID"| API
    App -->|"JDBC / JPA\nFlyway Migrations"| PG
    App -->|"Spring Cache\nRedis Client"| Redis
    
    subgraph Observability["Observability"]
        Health["/actuator/health\nLiveness + Readiness"]
        Metrics["/actuator/prometheus\nPrometheus Metrics"]
        Logs["Structured JSON Logs\n(stdout)"]
    end
    
    App --> Observability
```

## Notes

- All four domain modules are in a single deployable JAR.
- Phase 2 is implementing these module internals incrementally, starting with Catalog products/categories and Inventory stock reservations.
- Each module has its own package hierarchy and database schema (via Flyway).
- Modules communicate through Java interfaces — no direct cross-module class coupling.
- This design enables future service extraction without architectural rewrites.

## Evolution

In Phase 3, the API Gateway will be introduced in front of this application.
In Phase 5, individual modules will be extracted into separate services when justified.
