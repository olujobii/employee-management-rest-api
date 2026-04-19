package com.olujobii.employeerestapi.employee.dto.request;

import java.math.BigDecimal;


public record EmployeePatchRequestDto(BigDecimal salary, String departmentName, Boolean active) {
}
