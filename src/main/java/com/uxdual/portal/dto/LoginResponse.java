package com.uxdual.portal.dto;

import com.uxdual.portal.model.UserRole;
import io.micronaut.core.annotation.Introspected;

import java.util.List;

@Introspected
public class LoginResponse {
    
    private String token;
    private String username;
    private String email;
    private UserRole role;
    private Long commerceId;
    private List<Long> branchIds;
    private List<Long> cashierIds;
    
    public LoginResponse() {}
    
    public LoginResponse(String token, String username, String email, UserRole role, 
                        Long commerceId, List<Long> branchIds, List<Long> cashierIds) {
        this.token = token;
        this.username = username;
        this.email = email;
        this.role = role;
        this.commerceId = commerceId;
        this.branchIds = branchIds;
        this.cashierIds = cashierIds;
    }
    
    // Getters and Setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }
    
    public Long getCommerceId() { return commerceId; }
    public void setCommerceId(Long commerceId) { this.commerceId = commerceId; }
    
    public List<Long> getBranchIds() { return branchIds; }
    public void setBranchIds(List<Long> branchIds) { this.branchIds = branchIds; }
    
    public List<Long> getCashierIds() { return cashierIds; }
    public void setCashierIds(List<Long> cashierIds) { this.cashierIds = cashierIds; }
}
