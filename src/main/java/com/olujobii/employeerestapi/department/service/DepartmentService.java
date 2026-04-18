package com.olujobii.employeerestapi.department.service;

import com.olujobii.employeerestapi.department.dto.request.DepartmentRequestDto;
import com.olujobii.employeerestapi.department.dto.response.DepartmentResponseDto;
import com.olujobii.employeerestapi.department.entity.Department;
import jakarta.validation.Valid;

import java.util.List;

public interface DepartmentService {

    void createDepartment(@Valid DepartmentRequestDto departmentRequestDto);

    DepartmentResponseDto getDepartmentById(Long id);

    Department searchDepartmentById(Long id);

    void deleteDepartment(Long id);

    List<DepartmentResponseDto> getDepartments();

    void updateDepartment(@Valid  DepartmentRequestDto departmentRequestDto,Long id);
}
