package com.uxdual.portal.repository;

import com.uxdual.portal.model.Cashier;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

@JdbcRepository(dialect = Dialect.POSTGRES)
public interface CashierRepository extends CrudRepository<Cashier, Long> {
    
    List<Cashier> findByBranchIdAndActive(Long branchId, boolean active);
    
    List<Cashier> findByUserIdAndActive(Long userId, boolean active);
    
    List<Cashier> findByBranchIdInAndActive(List<Long> branchIds, boolean active);
}
