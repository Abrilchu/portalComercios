package com.uxdual.portal.service;

import com.uxdual.portal.dto.LoginRequest;
import com.uxdual.portal.dto.LoginResponse;
import com.uxdual.portal.model.Branch;
import com.uxdual.portal.model.Cashier;
import com.uxdual.portal.model.User;
import com.uxdual.portal.model.UserRole;
import com.uxdual.portal.repository.BranchRepository;
import com.uxdual.portal.repository.CashierRepository;
import com.uxdual.portal.repository.UserRepository;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.exceptions.HttpStatusException;
import io.micronaut.security.token.jwt.generator.JwtTokenGenerator;
import jakarta.inject.Singleton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
public class AuthService {
    
    private final UserRepository userRepository;
    private final BranchRepository branchRepository;
    private final CashierRepository cashierRepository;
    private final JwtTokenGenerator tokenGenerator;
    
    public AuthService(UserRepository userRepository, BranchRepository branchRepository,
                      CashierRepository cashierRepository, JwtTokenGenerator tokenGenerator) {
        this.userRepository = userRepository;
        this.branchRepository = branchRepository;
        this.cashierRepository = cashierRepository;
        this.tokenGenerator = tokenGenerator;
    }
    
    public LoginResponse login(LoginRequest loginRequest) {
        Optional<User> userOpt = userRepository.findByUsername(loginRequest.getUsername());
        
        if (userOpt.isEmpty()) {
            throw new HttpStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
        
        User user = userOpt.get();
        
        if (!user.isActive()) {
            throw new HttpStatusException(HttpStatus.UNAUTHORIZED, "User account is inactive");
        }
        
        if (!verifyPassword(loginRequest.getPassword(), user.getPasswordHash())) {
            throw new HttpStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
        
        // Get user's branches and cashiers based on role
        List<Long> branchIds = getBranchIdsForUser(user);
        List<Long> cashierIds = getCashierIdsForUser(user, branchIds);
        
        // Generate JWT token with business claims
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", user.getUsername());
        claims.put("userId", user.getId());
        claims.put("role", user.getRole().getValue());
        claims.put("commerceId", user.getCommerceId());
        claims.put("branchIds", branchIds);
        claims.put("cashierIds", cashierIds);
        
        Optional<String> tokenOpt = tokenGenerator.generateToken(claims);
        
        if (tokenOpt.isEmpty()) {
            throw new HttpStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to generate token");
        }
        
        return new LoginResponse(
            tokenOpt.get(),
            user.getUsername(),
            user.getEmail(),
            user.getRole(),
            user.getCommerceId(),
            branchIds,
            cashierIds
        );
    }
    
    private List<Long> getBranchIdsForUser(User user) {
        switch (user.getRole()) {
            case OWNER:
                return branchRepository.findByCommerceIdAndActive(user.getCommerceId(), true)
                    .stream()
                    .map(Branch::getId)
                    .collect(Collectors.toList());
            case MANAGER:
                return branchRepository.findByManagerIdAndActive(user.getId(), true)
                    .stream()
                    .map(Branch::getId)
                    .collect(Collectors.toList());
            case CASHIER:
                return cashierRepository.findByUserIdAndActive(user.getId(), true)
                    .stream()
                    .map(Cashier::getBranchId)
                    .distinct()
                    .collect(Collectors.toList());
            default:
                return List.of();
        }
    }
    
    private List<Long> getCashierIdsForUser(User user, List<Long> branchIds) {
        switch (user.getRole()) {
            case OWNER:
            case MANAGER:
                return cashierRepository.findByBranchIdInAndActive(branchIds, true)
                    .stream()
                    .map(Cashier::getId)
                    .collect(Collectors.toList());
            case CASHIER:
                return cashierRepository.findByUserIdAndActive(user.getId(), true)
                    .stream()
                    .map(Cashier::getId)
                    .collect(Collectors.toList());
            default:
                return List.of();
        }
    }
    
    private boolean verifyPassword(String password, String hashedPassword) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            
            return hexString.toString().equals(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }
}
