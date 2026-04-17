package com.olujobii.employeerestapi.employee.service.impl;

import com.olujobii.employeerestapi.department.entity.Department;
import com.olujobii.employeerestapi.department.service.DepartmentService;
import com.olujobii.employeerestapi.employee.dto.EmployeePatchRequestDto;
import com.olujobii.employeerestapi.employee.dto.EmployeeRequestDto;
import com.olujobii.employeerestapi.employee.dto.EmployeeResponseDto;
import com.olujobii.employeerestapi.employee.entity.Employee;
import com.olujobii.employeerestapi.employee.mapper.EmployeeMapper;
import com.olujobii.employeerestapi.employee.mapper.EmployeeResponseMapper;
import com.olujobii.employeerestapi.employee.repository.EmployeeRepository;
import com.olujobii.employeerestapi.employee.service.EmployeeService;
import com.olujobii.employeerestapi.exception.*;
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
    private final EmployeeRepository employeeRepository;
    private final DepartmentService departmentService;

    @Override
    public void createEmployee(@Valid EmployeeRequestDto employeeRequestDto){
        employeeRepository.findByEmail(employeeRequestDto.email().trim().toLowerCase())
                .ifPresent(employee -> {
                    throw new DuplicateEmailException("Email already exists");
                });

        //Checking if department exists and if department accepts intern
        Department department = departmentService.getDepartmentById(employeeRequestDto.departmentId());

        if(!validateInternAcceptance(employeeRequestDto,department))
            throw new RuntimeException("Department does not accept intern");

        //Validation salary cap for interns and non-interns
        validateSalary(employeeRequestDto);

        //Map Request DTO to employee data
        Employee employee = EmployeeMapper.toEmployeeEntity(employeeRequestDto,department);

        employeeRepository.save(employee);
    }

    @Override
    public List<EmployeeResponseDto> getEmployees(){
        //Find all Employees, Map employees to EmployeeResponseDto and returning list of EmployeeResponseDto object
        return employeeRepository.findAll().stream().map(employee -> EmployeeResponseMapper.toEmployeeResponseDto(employee.getId(),
                employee.getFirstName(),employee.getLastName(),employee.getEmail(),employee.getDepartment().getDepartmentName(),employee.getSalary(),
                employee.getDateOfJoining(),employee.getActive(),employee.getIsAnIntern())
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
                employee.getFirstName(),employee.getLastName(),employee.getEmail(),employee.getDepartment().getDepartmentName(),employee.getSalary(),
                employee.getDateOfJoining(),employee.getActive(),employee.getIsAnIntern());
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

        //Check if department exists and if department accepts intern
        Department department = departmentService.getDepartmentById(employeeRequestDto.departmentId());

        if(!validateInternAcceptance(employeeRequestDto,department))
            throw new RuntimeException("Department does not accept intern");

        //Validate salary
        validateSalary(employeeRequestDto);

        employee.setFirstName(employeeRequestDto.firstName().trim());
        employee.setLastName(employeeRequestDto.lastName().trim());
        employee.setDepartment(department);
        employee.setEmail(employeeRequestDto.email().trim().toLowerCase());
        employee.setSalary(employeeRequestDto.salary());
        employee.setDateOfJoining(employeeRequestDto.dateOfJoining());
        employee.setActive(employeeRequestDto.active());
        employee.setIsAnIntern(employeeRequestDto.isAnIntern());

        employeeRepository.save(employee);
    }

    @Override
    public void updateSpecificEmployeeData(Long id, EmployeePatchRequestDto employeePatchRequestDto) {
        Employee employee = employeeRepository.findById(id)
                //FIXME: Saying Throwable Supplier does not return any exception
                .orElseThrow(() -> {
                    throw new EmployeeNotFoundException("Employee does not exist");
                });

        if(employeePatchRequestDto.salary() == null && employeePatchRequestDto.departmentId() == null
                && employeePatchRequestDto.active() == null)
            throw new InvalidPatchRequestBodyException("Only departmentId, salary or " +
                    "active fields can be passed in request body");

        if(employeePatchRequestDto.salary() != null){
            employee.setSalary(employeePatchRequestDto.salary());
        }

        if(employeePatchRequestDto.departmentId() != null){
            Department department = departmentService.getDepartmentById(employeePatchRequestDto.departmentId());
            employee.setDepartment(department);
        }

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

    //TODO: Add to updateEmployeeData(In case status is changed to employee is an intern in the future)
    private boolean validateInternAcceptance(EmployeeRequestDto employeeRequestDto, Department department){
        if(employeeRequestDto.isAnIntern() && !department.getIsAcceptingIntern())
            return false;

        return true;
    }

    private void validateSalary(EmployeeRequestDto employeeRequestDto){
        BigDecimal employeeSalary = employeeRequestDto.salary();
        BigDecimal minimumInternSalary = new BigDecimal(15_000);
        BigDecimal minimumNonInternSalary = new BigDecimal(30_000);

        if(employeeRequestDto.isAnIntern() && employeeSalary.compareTo(minimumInternSalary) < 0)
            throw new InsufficientSalaryException("Minimum intern salary is 15,000");

        if(!employeeRequestDto.isAnIntern() && employeeSalary.compareTo(minimumNonInternSalary) < 0)
            throw new InsufficientSalaryException("Minimum non intern salary is 30,000");
    }
}
