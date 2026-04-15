package com.olujobii.employeerestapi.mapper;

import com.olujobii.employeerestapi.dto.EmployeeResponseDto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class EmployeeResponseMapper {

    public static EmployeeResponseDto toEmployeeResponseDto(Long id, String firstName, String lastName, String email,
        String department, BigDecimal salary, LocalDate dateOfJoining, boolean active){

        return new EmployeeResponseDto(id,firstName,lastName,email,department,salary,dateOfJoining, active);
    }
}
