package com.olujobii.employeemanagementsystem.controller;

import com.olujobii.employeemanagementsystem.dto.request.EmployeeRequestDTO;
import com.olujobii.employeemanagementsystem.dto.response.EmployeeResponseDTO;
import com.olujobii.employeemanagementsystem.dto.response.ResponseWrapper;
import com.olujobii.employeemanagementsystem.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<ResponseWrapper<List<EmployeeResponseDTO>>> getAllEmployees(@RequestParam(defaultValue = "0") Integer page,
                                                                                      @RequestParam(defaultValue = "10") Integer size,
                                                                                      @RequestParam(defaultValue = "ASC") String sort){
        return ResponseEntity.ok(employeeService.getAllEmployees(page, size, sort));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseWrapper<EmployeeResponseDTO>> getEmployee(@PathVariable Long id){
        return ResponseEntity.ok(employeeService.getEmployee(id));
    }

    @PostMapping
    public ResponseEntity<Void> createEmployee(@RequestBody @Valid EmployeeRequestDTO payload){
        employeeService.createEmployee(payload);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseWrapper<EmployeeResponseDTO>> updateEmployee(@PathVariable Long id, @RequestBody @Valid EmployeeRequestDTO payload){
        return ResponseEntity.ok(employeeService.updateEmployee(id, payload));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDeleteEmployee(@PathVariable Long id){
        employeeService.softDeleteEmployee(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}/hard")
    public ResponseEntity<Void> hardDeleteEmployee(@PathVariable Long id){
        employeeService.hardDeleteEmployee(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/salary-range")
    public ResponseEntity<ResponseWrapper<List<EmployeeResponseDTO>>> getEmployeeBySalaryRange(@RequestParam BigDecimal min,
                                                                                               @RequestParam BigDecimal max){
        return ResponseEntity.ok(employeeService.fetchEmployeeBySalaryRange(min,max));
    }
}
