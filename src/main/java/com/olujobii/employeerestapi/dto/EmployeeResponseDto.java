package com.olujobii.employeerestapi.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record EmployeeResponseDto(Long id,
                                  String firstName,
                                  String lastName,
                                  String email,
                                  String department,
                                  BigDecimal salary,
                                  LocalDate dateOfJoining,
                                  boolean active,
                                  LocalDateTime createdAt,
                                  LocalDateTime updatedAt) {
}
