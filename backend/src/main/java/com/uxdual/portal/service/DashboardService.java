package com.uxdual.portal.service;

import com.uxdual.portal.dto.DashboardStats;
import jakarta.inject.Singleton;
import com.uxdual.portal.repository.PaymentRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Singleton
public class DashboardService {
    
    private final PaymentRepository paymentRepository;
    
    public DashboardService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }
    
    public DashboardStats getDashboardStats(java.util.List<Long> branchIds) {
        // Get basic counts and amounts for today
        Long totalPayments = paymentRepository.getPaymentCountToday();
        BigDecimal totalAmount = paymentRepository.getTotalSalesToday();
        
        // Get yesterday data for comparison
        BigDecimal totalAmountYesterday = paymentRepository.getTotalSalesYesterday();
        Long totalPaymentsYesterday = paymentRepository.getPaymentCountYesterday();
        
        // Handle null values with defaults
        if (totalPayments == null) totalPayments = 0L;
        if (totalAmount == null) totalAmount = BigDecimal.ZERO;
        if (totalAmountYesterday == null) totalAmountYesterday = BigDecimal.ZERO;
        if (totalPaymentsYesterday == null) totalPaymentsYesterday = 0L;
        
        // Calculate percentage change vs yesterday
        Double salesChangePercent = 0.0;
        if (totalAmountYesterday.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal difference = totalAmount.subtract(totalAmountYesterday);
            BigDecimal percentChange = difference.divide(totalAmountYesterday, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
            salesChangePercent = percentChange.doubleValue();
        } else if (totalAmount.compareTo(BigDecimal.ZERO) > 0) {
            salesChangePercent = 100.0; // 100% increase from 0
        }
        
        // For now, using simple defaults for the other values that don't cause enum issues
        return new DashboardStats(
            totalPayments, 0L, 0L, 0L, // counts: total, approved, pending, liquidated
            totalAmount, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, // amounts
            totalAmountYesterday, totalPaymentsYesterday, salesChangePercent
        );
    }
}
