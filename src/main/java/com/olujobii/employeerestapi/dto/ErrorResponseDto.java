package com.olujobii.employeerestapi.dto;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ErrorResponseDto(HttpStatus httpStatus, String message, LocalDateTime localDateTime) {
}
