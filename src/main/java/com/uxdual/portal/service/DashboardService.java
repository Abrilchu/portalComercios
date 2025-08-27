package com.uxdual.portal.service;

import com.uxdual.portal.dto.DashboardStats;
// import io.micronaut.cache.annotation.Cacheable;
import jakarta.inject.Singleton;
import com.uxdual.portal.repository.PaymentRepository;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import jakarta.inject.Inject;

@Singleton
public class DashboardService {
    
    private final PaymentRepository paymentRepository;
    
    @Inject
    private DataSource dataSource;
    
    public DashboardService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }
    
    // @Cacheable("dashboard-stats") // Disabled until cache dependency is added
    public DashboardStats getDashboardStats(java.util.List<Long> branchIds) {
        return getOptimizedDashboardStats();
    }

    private DashboardStats getOptimizedDashboardStats() {
        // Single optimized query for all dashboard data
        String sql = """
            WITH today_stats AS (
                SELECT 
                    COUNT(*) as total_payments,
                    COALESCE(SUM(amount), 0) as total_amount,
                    COUNT(CASE WHEN status = 'APROBADA' THEN 1 END) as approved_payments,
                    COALESCE(SUM(CASE WHEN status = 'APROBADA' THEN amount ELSE 0 END), 0) as approved_amount,
                    COUNT(CASE WHEN status IN ('PEND_LIQ', 'APROBADA') THEN 1 END) as pending_payments,
                    COALESCE(SUM(CASE WHEN status IN ('PEND_LIQ', 'APROBADA') THEN amount ELSE 0 END), 0) as pending_amount,
                    COUNT(CASE WHEN status = 'LIQUIDADA' THEN 1 END) as liquidated_payments,
                    COALESCE(SUM(CASE WHEN status = 'LIQUIDADA' THEN amount ELSE 0 END), 0) as liquidated_amount
                FROM payments
                WHERE DATE(created_at) = CURRENT_DATE
            ),
            yesterday_stats AS (
                SELECT 
                    COUNT(*) as total_payments_yesterday,
                    COALESCE(SUM(amount), 0) as total_amount_yesterday
                FROM payments
                WHERE DATE(created_at) = CURRENT_DATE - INTERVAL '1 day'
            )
            SELECT 
                ts.total_payments,
                ts.total_amount,
                ts.approved_payments,
                ts.approved_amount,
                ts.pending_payments,
                ts.pending_amount,
                ts.liquidated_payments,
                ts.liquidated_amount,
                ys.total_payments_yesterday,
                ys.total_amount_yesterday
            FROM today_stats ts
            CROSS JOIN yesterday_stats ys
            """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return buildOptimizedDashboardStats(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching optimized dashboard stats: " + e.getMessage());
            // Fallback to individual queries
            return getFallbackDashboardStats();
        }

        return getFallbackDashboardStats();
    }

    private DashboardStats buildOptimizedDashboardStats(ResultSet rs) throws SQLException {
        Long totalPayments = rs.getLong("total_payments");
        BigDecimal totalAmount = rs.getBigDecimal("total_amount");
        Long approvedPayments = rs.getLong("approved_payments");
        BigDecimal approvedAmount = rs.getBigDecimal("approved_amount");
        Long pendingPayments = rs.getLong("pending_payments");
        BigDecimal pendingAmount = rs.getBigDecimal("pending_amount");
        Long liquidatedPayments = rs.getLong("liquidated_payments");
        BigDecimal liquidatedAmount = rs.getBigDecimal("liquidated_amount");
        Long totalPaymentsYesterday = rs.getLong("total_payments_yesterday");
        BigDecimal totalAmountYesterday = rs.getBigDecimal("total_amount_yesterday");

        // Calculate percentage change
        Double salesChangePercent = 0.0;
        if (totalAmountYesterday.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal difference = totalAmount.subtract(totalAmountYesterday);
            BigDecimal percentChange = difference.divide(totalAmountYesterday, 4, RoundingMode.HALF_UP)
                                               .multiply(BigDecimal.valueOf(100));
            salesChangePercent = percentChange.doubleValue();
        } else if (totalAmount.compareTo(BigDecimal.ZERO) > 0) {
            salesChangePercent = 100.0;
        }

        return new DashboardStats(
            totalPayments, approvedPayments, pendingPayments, liquidatedPayments,
            totalAmount, approvedAmount, pendingAmount, liquidatedAmount,
            totalAmountYesterday, totalPaymentsYesterday, salesChangePercent
        );
    }

    private DashboardStats getFallbackDashboardStats() {
        // Fallback to repository methods
        Long totalPayments = paymentRepository.getPaymentCountToday();
        BigDecimal totalAmount = paymentRepository.getTotalSalesToday();
        BigDecimal totalAmountYesterday = paymentRepository.getTotalSalesYesterday();
        Long totalPaymentsYesterday = paymentRepository.getPaymentCountYesterday();
        
        // Handle null values
        if (totalPayments == null) totalPayments = 0L;
        if (totalAmount == null) totalAmount = BigDecimal.ZERO;
        if (totalAmountYesterday == null) totalAmountYesterday = BigDecimal.ZERO;
        if (totalPaymentsYesterday == null) totalPaymentsYesterday = 0L;
        
        Double salesChangePercent = 0.0;
        if (totalAmountYesterday.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal difference = totalAmount.subtract(totalAmountYesterday);
            BigDecimal percentChange = difference.divide(totalAmountYesterday, 4, RoundingMode.HALF_UP)
                                               .multiply(BigDecimal.valueOf(100));
            salesChangePercent = percentChange.doubleValue();
        } else if (totalAmount.compareTo(BigDecimal.ZERO) > 0) {
            salesChangePercent = 100.0;
        }
        
        return new DashboardStats(
            totalPayments, 0L, 0L, 0L,
            totalAmount, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
            totalAmountYesterday, totalPaymentsYesterday, salesChangePercent
        );
    }
}
