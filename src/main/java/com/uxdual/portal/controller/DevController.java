package com.uxdual.portal.controller;

import com.uxdual.portal.dto.DashboardStats;
import com.uxdual.portal.dto.PaymentDto;
import com.uxdual.portal.dto.CustomerDto;
import com.uxdual.portal.dto.PaymentFilter;
import com.uxdual.portal.service.DashboardService;
import com.uxdual.portal.service.PaymentService;
import com.uxdual.portal.service.CustomerService;
import io.micronaut.data.model.Page;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

import java.util.Arrays;
import java.util.List;

/**
 * Development controller for testing without authentication
 * Only for development purposes - should be removed in production
 */
@Controller("/api/dev")
@Secured(SecurityRule.IS_ANONYMOUS)
public class DevController {
    
    private final DashboardService dashboardService;
    private final PaymentService paymentService;
    private final CustomerService customerService;
    
    public DevController(DashboardService dashboardService, 
                        PaymentService paymentService,
                        CustomerService customerService) {
        this.dashboardService = dashboardService;
        this.paymentService = paymentService;
        this.customerService = customerService;
    }
    
    @Get("/dashboard/stats")
    public DashboardStats getDashboardStats() {
        // Use all branches for development
        List<Long> allBranchIds = Arrays.asList(1L, 2L);
        return dashboardService.getDashboardStats(allBranchIds);
    }
    
    @Get("/payments")
    public Page<PaymentDto> getPayments() {
        // Use all branches and cashiers for development
        List<Long> allBranchIds = Arrays.asList(1L, 2L);
        List<Long> allCashierIds = Arrays.asList(1L, 2L, 3L);
        
        // Create empty filter for development
        PaymentFilter filter = new PaymentFilter();
        
        return paymentService.getPayments(filter, allBranchIds, allCashierIds);
    }
    
    @Get("/customers")
    public Page<CustomerDto> getCustomers() {
        // Use all customers for development
        Long commerceId = 1L;
        return customerService.getCustomers(commerceId, null, 0, 50);
    }
}