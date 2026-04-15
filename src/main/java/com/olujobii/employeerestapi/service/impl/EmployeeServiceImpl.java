package com.olujobii.employeerestapi.service.impl;

import com.olujobii.employeerestapi.dto.EmployeeRequestDto;
import com.olujobii.employeerestapi.entity.Employee;
import com.olujobii.employeerestapi.exception.DuplicateEmailException;
import com.olujobii.employeerestapi.exception.EmployeeNotFoundException;
import com.olujobii.employeerestapi.exception.InvalidSalaryException;
import com.olujobii.employeerestapi.mapper.EmployeeMapper;
import com.olujobii.employeerestapi.repository.EmployeeRepository;
import com.olujobii.employeerestapi.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
@Validated
public class EmployeeServiceImpl implements EmployeeService {
    private EmployeeRepository employeeRepository;

    @Override
    public void createEmployee(@Valid EmployeeRequestDto employeeRequestDto){
        //Check if employee ID exists
        employeeRepository.findByEmail(employeeRequestDto.email().trim())
                .ifPresent(employee -> {
                    throw new DuplicateEmailException("Email already exists");
                });

        validateSalary(employeeRequestDto);

        //Map Request DTO to employee data
        Employee employee = EmployeeMapper.toEmployeeEntity(employeeRequestDto);

        employeeRepository.save(employee);
    }

    @Override
    public List<Employee> getEmployees(){
        return employeeRepository.findAll();
    }

    @Override
    public Employee getEmployeeById(Long id){
        return employeeRepository.findById(id)
                .orElseThrow(() -> {
                    throw new EmployeeNotFoundException("Employee does not exist");
                });
    }

    private void validateSalary(EmployeeRequestDto employeeRequestDto){
        String employeeDepartment = employeeRequestDto.department().trim();
        BigDecimal employeeSalary = employeeRequestDto.salary();
        BigDecimal salaryForIntern = new BigDecimal("15000.00");
        BigDecimal salaryForOtherDepartment = new BigDecimal("30000.00");

        if(employeeDepartment.trim().equalsIgnoreCase("intern") && employeeSalary.compareTo(salaryForIntern) < 0)
            throw new InvalidSalaryException("Intern salary cannot be less than 15,000");

        if(!employeeDepartment.trim().equalsIgnoreCase("intern") &&  employeeSalary.compareTo(salaryForOtherDepartment) < 0)
            throw new InvalidSalaryException("Employee salary cannot be less than 30,000");
    }
}
