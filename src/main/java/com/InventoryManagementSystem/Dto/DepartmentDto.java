package com.InventoryManagementSystem.Dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DepartmentDto {
    private Long departmentId;
    private String departmentCode;
    private String departmentName;
    private Boolean display;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}