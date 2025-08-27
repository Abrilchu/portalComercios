package com.uxdual.portal.service;

import com.uxdual.portal.dto.CustomerDto;
import com.uxdual.portal.model.Customer;
import com.uxdual.portal.repository.CustomerRepository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Singleton;

@Singleton
public class CustomerService {
    
    private final CustomerRepository customerRepository;
    
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
    
    public Page<CustomerDto> getCustomers(Long commerceId, String search, int page, int size) {
        Pageable pageable = Pageable.from(page, size);
        
        Page<Customer> customers;
        if (search != null && !search.trim().isEmpty()) {
            customers = customerRepository.searchByCommerceId(commerceId, search.trim(), pageable);
        } else {
            customers = customerRepository.findByCommerceId(commerceId, pageable);
        }
        
        return customers.map(this::convertToDto);
    }
    
    private CustomerDto convertToDto(Customer customer) {
        return new CustomerDto(
            customer.getId(),
            customer.getName(),
            customer.getPhone(),
            customer.getEmail(),
            customer.getCustomerId(),
            customer.getCreatedAt()
        );
    }
}
