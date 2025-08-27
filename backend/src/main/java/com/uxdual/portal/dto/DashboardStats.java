package com.uxdual.portal.dto;

import io.micronaut.core.annotation.Introspected;

import java.math.BigDecimal;

@Introspected
public class DashboardStats {
    
    // Counts
    private Long totalPayments;
    private Long approvedPayments;
    private Long pendingPayments;
    private Long liquidatedPayments;
    
    // Amounts
    private BigDecimal totalAmount;
    private BigDecimal approvedAmount;
    private BigDecimal pendingAmount;
    private BigDecimal liquidatedAmount;
    
    // Yesterday comparison
    private BigDecimal totalAmountYesterday;
    private Long totalPaymentsYesterday;
    private Double salesChangePercent;
    
    public DashboardStats() {}
    
    public DashboardStats(Long totalPayments, Long approvedPayments, Long pendingPayments, Long liquidatedPayments,
                         BigDecimal totalAmount, BigDecimal approvedAmount, BigDecimal pendingAmount, BigDecimal liquidatedAmount,
                         BigDecimal totalAmountYesterday, Long totalPaymentsYesterday, Double salesChangePercent) {
        this.totalPayments = totalPayments;
        this.approvedPayments = approvedPayments;
        this.pendingPayments = pendingPayments;
        this.liquidatedPayments = liquidatedPayments;
        this.totalAmount = totalAmount;
        this.approvedAmount = approvedAmount;
        this.pendingAmount = pendingAmount;
        this.liquidatedAmount = liquidatedAmount;
        this.totalAmountYesterday = totalAmountYesterday;
        this.totalPaymentsYesterday = totalPaymentsYesterday;
        this.salesChangePercent = salesChangePercent;
    }
    
    // Getters and Setters
    public Long getTotalPayments() { return totalPayments; }
    public void setTotalPayments(Long totalPayments) { this.totalPayments = totalPayments; }

    public Long getApprovedPayments() { return approvedPayments; }
    public void setApprovedPayments(Long approvedPayments) { this.approvedPayments = approvedPayments; }

    public Long getPendingPayments() { return pendingPayments; }
    public void setPendingPayments(Long pendingPayments) { this.pendingPayments = pendingPayments; }

    public Long getLiquidatedPayments() { return liquidatedPayments; }
    public void setLiquidatedPayments(Long liquidatedPayments) { this.liquidatedPayments = liquidatedPayments; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public BigDecimal getApprovedAmount() { return approvedAmount; }
    public void setApprovedAmount(BigDecimal approvedAmount) { this.approvedAmount = approvedAmount; }

    public BigDecimal getPendingAmount() { return pendingAmount; }
    public void setPendingAmount(BigDecimal pendingAmount) { this.pendingAmount = pendingAmount; }

    public BigDecimal getLiquidatedAmount() { return liquidatedAmount; }
    public void setLiquidatedAmount(BigDecimal liquidatedAmount) { this.liquidatedAmount = liquidatedAmount; }

    public BigDecimal getTotalAmountYesterday() { return totalAmountYesterday; }
    public void setTotalAmountYesterday(BigDecimal totalAmountYesterday) { this.totalAmountYesterday = totalAmountYesterday; }

    public Long getTotalPaymentsYesterday() { return totalPaymentsYesterday; }
    public void setTotalPaymentsYesterday(Long totalPaymentsYesterday) { this.totalPaymentsYesterday = totalPaymentsYesterday; }

    public Double getSalesChangePercent() { return salesChangePercent; }
    public void setSalesChangePercent(Double salesChangePercent) { this.salesChangePercent = salesChangePercent; }
}
