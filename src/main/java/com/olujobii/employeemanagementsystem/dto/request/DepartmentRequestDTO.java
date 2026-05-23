package com.olujobii.employeemanagementsystem.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DepartmentRequestDTO(
        @NotBlank(message = "departmentName is a required field")
        @Size(min = 2, max = 100, message = "departmentName must be between 2 and 100 characters")
        String departmentName,

        @NotNull(message = "isAcceptingIntern is a required field")
        Boolean isAcceptingIntern,

        Boolean isActive
) {

    //To set default value if isActive is not provided
    public DepartmentRequestDTO{
        if(isActive == null)
            isActive = true;
    }
}
