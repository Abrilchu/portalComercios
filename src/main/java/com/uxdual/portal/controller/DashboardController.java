package com.uxdual.portal.controller;

import com.uxdual.portal.dto.DashboardStats;
import com.uxdual.portal.service.DashboardService;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;

import java.util.List;

@Controller("/api/dashboard")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class DashboardController {
    
    private final DashboardService dashboardService;
    
    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }
    
    @Get("/stats")
    public DashboardStats getDashboardStats(Authentication authentication) {
        List<Long> branchIds = (List<Long>) authentication.getAttributes().get("branchIds");
        return dashboardService.getDashboardStats(branchIds);
    }
}
