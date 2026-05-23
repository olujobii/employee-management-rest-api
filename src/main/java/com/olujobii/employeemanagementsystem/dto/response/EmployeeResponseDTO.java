package com.olujobii.employeemanagementsystem.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EmployeeResponseDTO(
        Long id,
        String firstName,
        String lastName,
        String email,
        String department,
        BigDecimal salary,
        LocalDate dateOfJoining,
        Boolean isActive,
        Boolean isAnIntern
) {
}
