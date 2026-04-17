package com.olujobii.employeerestapi.employee.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EmployeeResponseDto(Long id,
                                  String firstName,
                                  String lastName,
                                  String email,
                                  String department,
                                  BigDecimal salary,
                                  LocalDate dateOfJoining,
                                  Boolean active,
                                  Boolean isAnIntern) {
}
