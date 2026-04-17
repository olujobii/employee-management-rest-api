package com.olujobii.employeerestapi.department.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DepartmentRequestDto(
        @NotBlank(message = "departmentName is a required field")
        @Size(max = 100, message = "departmentName must be between 1 and 100 characters")
        String departmentName,

        @NotNull(message = "isAcceptingIntern is a required field")
        Boolean isAcceptingIntern
) {
}
