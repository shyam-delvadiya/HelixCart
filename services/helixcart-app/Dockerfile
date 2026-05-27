# HelixCart Application — Production Dockerfile
#
# Multi-stage build:
#   Stage 1 (builder): Compiles the application using Maven
#   Stage 2 (runtime): Minimal JRE image with the compiled JAR only
#
# Security:
#   - Non-root user (appuser) runs the application
#   - No build tools in the runtime image
#   - No secrets baked into the image — all config via environment variables
#
# See .rules/service-standards.md for Dockerfile requirements.

# ===== Stage 1: Build =====
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

# Copy Maven wrapper and POM first to leverage Docker layer caching.
# Dependencies are only re-downloaded when pom.xml changes.
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline -q

# Copy source and build
COPY src ./src
RUN ./mvnw package -DskipTests -q

# ===== Stage 2: Runtime =====
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Create non-root user and group
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Copy only the compiled JAR from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Set ownership
RUN chown appuser:appgroup app.jar

# Switch to non-root user
USER appuser

# Document the port (does not publish — use docker run -p or Kubernetes service)
EXPOSE 8080

# JVM tuning for containers:
#   -XX:+UseContainerSupport: Respect container memory limits (default in JDK 11+, explicit for clarity)
#   -XX:MaxRAMPercentage=75.0: Use 75% of container memory for heap
#   -XX:+ExitOnOutOfMemoryError: Fail fast on OOM rather than degrading silently
ENTRYPOINT ["java", \
    "-XX:+UseContainerSupport", \
    "-XX:MaxRAMPercentage=75.0", \
    "-XX:+ExitOnOutOfMemoryError", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-jar", "app.jar"]
