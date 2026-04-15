package com.olujobii.employeerestapi.service;

import com.olujobii.employeerestapi.dto.EmployeePatchRequestDto;
import com.olujobii.employeerestapi.dto.EmployeeRequestDto;
import com.olujobii.employeerestapi.dto.EmployeeResponseDto;
import jakarta.validation.Valid;

import java.util.List;


public interface EmployeeService {
    void createEmployee(@Valid EmployeeRequestDto employeeRequestDto);

    List<EmployeeResponseDto> getEmployees();

     EmployeeResponseDto getEmployeeById(Long id);

    void updateEmployeeData(Long id, @Valid EmployeeRequestDto employeeRequestDto);

    void updateSpecificEmployeeData(Long id, EmployeePatchRequestDto employeePatchRequestDto);

    void softDeleteEmployee(Long id);

    void hardDeleteEmployee(Long id);
}
