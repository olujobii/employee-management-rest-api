package com.olujobii.employeemanagementsystem.dto.response;

import java.time.LocalDateTime;

public record APIErrorResponseDTO(
        String message,
        LocalDateTime timestamp
) {
}
