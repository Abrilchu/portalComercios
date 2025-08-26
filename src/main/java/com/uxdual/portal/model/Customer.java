package com.uxdual.portal.model;

import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.Relation;

import java.time.LocalDateTime;
import java.util.List;

@MappedEntity("customers")
public class Customer {
    
    @Id
    @GeneratedValue
    private Long id;
    
    private String name;
    private String phone;
    private String email;
    private String customerId;
    private Long commerceId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    @Relation(value = Relation.Kind.ONE_TO_MANY, mappedBy = "customer")
    private List<Payment> payments;
    
    // Constructors
    public Customer() {}
    
    public Customer(String name, String phone, String email, String customerId, Long commerceId) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.customerId = customerId;
        this.commerceId = commerceId;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
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
    
    public Long getCommerceId() { return commerceId; }
    public void setCommerceId(Long commerceId) { this.commerceId = commerceId; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public List<Payment> getPayments() { return payments; }
    public void setPayments(List<Payment> payments) { this.payments = payments; }
}
