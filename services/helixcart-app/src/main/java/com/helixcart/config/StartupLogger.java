package com.helixcart.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Emits a non-sensitive startup summary for operational visibility.
 */
@Component
public class StartupLogger implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(StartupLogger.class);

    private final Environment environment;

    public StartupLogger(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void run(ApplicationArguments args) {
        String serviceName = environment.getProperty("spring.application.name", "helixcart-app");
        String version = environment.getProperty("info.app.version", "0.1.0-SNAPSHOT");
        String profiles = Arrays.toString(environment.getActiveProfiles());
        String serverPort = environment.getProperty("server.port", "8080");

        log.info(
            "Service started: name={}, version={}, profiles={}, port={}",
            serviceName,
            version,
            profiles,
            serverPort
        );
    }
}
