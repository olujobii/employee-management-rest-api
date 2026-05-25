package com.olujobii.employeemanagementsystem.service;

import com.olujobii.employeemanagementsystem.dto.request.EmployeeRequestDTO;
import com.olujobii.employeemanagementsystem.dto.response.EmployeeResponseDTO;
import com.olujobii.employeemanagementsystem.dto.response.ResponseWrapper;
import jakarta.validation.Valid;

import java.math.BigDecimal;
import java.util.List;

public interface EmployeeService {
    ResponseWrapper<List<EmployeeResponseDTO>> getAllEmployees(Integer page, Integer size, String sort);

    ResponseWrapper<EmployeeResponseDTO> getEmployee(Long id);

    void createEmployee(@Valid EmployeeRequestDTO payload);

    ResponseWrapper<EmployeeResponseDTO> updateEmployee(Long id, @Valid EmployeeRequestDTO payload);

    void softDeleteEmployee(Long id);

    void hardDeleteEmployee(Long id);

    ResponseWrapper<List<EmployeeResponseDTO>> fetchEmployeeBySalaryRange(BigDecimal min, BigDecimal max);
}
