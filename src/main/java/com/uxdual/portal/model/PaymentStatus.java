package com.uxdual.portal.model;

public enum PaymentStatus {
    APROBADA("APROBADA"),
    RECHAZADA("RECHAZADA"),
    PEND_LIQ("PEND_LIQ"),
    LIQUIDADA("LIQUIDADA");
    
    private final String value;
    
    PaymentStatus(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
}
