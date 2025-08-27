package com.uxdual.portal.dto;

public class BranchDto {
    private Long id;
    private String name;
    private String location;
    private String code;

    // Constructors
    public BranchDto() {}

    public BranchDto(Long id, String name, String location, String code) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.code = code;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
}