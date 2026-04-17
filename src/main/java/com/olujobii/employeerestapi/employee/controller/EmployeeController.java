package com.olujobii.employeerestapi.employee.controller;

import com.olujobii.employeerestapi.employee.dto.EmployeePatchRequestDto;
import com.olujobii.employeerestapi.employee.dto.EmployeeRequestDto;
import com.olujobii.employeerestapi.employee.dto.EmployeeResponseDto;
import com.olujobii.employeerestapi.employee.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
@AllArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<Void> createEmployee(@Valid @RequestBody EmployeeRequestDto employeeRequestDto){
        employeeService.createEmployee(employeeRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // FIXME: Still going to add query params
    @GetMapping
    public ResponseEntity<List<EmployeeResponseDto>> getEmployees(){
        return ResponseEntity.status(HttpStatus.OK).body(employeeService.getEmployees());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseDto> getEmployeeById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(employeeService.getEmployeeById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateEmployeeData(@PathVariable Long id, @Valid @RequestBody EmployeeRequestDto employeeRequestDto){
        employeeService.updateEmployeeData(id,employeeRequestDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateSpecificEmployeeData(@PathVariable Long id, @RequestBody EmployeePatchRequestDto employeePatchRequestDto){
        employeeService.updateSpecificEmployeeData(id, employeePatchRequestDto);
        return ResponseEntity.status(HttpStatus.OK).build();
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
}
