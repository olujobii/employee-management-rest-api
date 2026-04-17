package com.olujobii.employeerestapi.employee.dto;

import java.math.BigDecimal;


public record EmployeePatchRequestDto(BigDecimal salary, Long departmentId, Boolean active) {
}
