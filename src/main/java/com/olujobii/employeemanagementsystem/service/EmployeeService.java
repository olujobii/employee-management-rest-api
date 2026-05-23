package com.olujobii.employeemanagementsystem.service;

import com.olujobii.employeemanagementsystem.dto.request.EmployeeRequestDTO;
import com.olujobii.employeemanagementsystem.dto.response.EmployeeResponseDTO;
import com.olujobii.employeemanagementsystem.dto.response.ResponseWrapper;
import jakarta.validation.Valid;

import java.util.List;

public interface EmployeeService {
    ResponseWrapper<List<EmployeeResponseDTO>> getAllEmployees();

    ResponseWrapper<EmployeeResponseDTO> getEmployee(Long id);

    void createEmployee(@Valid EmployeeRequestDTO payload);

    ResponseWrapper<EmployeeResponseDTO> updateEmployee(Long id, @Valid EmployeeRequestDTO payload);
}
