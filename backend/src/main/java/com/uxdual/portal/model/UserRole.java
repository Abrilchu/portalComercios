package com.uxdual.portal.model;

public enum UserRole {
    CASHIER("cashier"),
    MANAGER("manager"), 
    OWNER("owner");
    
    private final String value;
    
    UserRole(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
}
