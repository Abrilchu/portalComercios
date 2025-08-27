package com.uxdual.portal.dto;

import io.micronaut.core.annotation.Introspected;

import java.time.LocalDateTime;

@Introspected
public class CustomerDto {
    
    private Long id;
    private String name;
    private String phone;
    private String email;
    private String customerId;
    private LocalDateTime createdAt;
    
    public CustomerDto() {}
    
    public CustomerDto(Long id, String name, String phone, String email, String customerId, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.customerId = customerId;
        this.createdAt = createdAt;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
