package com.olujobii.employeerestapi.employee.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EmployeeRequestDto(
    @NotBlank(message = "firstName is required")
    @Size(min = 2, max = 50, message = "firstName must be between 2 and 50 characters")
    String firstName,

    @NotBlank(message = "lastName is required")
    @Size(min = 2, max = 50, message = "lastName must be between 1 and 50 characters")
    String lastName,

    @NotBlank(message = "email is required")
    @Email
    String email,

    @NotBlank(message = "departmentId is required")
    @Size(min = 2, max = 100, message = "departmentName must be between 2 and 100 characters")
    String departmentName,

    @NotNull(message = "salary is a required field")
    @DecimalMin("0.00")
    BigDecimal salary,

    @NotNull(message = "dateOfJoining is a required field")
    @PastOrPresent
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate dateOfJoining,

    @NotNull(message = "active is a required field")
    Boolean active,

    @NotNull(message = "isAnIntern is a required field")
    Boolean isAnIntern) {


}
