package com.olujobii.employeemanagementsystem.controller;

import com.olujobii.employeemanagementsystem.dto.request.DepartmentRequestDTO;
import com.olujobii.employeemanagementsystem.dto.response.DepartmentResponseDTO;
import com.olujobii.employeemanagementsystem.dto.response.ResponseWrapper;
import com.olujobii.employeemanagementsystem.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    @GetMapping
    public ResponseEntity<ResponseWrapper<List<DepartmentResponseDTO>>> getAllDepartments(){
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper<DepartmentResponseDTO>> createDepartment(@Valid @RequestBody DepartmentRequestDTO payload){
        return ResponseEntity.status(HttpStatus.CREATED).body(departmentService.createDepartment(payload));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseWrapper<DepartmentResponseDTO>> getDepartment(@PathVariable Long id){
        return ResponseEntity.ok(departmentService.getDepartment(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseWrapper<DepartmentResponseDTO>> updateDepartment(@PathVariable Long id, @RequestBody DepartmentRequestDTO payload){
        return ResponseEntity.ok(departmentService.updateDepartment(id, payload));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDeleteDepartment(@PathVariable Long id){
        departmentService.softDeleteDepartment(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}/hard")
    public ResponseEntity<Void> hardDeleteDepartment(@PathVariable Long id){
        departmentService.hardDeleteDepartment(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
