package com.uxdual.portal.controller;

import com.uxdual.portal.dto.DashboardStats;
import com.uxdual.portal.dto.PaymentDto;
import com.uxdual.portal.dto.CustomerDto;
import com.uxdual.portal.dto.PaymentFilter;
import com.uxdual.portal.service.DashboardService;
import com.uxdual.portal.service.PaymentService;
import com.uxdual.portal.service.CustomerService;
import com.uxdual.portal.service.BranchService;
import com.uxdual.portal.service.CashierService;
import com.uxdual.portal.dto.BranchDto;
import com.uxdual.portal.dto.CashierDto;
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
    private final BranchService branchService;
    private final CashierService cashierService;
    
    public DevController(DashboardService dashboardService, 
                        PaymentService paymentService,
                        CustomerService customerService,
                        BranchService branchService,
                        CashierService cashierService) {
        this.dashboardService = dashboardService;
        this.paymentService = paymentService;
        this.customerService = customerService;
        this.branchService = branchService;
        this.cashierService = cashierService;
    }
    
    @Get("/dashboard/stats")
    public DashboardStats getDashboardStats() {
        // Use all branches for development
        List<Long> allBranchIds = Arrays.asList(1L, 2L);
        return dashboardService.getDashboardStats(allBranchIds);
    }
    
    @Get("/payments")
    public Page<PaymentDto> getPayments() {
        return paymentService.getAllPayments();
    }
    
    @Get("/customers")
    public Page<CustomerDto> getCustomers() {
        // Use all customers for development
        Long commerceId = 1L;
        return customerService.getCustomers(commerceId, null, 0, 50);
    }
    
    @Get("/branches")
    public List<BranchDto> getBranches() {
        return branchService.getAllBranches();
    }
    
    @Get("/cashiers")
    public List<CashierDto> getCashiers() {
        return cashierService.getAllCashiers();
    }
}