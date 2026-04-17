package com.olujobii.employeerestapi.department.controller;

import com.olujobii.employeerestapi.department.dto.request.DepartmentRequestDto;
import com.olujobii.employeerestapi.department.entity.Department;
import com.olujobii.employeerestapi.department.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/departments")
class DepartmentController {

    private final DepartmentService departmentService;


    @PostMapping
    public ResponseEntity<Void> createDepartment(@Valid @RequestBody DepartmentRequestDto departmentRequestDto){
        departmentService.createDepartment(departmentRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Department> getDepartmentById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(departmentService.getDepartmentById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id){
        departmentService.deleteDepartment(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
