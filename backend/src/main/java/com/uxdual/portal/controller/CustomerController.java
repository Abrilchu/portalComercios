package com.uxdual.portal.controller;

import com.uxdual.portal.dto.CustomerDto;
import com.uxdual.portal.service.CustomerService;
import io.micronaut.data.model.Page;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;

import java.util.Optional;

@Controller("/api/customers")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class CustomerController {
    
    private final CustomerService customerService;
    
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }
    
    @Get
    public Page<CustomerDto> getCustomers(@QueryValue Optional<String> search,
                                         @QueryValue Optional<Integer> page,
                                         @QueryValue Optional<Integer> size,
                                         Authentication authentication) {
        Long commerceId = ((Number) authentication.getAttributes().get("commerceId")).longValue();
        
        return customerService.getCustomers(
            commerceId,
            search.orElse(null),
            page.orElse(0),
            size.orElse(20)
        );
    }
}
