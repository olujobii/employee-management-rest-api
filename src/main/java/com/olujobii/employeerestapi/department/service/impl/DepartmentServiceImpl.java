package com.olujobii.employeerestapi.department.service.impl;

import com.olujobii.employeerestapi.department.dto.DepartmentRequestDto;
import com.olujobii.employeerestapi.department.entity.Department;
import com.olujobii.employeerestapi.exception.DepartmentNotFoundException;
import com.olujobii.employeerestapi.exception.DuplicateDepartmentException;
import com.olujobii.employeerestapi.department.repository.DepartmentRepository;
import com.olujobii.employeerestapi.department.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@AllArgsConstructor
@Validated
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Override
    public void createDepartment(@Valid DepartmentRequestDto departmentRequestDto){
        departmentRepository.findByDepartmentName(departmentRequestDto.departmentName())
                .ifPresent(department -> {
                    throw new DuplicateDepartmentException("Department already exist");
                });

        Department department = Department.builder()
                .departmentName(departmentRequestDto.departmentName().trim())
                .isAcceptingIntern(departmentRequestDto.isAcceptingIntern())
                .build();

        departmentRepository.save(department);
    }

    @Override
    public Department getDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> {
                    throw new DepartmentNotFoundException(id, HttpStatus.NOT_FOUND);
                });
    }

    @Override
    public void deleteDepartment(Long id) {
        departmentRepository.findById(id).orElseThrow(() -> {
            throw new DepartmentNotFoundException(id, HttpStatus.NOT_FOUND);
        });

        departmentRepository.deleteById(id);
    }
}
