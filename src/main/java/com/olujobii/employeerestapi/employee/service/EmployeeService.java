package com.olujobii.employeerestapi.employee.service;

import com.olujobii.employeerestapi.employee.dto.request.EmployeePatchRequestDto;
import com.olujobii.employeerestapi.employee.dto.request.EmployeeRequestDto;
import com.olujobii.employeerestapi.employee.dto.response.EmployeeResponseDto;
import jakarta.validation.Valid;

import java.math.BigDecimal;
import java.util.List;


public interface EmployeeService {
    void createEmployee(@Valid EmployeeRequestDto employeeRequestDto);

    List<EmployeeResponseDto> getEmployees(int pageNo, int pageSize, String sortBy, String sortDir, Boolean isActive);

     EmployeeResponseDto getEmployeeById(Long id);

    void updateEmployeeData(Long id, @Valid EmployeeRequestDto employeeRequestDto);

    void updateSpecificEmployeeData(Long id, EmployeePatchRequestDto employeePatchRequestDto);

    void softDeleteEmployee(Long id);

    void hardDeleteEmployee(Long id);

    List<EmployeeResponseDto> filterBySalaryRange(BigDecimal min, BigDecimal max);

}
