package com.olujobii.employeerestapi.employee.dto;

import java.math.BigDecimal;


public record EmployeePatchRequestDto(BigDecimal salary, String department, Boolean active) {
}
