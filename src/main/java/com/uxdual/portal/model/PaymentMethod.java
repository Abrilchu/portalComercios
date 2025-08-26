package com.uxdual.portal.model;

public enum PaymentMethod {
    OPEN_AMOUNT("open_amount"),
    SET_AMOUNT("set_amount");
    
    private final String value;
    
    PaymentMethod(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
}
