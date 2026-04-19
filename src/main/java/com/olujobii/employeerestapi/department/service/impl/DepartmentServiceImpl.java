package com.olujobii.employeerestapi.department.service.impl;

import com.olujobii.employeerestapi.department.dto.request.DepartmentRequestDto;
import com.olujobii.employeerestapi.department.dto.response.DepartmentResponseDto;
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

import java.util.List;

@Service
@AllArgsConstructor
@Validated
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Override
    public void createDepartment(@Valid DepartmentRequestDto departmentRequestDto){
        departmentRepository.findByDepartmentName(departmentRequestDto.departmentName().trim())
                .ifPresent(department -> {
                    throw new DuplicateDepartmentException("Department already exist", HttpStatus.CONFLICT);
                });

        Department department = Department.builder()
                .departmentName(departmentRequestDto.departmentName().trim())
                .isAcceptingIntern(departmentRequestDto.isAcceptingIntern())
                .build();

        departmentRepository.save(department);
    }

    @Override
    public DepartmentResponseDto getDepartmentById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException("Department does not exist for ID: "+id, HttpStatus.NOT_FOUND));

        return new DepartmentResponseDto(department.getDepartmentId(),department.getDepartmentName(),department.getIsAcceptingIntern());
    }

    @Override
    public Department searchDepartmentByName(String departmentName) {
        return departmentRepository.findByDepartmentName(departmentName)
                .orElseThrow(() -> new DepartmentNotFoundException("Department does not exist: "+departmentName, HttpStatus.NOT_FOUND));
    }

    @Override
    public void deleteDepartment(Long id) {
        departmentRepository.findById(id).orElseThrow(() -> new DepartmentNotFoundException("Department does not exist for ID: "+id, HttpStatus.NOT_FOUND));

        departmentRepository.deleteById(id);
    }

    @Override
    public List<DepartmentResponseDto> getDepartments() {
        return departmentRepository.findAll().stream()
                .map(dep -> new DepartmentResponseDto(dep.getDepartmentId(),dep.getDepartmentName(),dep.getIsAcceptingIntern()))
                .toList();
    }

    @Override
    public void updateDepartment(@Valid DepartmentRequestDto departmentRequestDto,Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException("Department does not exist for ID: "+id,HttpStatus.NOT_FOUND));

        //Check if another record has the department name
        departmentRepository.findByDepartmentNameWhereIdNotEqualTo(id, departmentRequestDto.departmentName().trim())
                .ifPresent(dep -> {
                    throw new DuplicateDepartmentException("Department already exists",HttpStatus.CONFLICT);
                });

        //Update record
        department.setDepartmentName(departmentRequestDto.departmentName().trim());
        department.setIsAcceptingIntern(departmentRequestDto.isAcceptingIntern());

        departmentRepository.save(department);
    }
}
