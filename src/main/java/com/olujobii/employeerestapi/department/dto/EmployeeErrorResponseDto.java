package com.olujobii.employeerestapi.department.dto;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record EmployeeErrorResponseDto(HttpStatus httpStatus, String message, LocalDateTime timestamp) {
}
