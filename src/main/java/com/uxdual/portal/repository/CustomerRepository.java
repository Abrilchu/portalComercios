package com.uxdual.portal.repository;

import com.uxdual.portal.model.Customer;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;

@JdbcRepository(dialect = Dialect.POSTGRES)
public interface CustomerRepository extends CrudRepository<Customer, Long> {
    
    Page<Customer> findByCommerceId(Long commerceId, Pageable pageable);
    
    @Query(value = "SELECT * FROM customers WHERE commerce_id = :commerceId " +
           "AND (LOWER(name) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR phone LIKE CONCAT('%', :search, '%') " +
           "OR customer_id LIKE CONCAT('%', :search, '%'))",
           countQuery = "SELECT count(*) FROM customers WHERE commerce_id = :commerceId " +
           "AND (LOWER(name) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR phone LIKE CONCAT('%', :search, '%') " +
           "OR customer_id LIKE CONCAT('%', :search, '%'))")
    Page<Customer> searchByCommerceId(Long commerceId, String search, Pageable pageable);
}
