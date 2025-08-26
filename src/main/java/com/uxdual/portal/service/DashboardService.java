package com.uxdual.portal.service;

import com.uxdual.portal.dto.DashboardStats;
import com.uxdual.portal.model.PaymentStatus;
import com.uxdual.portal.repository.PaymentRepository;
import jakarta.inject.Singleton;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Singleton
public class DashboardService {
    
    private final PaymentRepository paymentRepository;
    
    public DashboardService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }
    
    public DashboardStats getDashboardStats(List<Long> branchIds) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfDay = now.with(LocalTime.MIN);
        LocalDateTime endOfDay = now.with(LocalTime.MAX);
        LocalDateTime startOfMonth = now.withDayOfMonth(1).with(LocalTime.MIN);
        LocalDateTime endOfMonth = now.withDayOfMonth(now.getMonth().length(now.toLocalDate().isLeapYear())).with(LocalTime.MAX);
        
        // Sales today
        Long salesToday = paymentRepository.countTodaysByBranches(branchIds, startOfDay, endOfDay);
        
        // Approved today
        Long approvedToday = paymentRepository.countTodaysByStatusAndBranches(
            branchIds, PaymentStatus.APROBADA, startOfDay, endOfDay);
        
        // Pending settlement
        Long pendingSettlement = paymentRepository.countTodaysByStatusAndBranches(
            branchIds, PaymentStatus.PEND_LIQ, startOfDay, endOfDay);
        
        // Settlement month
        BigDecimal settlementMonth = paymentRepository.sumByStatusAndBranchesForMonth(
            branchIds, PaymentStatus.LIQUIDADA, startOfMonth, endOfMonth);
        
        if (settlementMonth == null) {
            settlementMonth = BigDecimal.ZERO;
        }
        
        return new DashboardStats(salesToday, approvedToday, pendingSettlement, settlementMonth);
    }
}
