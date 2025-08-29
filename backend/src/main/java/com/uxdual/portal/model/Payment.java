package com.uxdual.portal.model;

import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.Relation;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@MappedEntity("payments")
public class Payment {
    
    @Id
    @GeneratedValue
    private Long id;
    
    private String paymentId;
    private String orderId;
    private Long customerId;
    private Long branchId;
    private Long cashierId;
    private BigDecimal amount;
    private PaymentMethod method;
    private PaymentStatus status;
    private LocalDateTime settlementEta;
    private String statusTimeline;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastSyncCursor;
    
    // Simplified model - no complex relations for UXDUAL schema compatibility
    // Using direct fields instead of relations to avoid JOIN queries
    private String customerName;
    private String branchName;
    private String cashierName;
    
    // Constructors
    public Payment() {}
    
    public Payment(String paymentId, String orderId, Long customerId, Long branchId, 
                   Long cashierId, BigDecimal amount, PaymentMethod method, PaymentStatus status) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.customerId = customerId;
        this.branchId = branchId;
        this.cashierId = cashierId;
        this.amount = amount;
        this.method = method;
        this.status = status;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.lastSyncCursor = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getPaymentId() { return paymentId; }
    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }
    
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    
    public Long getBranchId() { return branchId; }
    public void setBranchId(Long branchId) { this.branchId = branchId; }
    
    public Long getCashierId() { return cashierId; }
    public void setCashierId(Long cashierId) { this.cashierId = cashierId; }
    
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    
    public PaymentMethod getMethod() { return method; }
    public void setMethod(PaymentMethod method) { this.method = method; }
    
    public PaymentStatus getStatus() { return status; }
    public void setStatus(PaymentStatus status) { this.status = status; }
    
    public LocalDateTime getSettlementEta() { return settlementEta; }
    public void setSettlementEta(LocalDateTime settlementEta) { this.settlementEta = settlementEta; }
    
    public String getStatusTimeline() { return statusTimeline; }
    public void setStatusTimeline(String statusTimeline) { this.statusTimeline = statusTimeline; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public LocalDateTime getLastSyncCursor() { return lastSyncCursor; }
    public void setLastSyncCursor(LocalDateTime lastSyncCursor) { this.lastSyncCursor = lastSyncCursor; }
    
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    
    public String getBranchName() { return branchName; }
    public void setBranchName(String branchName) { this.branchName = branchName; }
    
    public String getCashierName() { return cashierName; }
    public void setCashierName(String cashierName) { this.cashierName = cashierName; }
}
