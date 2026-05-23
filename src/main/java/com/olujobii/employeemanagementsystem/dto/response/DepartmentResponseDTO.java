package com.olujobii.employeemanagementsystem.dto.response;

public record DepartmentResponseDTO(
        Long id,
        String departmentName,
        Boolean isAcceptingIntern,
        Boolean isActive
) {
}
