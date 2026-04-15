package com.olujobii.employeerestapi.employee.service.impl;

import com.olujobii.employeerestapi.employee.dto.EmployeePatchRequestDto;
import com.olujobii.employeerestapi.employee.dto.EmployeeRequestDto;
import com.olujobii.employeerestapi.employee.dto.EmployeeResponseDto;
import com.olujobii.employeerestapi.employee.entity.Employee;
import com.olujobii.employeerestapi.employee.exception.*;
import com.olujobii.employeerestapi.employee.exception.*;
import com.olujobii.employeerestapi.employee.mapper.EmployeeMapper;
import com.olujobii.employeerestapi.employee.mapper.EmployeeResponseMapper;
import com.olujobii.employeerestapi.employee.repository.EmployeeRepository;
import com.olujobii.employeerestapi.employee.service.EmployeeService;
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
        employeeRepository.findByEmail(employeeRequestDto.email().trim().toLowerCase())
                .ifPresent(employee -> {
                    throw new DuplicateEmailException("Email already exists");
                });

        validateSalary(employeeRequestDto);

        //Map Request DTO to employee data
        Employee employee = EmployeeMapper.toEmployeeEntity(employeeRequestDto);

        employeeRepository.save(employee);
    }

    @Override
    public List<EmployeeResponseDto> getEmployees(){
        List<Employee> employees = employeeRepository.findAll();

        //Map employees to EmployeeResponseDto and returning list of EmployeeResponseDto object
        return employees.stream().map(employee -> EmployeeResponseMapper.toEmployeeResponseDto(employee.getId(),
                employee.getFirstName(),employee.getLastName(),employee.getEmail(),employee.getDepartment(),employee.getSalary(),
                employee.getDateOfJoining(),employee.getActive())
        ).toList();
    }

    @Override
    public EmployeeResponseDto getEmployeeById(Long id){
        Employee employee = employeeRepository.findById(id)
                //FIXME: Saying Throwable Supplier does not return any exception
                .orElseThrow(() -> {
                    throw new EmployeeNotFoundException("Employee does not exist");
                });

        return EmployeeResponseMapper.toEmployeeResponseDto(employee.getId(),
                employee.getFirstName(),employee.getLastName(),employee.getEmail(),employee.getDepartment(),employee.getSalary(),
                employee.getDateOfJoining(),employee.getActive());
    }

    @Override
    public void updateEmployeeData(Long id,@Valid EmployeeRequestDto employeeRequestDto) {
        Employee employee = employeeRepository.findById(id)
                //FIXME: Saying Throwable Supplier does not return any exception
                .orElseThrow(() -> {
                    throw new EmployeeNotFoundException("Employee does not exist");
                });

        //Check if email exists and skip the id of the current record I want to update
        employeeRepository.findByEmailWhereIdIsNotEqualTo(id,
                employeeRequestDto.email().trim().toLowerCase())
                        .ifPresent(emp -> {
                            //FIXME: Saying Throwable Supplier does not return any exception
                            throw new DuplicateEmailException("Email already exist");
                        });

        employee.setFirstName(employeeRequestDto.firstName().trim());
        employee.setLastName(employeeRequestDto.lastName().trim());
        employee.setDepartment(employeeRequestDto.department().trim().toUpperCase());
        employee.setEmail(employeeRequestDto.email().trim().toLowerCase());
        employee.setSalary(employeeRequestDto.salary());
        employee.setDateOfJoining(employeeRequestDto.dateOfJoining());
        employee.setActive(employeeRequestDto.active());

        employeeRepository.save(employee);
    }

    @Override
    public void updateSpecificEmployeeData(Long id, EmployeePatchRequestDto employeePatchRequestDto) {
        Employee employee = employeeRepository.findById(id)
                //FIXME: Saying Throwable Supplier does not return any exception
                .orElseThrow(() -> {
                    throw new EmployeeNotFoundException("Employee does not exist");
                });

        if(employeePatchRequestDto.salary() == null && employeePatchRequestDto.department() == null
                && employeePatchRequestDto.active() == null)
            throw new InvalidPatchRequestBodyException("Only department, salary or " +
                    "active fields can be passed in request body");

        if(employeePatchRequestDto.salary() != null)
            employee.setSalary(employeePatchRequestDto.salary());

        if(employeePatchRequestDto.department() != null)
            employee.setDepartment(employeePatchRequestDto.department().trim().toUpperCase());

        if(employeePatchRequestDto.active() != null)
            employee.setActive(employeePatchRequestDto.active());

        employeeRepository.save(employee);
    }

    @Override
    public void softDeleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> {
            //FIXME: Saying Throwable Supplier does not return any exception
            throw new EmployeeNotFoundException("Employee does not exist");
        });

        if(!employee.getActive())
            return;

        employee.setActive(false);
        employeeRepository.save(employee);
    }

    @Override
    public void hardDeleteEmployee(Long id){
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> {
            throw new EmployeeNotFoundException("Employee does not exist");
        });

        if(employee.getActive())
            throw new InvalidActiveFieldException("Cannot hard delete an active employee");

        employeeRepository.deleteById(id);
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
