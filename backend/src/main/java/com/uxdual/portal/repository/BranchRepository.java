package com.uxdual.portal.repository;

import com.uxdual.portal.model.Branch;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

@JdbcRepository(dialect = Dialect.POSTGRES)
public interface BranchRepository extends CrudRepository<Branch, Long> {
    
    List<Branch> findByCommerceIdAndActive(Long commerceId, boolean active);
    
    List<Branch> findByManagerIdAndActive(Long managerId, boolean active);
    
    List<Branch> findByIdInAndActive(List<Long> ids, boolean active);
}
