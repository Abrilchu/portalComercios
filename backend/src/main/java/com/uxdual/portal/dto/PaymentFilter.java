package com.uxdual.portal.dto;

import com.uxdual.portal.model.PaymentStatus;
import io.micronaut.core.annotation.Introspected;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Introspected
public class PaymentFilter {
    
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private PaymentStatus status;
    private List<Long> branchIds;
    private List<Long> cashierIds;
    private BigDecimal minAmount;
    private BigDecimal maxAmount;
    private String paymentId;
    private String orderId;
    private int page = 0;
    private int size = 20;
    private LocalDateTime sinceCursor;
    
    public PaymentFilter() {}
    
    // Getters and Setters
    public LocalDateTime getFromDate() { return fromDate; }
    public void setFromDate(LocalDateTime fromDate) { this.fromDate = fromDate; }
    
    public LocalDateTime getToDate() { return toDate; }
    public void setToDate(LocalDateTime toDate) { this.toDate = toDate; }
    
    public PaymentStatus getStatus() { return status; }
    public void setStatus(PaymentStatus status) { this.status = status; }
    
    public List<Long> getBranchIds() { return branchIds; }
    public void setBranchIds(List<Long> branchIds) { this.branchIds = branchIds; }
    
    public List<Long> getCashierIds() { return cashierIds; }
    public void setCashierIds(List<Long> cashierIds) { this.cashierIds = cashierIds; }
    
    public BigDecimal getMinAmount() { return minAmount; }
    public void setMinAmount(BigDecimal minAmount) { this.minAmount = minAmount; }
    
    public BigDecimal getMaxAmount() { return maxAmount; }
    public void setMaxAmount(BigDecimal maxAmount) { this.maxAmount = maxAmount; }
    
    public String getPaymentId() { return paymentId; }
    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }
    
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    
    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }
    
    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }
    
    public LocalDateTime getSinceCursor() { return sinceCursor; }
    public void setSinceCursor(LocalDateTime sinceCursor) { this.sinceCursor = sinceCursor; }
}
