package com.uxdual.portal.service;

import com.uxdual.portal.dto.BranchDto;
import com.uxdual.portal.model.Branch;
import com.uxdual.portal.repository.BranchRepository;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class BranchService {
    
    private final BranchRepository branchRepository;
    
    public BranchService(BranchRepository branchRepository) {
        this.branchRepository = branchRepository;
    }
    
    public List<BranchDto> getAllBranches() {
        List<Branch> branches = (List<Branch>) branchRepository.findAll();
        return branches.stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }
    
    private BranchDto convertToDto(Branch branch) {
        return new BranchDto(
            branch.getId(),
            branch.getName(),
            branch.getName(), // Use name as location for now
            branch.getName() // Use name as code for now
        );
    }
}