package com.uxdual.portal.repository;

import com.uxdual.portal.model.User;
import com.uxdual.portal.model.UserRole;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

@JdbcRepository(dialect = Dialect.POSTGRES)
public interface UserRepository extends CrudRepository<User, Long> {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
    
    List<User> findByCommerceIdAndActive(Long commerceId, boolean active);
    
    List<User> findByRoleAndCommerceId(UserRole role, Long commerceId);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
}
