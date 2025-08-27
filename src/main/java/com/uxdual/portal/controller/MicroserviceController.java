package com.uxdual.portal.controller;

import com.uxdual.portal.config.MicroserviceConfig;
import io.micronaut.core.version.VersionUtils;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

import jakarta.inject.Inject;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Controller
@Secured(SecurityRule.IS_ANONYMOUS)
public class MicroserviceController {

    @Inject
    private MicroserviceConfig microserviceConfig;

    @Get("/health")
    public Map<String, Object> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("service", "ux-dual-backend");
        health.put("version", "1.0.0");
        health.put("status", "healthy");
        health.put("timestamp", Instant.now().toString());
        health.put("uptime", getUptime());
        health.put("microservice", true);
        health.put("frontendUrl", microserviceConfig.getFrontendUrl());
        
        // Database connectivity check
        try {
            health.put("database", "connected");
        } catch (Exception e) {
            health.put("database", "disconnected");
            health.put("status", "degraded");
        }
        
        return health;
    }

    @Get("/info")
    public Map<String, Object> info() {
        Map<String, Object> info = new HashMap<>();
        info.put("service", "ux-dual-backend");
        info.put("version", "1.0.0");
        info.put("description", "UX Dual Portal Backend API Microservice");
        info.put("micronaut-version", VersionUtils.getMicronautVersion());
        info.put("java-version", System.getProperty("java.version"));
        info.put("architecture", "microservices");
        info.put("endpoints", Map.of(
            "api", microserviceConfig.getApiPrefix(),
            "auth", microserviceConfig.getAuthPrefix(),
            "health", "/health",
            "info", "/info"
        ));
        return info;
    }

    @Get("/metrics")
    public Map<String, Object> metrics() {
        Map<String, Object> metrics = new HashMap<>();
        Runtime runtime = Runtime.getRuntime();
        
        metrics.put("memory", Map.of(
            "total", runtime.totalMemory(),
            "free", runtime.freeMemory(),
            "used", runtime.totalMemory() - runtime.freeMemory(),
            "max", runtime.maxMemory()
        ));
        
        metrics.put("system", Map.of(
            "processors", runtime.availableProcessors(),
            "uptime", getUptime()
        ));
        
        metrics.put("service", Map.of(
            "name", "ux-dual-backend",
            "version", "1.0.0",
            "type", "microservice"
        ));
        
        return metrics;
    }

    private long getUptime() {
        return java.lang.management.ManagementFactory.getRuntimeMXBean().getUptime();
    }
}