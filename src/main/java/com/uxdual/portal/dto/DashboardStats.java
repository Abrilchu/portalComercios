package com.uxdual.portal.dto;

import io.micronaut.core.annotation.Introspected;

import java.math.BigDecimal;

@Introspected
public class DashboardStats {
    
    private Long salesToday;
    private Long approvedToday;
    private Long pendingSettlement;
    private BigDecimal settlementMonth;
    
    public DashboardStats() {}
    
    public DashboardStats(Long salesToday, Long approvedToday, Long pendingSettlement, BigDecimal settlementMonth) {
        this.salesToday = salesToday;
        this.approvedToday = approvedToday;
        this.pendingSettlement = pendingSettlement;
        this.settlementMonth = settlementMonth;
    }
    
    // Getters and Setters
    public Long getSalesToday() { return salesToday; }
    public void setSalesToday(Long salesToday) { this.salesToday = salesToday; }
    
    public Long getApprovedToday() { return approvedToday; }
    public void setApprovedToday(Long approvedToday) { this.approvedToday = approvedToday; }
    
    public Long getPendingSettlement() { return pendingSettlement; }
    public void setPendingSettlement(Long pendingSettlement) { this.pendingSettlement = pendingSettlement; }
    
    public BigDecimal getSettlementMonth() { return settlementMonth; }
    public void setSettlementMonth(BigDecimal settlementMonth) { this.settlementMonth = settlementMonth; }
}
