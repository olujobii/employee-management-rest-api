package com.olujobii.employeerestapi.employee.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EmployeeRequestDto(
    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name must be between 1 and 50 characters")
    String firstName,

    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name must be between 1 and 50 characters")
    String lastName,

    @NotBlank(message = "Email is required")
    @Email
    String email,

    @NotBlank(message = "Department is required")
    @Size(max = 100, message = "Department must be between 1 and 100 characters")
    String department,

    @NotNull(message = "Salary is a required field")
    @DecimalMin("0.00")
    BigDecimal salary,

    @NotNull(message = "Date of joining is a required field")
    @PastOrPresent
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate dateOfJoining,

    // TODO: Accept default value as true if user does not specify
    @NotNull(message = "Active is a required field")
    boolean active) {


}
