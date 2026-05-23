package com.olujobii.employeemanagementsystem.service;

import com.olujobii.employeemanagementsystem.dto.request.DepartmentRequestDTO;
import com.olujobii.employeemanagementsystem.dto.response.DepartmentResponseDTO;
import com.olujobii.employeemanagementsystem.dto.response.ResponseWrapper;
import jakarta.validation.Valid;

import java.util.List;

public interface DepartmentService {
    ResponseWrapper<List<DepartmentResponseDTO>> getAllDepartments();

    void createDepartment(@Valid DepartmentRequestDTO payload);

    ResponseWrapper<DepartmentResponseDTO> getDepartment(Long id);

    ResponseWrapper<DepartmentResponseDTO> updateDepartment(Long id, @Valid DepartmentRequestDTO payload);

    void softDeleteDepartment(Long id);

    void hardDeleteDepartment(Long id);
}
