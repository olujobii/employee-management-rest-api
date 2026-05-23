package com.olujobii.employeemanagementsystem.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EmployeeRequestDTO(
        @NotBlank(message = "firstName is a required field")
        @Size(min = 2, max = 50, message = "firstName must be between 2 and 50 characters")
        String firstName,

        @NotBlank(message = "lastName is a required field")
        @Size(min = 2, max = 50, message = "lastName must be between 2 and 50 characters")
        String lastName,

        @NotBlank(message = "email is a required field")
        @Email(message = "Must be a valid email")
        String email,

        @NotNull(message = "department is a required field")
        Long departmentId,

        @NotNull(message = "salary is a required field")
        BigDecimal salary,

        @NotNull(message = "dateOfJoining is a required field")
        @JsonFormat(pattern = "yyyy-MM-dd")
        @PastOrPresent(message = "dateOfJoining must be the current date or a past date")
        LocalDate dateOfJoining,

        Boolean active,

        @NotNull(message = "isAnIntern is a required field")
        Boolean isAnIntern
) {

        public EmployeeRequestDTO{
              if(active == null)
                      active = true;
        }
}
