package com.olujobii.employeemanagementsystem.dto.response;

import org.springframework.http.HttpStatusCode;

import java.time.LocalDateTime;

public record APIErrorResponseDTO(
        String message,
        LocalDateTime timestamp,
        HttpStatusCode statusCode
) {
}
