package com.uxdual.portal.service;

import com.uxdual.portal.dto.DashboardStats;
import jakarta.inject.Singleton;
import com.uxdual.portal.repository.PaymentRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Singleton
public class SimpleDashboardService {
    
    private final PaymentRepository paymentRepository;
    
    public SimpleDashboardService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }
    
    public DashboardStats getDashboardStats() {
        try {
            // Get basic counts and amounts for today
            Long totalPayments = paymentRepository.getPaymentCountToday();
            BigDecimal totalAmount = paymentRepository.getTotalSalesToday();
            
            System.out.println("DEBUG: Total payments today: " + totalPayments);
            System.out.println("DEBUG: Total amount today: " + totalAmount);
            
            // Get counts by status for today
            Long approvedPayments = null;
            Long pendingPayments = null;
            Long liquidatedPayments = null;
            
            try {
                approvedPayments = paymentRepository.getCountByStatusToday("APROBADA");
                System.out.println("DEBUG: Approved payments: " + approvedPayments);
            } catch (Exception e) {
                System.out.println("ERROR getting approved payments: " + e.getMessage());
                approvedPayments = 0L;
            }
            
            try {
                pendingPayments = paymentRepository.getCountByStatusToday("PEND_LIQ");
                System.out.println("DEBUG: Pending payments: " + pendingPayments);
            } catch (Exception e) {
                System.out.println("ERROR getting pending payments: " + e.getMessage());
                pendingPayments = 0L;
            }
            
            try {
                liquidatedPayments = paymentRepository.getCountByStatusToday("LIQUIDADA");
                System.out.println("DEBUG: Liquidated payments: " + liquidatedPayments);
            } catch (Exception e) {
                System.out.println("ERROR getting liquidated payments: " + e.getMessage());
                liquidatedPayments = 0L;
            }
            
            // Get amounts by status for today
            BigDecimal approvedAmount = paymentRepository.getTotalByStatusToday("APROBADA");
            BigDecimal liquidatedAmount = paymentRepository.getTotalByStatusToday("LIQUIDADA");
            
            // Pending liquidation includes both APROBADA and PEND_LIQ
            BigDecimal pendingAmount = paymentRepository.getTotalPendingLiquidationToday();
            Long pendingLiquidationCount = paymentRepository.getCountPendingLiquidationToday();
            
            System.out.println("DEBUG: Pending liquidation amount: " + pendingAmount);
            System.out.println("DEBUG: Pending liquidation count: " + pendingLiquidationCount);
            
            // Get yesterday data for comparison
            BigDecimal totalAmountYesterday = paymentRepository.getTotalSalesYesterday();
            Long totalPaymentsYesterday = paymentRepository.getPaymentCountYesterday();
            
            // Handle null values with defaults
            if (totalPayments == null) totalPayments = 0L;
            if (totalAmount == null) totalAmount = BigDecimal.ZERO;
            if (approvedPayments == null) approvedPayments = 0L;
            if (pendingPayments == null) pendingPayments = 0L;
            if (liquidatedPayments == null) liquidatedPayments = 0L;
            if (approvedAmount == null) approvedAmount = BigDecimal.ZERO;
            if (pendingAmount == null) pendingAmount = BigDecimal.ZERO;
            if (liquidatedAmount == null) liquidatedAmount = BigDecimal.ZERO;
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
            
            // Return complete stats with proper counts
            return new DashboardStats(
                totalPayments, approvedPayments, pendingPayments, liquidatedPayments, // counts
                totalAmount, approvedAmount, pendingAmount, liquidatedAmount, // amounts
                totalAmountYesterday, totalPaymentsYesterday, salesChangePercent
            );
            
        } catch (Exception e) {
            // If there's any error, return default values
            return new DashboardStats(
                0L, 0L, 0L, 0L, // counts
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, // amounts
                BigDecimal.ZERO, 0L, 0.0 // yesterday data
            );
        }
    }
}