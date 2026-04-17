package com.olujobii.employeerestapi.department.dto.response;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record DepartmentErrorResponseDto(HttpStatus httpStatus, String message, LocalDateTime timestamp) {
}
