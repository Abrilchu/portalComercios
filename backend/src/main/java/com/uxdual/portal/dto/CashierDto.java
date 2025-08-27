package com.uxdual.portal.dto;

public class CashierDto {
    private Long id;
    private String name;
    private String code;
    private Long branchId;
    private String branchName;

    // Constructors
    public CashierDto() {}

    public CashierDto(Long id, String name, String code, Long branchId, String branchName) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.branchId = branchId;
        this.branchName = branchName;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public Long getBranchId() { return branchId; }
    public void setBranchId(Long branchId) { this.branchId = branchId; }

    public String getBranchName() { return branchName; }
    public void setBranchName(String branchName) { this.branchName = branchName; }
}