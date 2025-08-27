package com.uxdual.portal.service;

import com.uxdual.portal.dto.CashierDto;
import com.uxdual.portal.model.Cashier;
import com.uxdual.portal.repository.CashierRepository;
import com.uxdual.portal.repository.BranchRepository;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class CashierService {
    
    private final CashierRepository cashierRepository;
    private final BranchRepository branchRepository;
    
    public CashierService(CashierRepository cashierRepository, BranchRepository branchRepository) {
        this.cashierRepository = cashierRepository;
        this.branchRepository = branchRepository;
    }
    
    public List<CashierDto> getAllCashiers() {
        List<Cashier> cashiers = (List<Cashier>) cashierRepository.findAll();
        return cashiers.stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }
    
    private CashierDto convertToDto(Cashier cashier) {
        CashierDto dto = new CashierDto();
        dto.setId(cashier.getId());
        dto.setName(cashier.getName());
        dto.setCode(cashier.getCode());
        dto.setBranchId(cashier.getBranchId());
        
        // Load branch name
        if (cashier.getBranchId() != null) {
            branchRepository.findById(cashier.getBranchId())
                .ifPresent(branch -> dto.setBranchName(branch.getName()));
        }
        
        return dto;
    }
}