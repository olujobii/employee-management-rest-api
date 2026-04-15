package com.olujobii.employeerestapi.controller;

import com.olujobii.employeerestapi.dto.EmployeeRequestDto;
import com.olujobii.employeerestapi.entity.Employee;
import com.olujobii.employeerestapi.service.EmployeeService;
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
    private EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<Void> createEmployee(@Valid @RequestBody EmployeeRequestDto employeeRequestDto){
        employeeService.createEmployee(employeeRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // FIXME: Still going to add search params and return list of List<EmployeeResponseDto>
    @GetMapping
    public ResponseEntity<List<Employee>> getEmployees(){
        return ResponseEntity.status(HttpStatus.OK).body(employeeService.getEmployees());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(employeeService.getEmployeeById(id));
    }
}
