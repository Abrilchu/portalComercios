package com.uxdual.portal.repository;

import com.uxdual.portal.model.Payment;
import com.uxdual.portal.model.PaymentStatus;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import io.micronaut.core.annotation.Nullable;

@JdbcRepository(dialect = Dialect.POSTGRES)
public interface PaymentRepository extends CrudRepository<Payment, Long> {
    
    @Query(value = "SELECT * FROM payments ORDER BY created_at DESC",
           countQuery = "SELECT COUNT(*) FROM payments")
    Page<Payment> findAllOrderByCreatedAtDesc(Pageable pageable);
    

    
    Page<Payment> findByBranchIdInOrderByCreatedAtDesc(List<Long> branchIds, Pageable pageable);
    
    Page<Payment> findByCashierIdInOrderByCreatedAtDesc(List<Long> cashierIds, Pageable pageable);
    
    List<Payment> findByLastSyncCursorAfterAndBranchIdIn(LocalDateTime sinceCursor, List<Long> branchIds);
    
    List<Payment> findByLastSyncCursorAfterAndCashierIdIn(LocalDateTime sinceCursor, List<Long> cashierIds);
    
    @Query(value = "SELECT * FROM payments p WHERE p.branch_id = ANY(:branchIds) " +
           "AND (:fromDate IS NULL OR p.created_at >= :fromDate) " +
           "AND (:toDate IS NULL OR p.created_at <= :toDate) " +
           "AND (:status IS NULL OR p.status = :status) " +
           "AND (:minAmount IS NULL OR p.amount >= :minAmount) " +
           "AND (:maxAmount IS NULL OR p.amount <= :maxAmount) " +
           "AND (:paymentId IS NULL OR p.payment_id LIKE CONCAT('%', :paymentId, '%')) " +
           "AND (:orderId IS NULL OR p.order_id LIKE CONCAT('%', :orderId, '%')) " +
           "ORDER BY p.created_at DESC",
           countQuery = "SELECT COUNT(*) FROM payments p WHERE p.branch_id = ANY(:branchIds) " +
           "AND (:fromDate IS NULL OR p.created_at >= :fromDate) " +
           "AND (:toDate IS NULL OR p.created_at <= :toDate) " +
           "AND (:status IS NULL OR p.status = :status) " +
           "AND (:minAmount IS NULL OR p.amount >= :minAmount) " +
           "AND (:maxAmount IS NULL OR p.amount <= :maxAmount) " +
           "AND (:paymentId IS NULL OR p.payment_id LIKE CONCAT('%', :paymentId, '%')) " +
           "AND (:orderId IS NULL OR p.order_id LIKE CONCAT('%', :orderId, '%'))")
    Page<Payment> findWithFilters(List<Long> branchIds, @Nullable LocalDateTime fromDate, @Nullable LocalDateTime toDate, 
                                 @Nullable PaymentStatus status, @Nullable BigDecimal minAmount, @Nullable BigDecimal maxAmount,
                                 @Nullable String paymentId, @Nullable String orderId, Pageable pageable);
    
    @Query("SELECT COUNT(*) FROM payments p WHERE p.branch_id = ANY(:branchIds) " +
           "AND p.created_at >= :startOfDay AND p.created_at <= :endOfDay")
    Long countTodaysByBranches(List<Long> branchIds, LocalDateTime startOfDay, LocalDateTime endOfDay);
    
    @Query("SELECT COUNT(*) FROM payments p WHERE p.branch_id = ANY(:branchIds) " +
           "AND p.status = :status AND p.created_at >= :startOfDay AND p.created_at <= :endOfDay")
    Long countTodaysByStatusAndBranches(List<Long> branchIds, PaymentStatus status, 
                                       LocalDateTime startOfDay, LocalDateTime endOfDay);
    
    @Query("SELECT SUM(p.amount) FROM payments p WHERE p.branch_id = ANY(:branchIds) " +
           "AND p.status = :status AND p.created_at >= :startOfMonth AND p.created_at <= :endOfMonth")
    BigDecimal sumByStatusAndBranchesForMonth(List<Long> branchIds, PaymentStatus status,
                                             LocalDateTime startOfMonth, LocalDateTime endOfMonth);
}
