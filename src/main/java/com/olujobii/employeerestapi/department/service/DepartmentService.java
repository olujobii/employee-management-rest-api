package com.olujobii.employeerestapi.department.service;

import com.olujobii.employeerestapi.department.dto.DepartmentRequestDto;
import com.olujobii.employeerestapi.department.entity.Department;
import jakarta.validation.Valid;

public interface DepartmentService {

    void createDepartment(@Valid DepartmentRequestDto departmentRequestDto);

    Department getDepartmentById(Long id);

    void deleteDepartment(Long id);
}
