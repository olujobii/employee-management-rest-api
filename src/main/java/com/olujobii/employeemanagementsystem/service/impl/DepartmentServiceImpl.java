package com.olujobii.employeemanagementsystem.service.impl;

import com.olujobii.employeemanagementsystem.dto.request.DepartmentRequestDTO;
import com.olujobii.employeemanagementsystem.dto.response.DepartmentResponseDTO;
import com.olujobii.employeemanagementsystem.dto.response.ResponseWrapper;
import com.olujobii.employeemanagementsystem.entity.Department;
import com.olujobii.employeemanagementsystem.exception.DepartmentException;
import com.olujobii.employeemanagementsystem.exception.DuplicateDepartmentException;
import com.olujobii.employeemanagementsystem.exception.InvalidActiveStateException;
import com.olujobii.employeemanagementsystem.exception.ResourceNotFoundException;
import com.olujobii.employeemanagementsystem.repository.DepartmentRepository;
import com.olujobii.employeemanagementsystem.repository.EmployeeRepository;
import com.olujobii.employeemanagementsystem.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public ResponseWrapper<List<DepartmentResponseDTO>> getAllDepartments() {
        List<DepartmentResponseDTO> departments = departmentRepository.findAll()
                .stream().map(this::toDepartmentResponseDTO).toList();

        return ResponseWrapper.<List<DepartmentResponseDTO>>builder()
                .data(departments)
                .message("All Departments")
                .statusCode(HttpStatusCode.valueOf(HttpStatus.OK.value()))
                .build();
    }

    @Override
    public void createDepartment(@Valid DepartmentRequestDTO payload) {
        String departmentName = payload.departmentName().trim();
        Boolean isAcceptingIntern = payload.isAcceptingIntern();
        Boolean isActive = payload.isActive();

        //Check if department is present already
        departmentRepository.findByDepartmentNameIgnoreCase(departmentName)
                .ifPresent(dept -> {
                    throw new DuplicateDepartmentException("Duplicate department",HttpStatusCode.valueOf(HttpStatus.CONFLICT.value()));
                });

        departmentRepository.save(toDepartmentEntity(departmentName,isAcceptingIntern,isActive));
    }

    @Override
    public ResponseWrapper<DepartmentResponseDTO> getDepartment(Long id) {
        Department department = fetchDepartmentById(id);

        return ResponseWrapper.<DepartmentResponseDTO>builder()
                .data(toDepartmentResponseDTO(department))
                .message("Department fetched")
                .statusCode(HttpStatusCode.valueOf(HttpStatus.OK.value()))
                .build();
    }

    @Override
    public ResponseWrapper<DepartmentResponseDTO> updateDepartment(Long id, @Valid DepartmentRequestDTO payload) {
        Department department = fetchDepartmentById(id);

        String departmentName = payload.departmentName().trim();
        Boolean isAcceptingIntern = payload.isAcceptingIntern();
        Boolean isActive = payload.isActive();

        //Is departmentName present?
        departmentRepository.findByDepartmentNameIgnoreCaseAndNotId(id,departmentName).ifPresent(dept -> {
            throw new DuplicateDepartmentException("Duplicate department", HttpStatusCode.valueOf(HttpStatus.CONFLICT.value()));
        });

        department.setDepartmentName(departmentName);
        department.setIsAcceptingIntern(isAcceptingIntern);
        department.setIsActive(isActive);

        department = departmentRepository.save(department);
        return ResponseWrapper.<DepartmentResponseDTO>builder()
                .data(toDepartmentResponseDTO(department))
                .message("Department updated")
                .statusCode(HttpStatusCode.valueOf(HttpStatus.OK.value()))
                .build();
    }

    @Override
    public void softDeleteDepartment(Long id) {
        Department department = fetchDepartmentById(id);

        Integer employeeCount = employeeRepository.countByDepartment(department);

        if (employeeCount > 0)
            throw new DepartmentException("Department cannot be made inactive because employees are there", HttpStatusCode.valueOf(HttpStatus.CONFLICT.value()));

        department.setIsActive(false);

        departmentRepository.save(department);
    }

    @Override
    public void hardDeleteDepartment(Long id) {
        Department department = fetchDepartmentById(id);

        if(department.getIsActive()) throw new InvalidActiveStateException("Department must not be active", HttpStatusCode.valueOf(HttpStatus.CONFLICT.value()));

        departmentRepository.delete(department);
    }

    private Department fetchDepartmentById(Long id){
        return departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id "+id, HttpStatusCode.valueOf(HttpStatus.NOT_FOUND.value())));
    }


    private DepartmentResponseDTO toDepartmentResponseDTO(Department department){
        return new DepartmentResponseDTO(department.getId(),
                                        department.getDepartmentName(),
                                        department.getIsAcceptingIntern(),
                                        department.getIsActive());
    }

    private Department toDepartmentEntity(String departmentName, Boolean isAcceptingIntern, Boolean isActive){
        return Department.builder()
                .departmentName(departmentName)
                .isAcceptingIntern(isAcceptingIntern)
                .isActive(isActive)
                .build();
    }
}
