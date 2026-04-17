package com.olujobii.employeerestapi.employee.mapper;

import com.olujobii.employeerestapi.employee.dto.response.EmployeeResponseDto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class EmployeeResponseMapper {

    public static EmployeeResponseDto toEmployeeResponseDto(Long id, String firstName, String lastName, String email,
        String department, BigDecimal salary, LocalDate dateOfJoining, Boolean active, Boolean isAnIntern){

        return new EmployeeResponseDto(id,firstName,lastName,email,department,salary,dateOfJoining, active, isAnIntern);
    }
}
