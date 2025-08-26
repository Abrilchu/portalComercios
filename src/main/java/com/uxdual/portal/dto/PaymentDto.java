package com.uxdual.portal.dto;

import com.uxdual.portal.model.PaymentMethod;
import com.uxdual.portal.model.PaymentStatus;
import io.micronaut.core.annotation.Introspected;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Introspected
public class PaymentDto {
    
    private Long id;
    private String paymentId;
    private String orderId;
    private String customerName;
    private String branchName;
    private String cashierName;
    private BigDecimal amount;
    private PaymentMethod method;
    private PaymentStatus status;
    private LocalDateTime settlementEta;
    private String statusTimeline;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public PaymentDto() {}
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getPaymentId() { return paymentId; }
    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }
    
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    
    public String getBranchName() { return branchName; }
    public void setBranchName(String branchName) { this.branchName = branchName; }
    
    public String getCashierName() { return cashierName; }
    public void setCashierName(String cashierName) { this.cashierName = cashierName; }
    
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
}
