package com.uxdual.portal.service;

import com.uxdual.portal.dto.PaymentDto;
import com.uxdual.portal.dto.PaymentFilter;
import com.uxdual.portal.model.Payment;
import com.uxdual.portal.model.Branch;
import com.uxdual.portal.model.Cashier;
import com.uxdual.portal.model.Customer;
import com.uxdual.portal.repository.PaymentRepository;
import com.uxdual.portal.repository.BranchRepository;
import com.uxdual.portal.repository.CashierRepository;
import com.uxdual.portal.repository.CustomerRepository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Singleton;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class PaymentService {
    
    private final PaymentRepository paymentRepository;
    private final BranchRepository branchRepository;
    private final CashierRepository cashierRepository;
    private final CustomerRepository customerRepository;
    
    public PaymentService(PaymentRepository paymentRepository,
                         BranchRepository branchRepository,
                         CashierRepository cashierRepository,
                         CustomerRepository customerRepository) {
        this.paymentRepository = paymentRepository;
        this.branchRepository = branchRepository;
        this.cashierRepository = cashierRepository;
        this.customerRepository = customerRepository;
    }
    
    public Page<PaymentDto> getPayments(PaymentFilter filter, List<Long> allowedBranchIds, List<Long> allowedCashierIds) {
        // Apply role-based filtering
        List<Long> branchIds = filter.getBranchIds() != null ? 
            filter.getBranchIds().stream().filter(allowedBranchIds::contains).collect(Collectors.toList()) :
            allowedBranchIds;
            
        List<Long> cashierIds = filter.getCashierIds() != null ?
            filter.getCashierIds().stream().filter(allowedCashierIds::contains).collect(Collectors.toList()) :
            allowedCashierIds;
        
        Pageable pageable = Pageable.from(filter.getPage(), filter.getSize());
        
        Page<Payment> payments = paymentRepository.findWithFilters(
            branchIds,
            filter.getFromDate(),
            filter.getToDate(),
            filter.getStatus(),
            filter.getMinAmount(),
            filter.getMaxAmount(),
            filter.getPaymentId(),
            filter.getOrderId(),
            pageable
        );
        
        return payments.map(this::convertToDto);
    }
    
    public List<PaymentDto> getUpdatedPayments(LocalDateTime sinceCursor, List<Long> allowedBranchIds, List<Long> allowedCashierIds) {
        List<Payment> payments;
        
        // Use cashier-specific query if user is cashier, otherwise use branch-based
        if (allowedCashierIds.size() < allowedBranchIds.size() * 5) { // Heuristic for cashier role
            payments = paymentRepository.findByLastSyncCursorAfterAndCashierIdIn(sinceCursor, allowedCashierIds);
        } else {
            payments = paymentRepository.findByLastSyncCursorAfterAndBranchIdIn(sinceCursor, allowedBranchIds);
        }
        
        return payments.stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }
    
    public Page<PaymentDto> getAllPayments() {
        Pageable pageable = Pageable.from(0, 20);
        Page<Payment> payments = paymentRepository.findAllOrderByCreatedAtDesc(pageable);
        return payments.map(this::convertToDto);
    }
    
    public PaymentDto getPaymentDetail(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
            .orElseThrow(() -> new RuntimeException("Payment not found"));
        
        return convertToDto(payment);
    }
    
    private PaymentDto convertToDto(Payment payment) {
        PaymentDto dto = new PaymentDto();
        dto.setId(payment.getId());
        dto.setPaymentId(payment.getPaymentId());
        dto.setOrderId(payment.getOrderId());
        dto.setAmount(payment.getAmount());
        dto.setMethod(payment.getMethod());
        dto.setStatus(payment.getStatus());
        dto.setSettlementEta(payment.getSettlementEta());
        dto.setStatusTimeline(payment.getStatusTimeline());
        dto.setCreatedAt(payment.getCreatedAt());
        dto.setUpdatedAt(payment.getUpdatedAt());
        
        // Load related entities manually since JPA relations are not working
        if (payment.getCustomerId() != null) {
            customerRepository.findById(payment.getCustomerId())
                .ifPresent(customer -> dto.setCustomerName(customer.getName()));
        }
        
        if (payment.getBranchId() != null) {
            branchRepository.findById(payment.getBranchId())
                .ifPresent(branch -> dto.setBranchName(branch.getName()));
        }
        
        if (payment.getCashierId() != null) {
            cashierRepository.findById(payment.getCashierId())
                .ifPresent(cashier -> dto.setCashierName(cashier.getName()));
        }
        
        return dto;
    }
}
