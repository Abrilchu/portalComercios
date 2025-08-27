package com.uxdual.portal.config;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.core.annotation.Introspected;

@ConfigurationProperties("ux-dual.microservice")
@Introspected
public class MicroserviceConfig {
    
    private String frontendUrl = "http://localhost:5000";
    private String apiPrefix = "/api";
    private String authPrefix = "/auth";
    private long healthCheckInterval = 30000;
    
    // Getters and setters
    public String getFrontendUrl() {
        return frontendUrl;
    }
    
    public void setFrontendUrl(String frontendUrl) {
        this.frontendUrl = frontendUrl;
    }
    
    public String getApiPrefix() {
        return apiPrefix;
    }
    
    public void setApiPrefix(String apiPrefix) {
        this.apiPrefix = apiPrefix;
    }
    
    public String getAuthPrefix() {
        return authPrefix;
    }
    
    public void setAuthPrefix(String authPrefix) {
        this.authPrefix = authPrefix;
    }
    
    public long getHealthCheckInterval() {
        return healthCheckInterval;
    }
    
    public void setHealthCheckInterval(long healthCheckInterval) {
        this.healthCheckInterval = healthCheckInterval;
    }
}