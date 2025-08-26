package com.uxdual.portal.controller;

import com.uxdual.portal.dto.DashboardStats;
import com.uxdual.portal.service.SimpleDashboardService;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

@Controller("/api/dashboard")
@Secured(SecurityRule.IS_ANONYMOUS)
public class DashboardController {
    
    private final SimpleDashboardService dashboardService;
    
    public DashboardController(SimpleDashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }
    
    @Get("/stats")
    public DashboardStats getDashboardStats() {
        return dashboardService.getDashboardStats();
    }
}
