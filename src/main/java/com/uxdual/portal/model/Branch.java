package com.uxdual.portal.model;

import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.Relation;

import java.time.LocalDateTime;
import java.util.List;

@MappedEntity("branches")
public class Branch {
    
    @Id
    @GeneratedValue
    private Long id;
    
    private String name;
    private String address;
    private Long commerceId;
    private Long managerId;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    @Relation(value = Relation.Kind.MANY_TO_ONE)
    private User user;
    
    @Relation(value = Relation.Kind.ONE_TO_MANY, mappedBy = "branch")
    private List<Cashier> cashiers;
    
    // Constructors
    public Branch() {}
    
    public Branch(String name, String address, Long commerceId, Long managerId) {
        this.name = name;
        this.address = address;
        this.commerceId = commerceId;
        this.managerId = managerId;
        this.active = true;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public Long getCommerceId() { return commerceId; }
    public void setCommerceId(Long commerceId) { this.commerceId = commerceId; }
    
    public Long getManagerId() { return managerId; }
    public void setManagerId(Long managerId) { this.managerId = managerId; }
    
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public List<Cashier> getCashiers() { return cashiers; }
    public void setCashiers(List<Cashier> cashiers) { this.cashiers = cashiers; }
}
