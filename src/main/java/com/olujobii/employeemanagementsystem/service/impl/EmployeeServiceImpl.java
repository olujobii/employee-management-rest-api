package com.olujobii.employeemanagementsystem.service.impl;

import com.olujobii.employeemanagementsystem.dto.request.EmployeeRequestDTO;
import com.olujobii.employeemanagementsystem.dto.response.EmployeeResponseDTO;
import com.olujobii.employeemanagementsystem.dto.response.ResponseWrapper;
import com.olujobii.employeemanagementsystem.entity.Department;
import com.olujobii.employeemanagementsystem.entity.Employee;
import com.olujobii.employeemanagementsystem.exception.*;
import com.olujobii.employeemanagementsystem.repository.DepartmentRepository;
import com.olujobii.employeemanagementsystem.repository.EmployeeRepository;
import com.olujobii.employeemanagementsystem.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    @Override
    public ResponseWrapper<List<EmployeeResponseDTO>> getAllEmployees(Integer page, Integer size, String sort) {
        if(page < 0) throw new EmployeeException("Page number cannot be less than 0", HttpStatusCode.valueOf(HttpStatus.UNPROCESSABLE_CONTENT.value()));
        if(size < 10) throw new EmployeeException("Page Size cannot be less than 10", HttpStatusCode.valueOf(HttpStatus.UNPROCESSABLE_CONTENT.value()));

        Sort sorts =sortingLogic(sort);
        Pageable pageable = PageRequest.of(page,size,sorts);

        List<EmployeeResponseDTO> employees = employeeRepository.findAll(pageable).stream()
                .map(this::toEmployeeResponseDTO).toList();

        return ResponseWrapper.<List<EmployeeResponseDTO>>builder()
                .data(employees)
                .message("All Employees")
                .statusCode(HttpStatusCode.valueOf(HttpStatus.OK.value()))
                .build();
    }

    @Override
    public ResponseWrapper<EmployeeResponseDTO> getEmployee(Long id) {
        Employee employee = fetchEmployeeById(id);

        return ResponseWrapper.<EmployeeResponseDTO>builder()
                .data(toEmployeeResponseDTO(employee))
                .message("Employee fetched")
                .statusCode(HttpStatusCode.valueOf(HttpStatus.OK.value()))
                .build();
    }

    @Override
    public void createEmployee(@Valid EmployeeRequestDTO payload) {
        String payloadFirstName = payload.firstName().trim();
        String payloadLastName = payload.lastName().trim();
        String payloadEmail = payload.email().trim();
        Long payloadDepartmentId = payload.departmentId();
        BigDecimal payloadSalary = payload.salary();
        LocalDate payloadDateOfJoining = payload.dateOfJoining();
        Boolean payloadActive = payload.active();
        Boolean payloadIsAnIntern = payload.isAnIntern();

        //Checking for duplicate emails
        employeeRepository.findByEmailIgnoreCase(payloadEmail)
                .ifPresent(emp -> {
                    throw new DuplicateEmailException("Employee email already exists", HttpStatusCode.valueOf(HttpStatus.CONFLICT.value()));
                });

        //Checking if department id exists
        Department department = departmentRepository.findById(payloadDepartmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id "+payloadDepartmentId, HttpStatusCode.valueOf(HttpStatus.NOT_FOUND.value())));

        //Checking if department is active
        if(!department.getIsActive())
            throw new DepartmentException("Department with id "+payloadDepartmentId+" is currently inactive", HttpStatusCode.valueOf(HttpStatus.UNPROCESSABLE_CONTENT.value()));

        //Checking if department accepts interns
        if(payloadIsAnIntern && !department.getIsAcceptingIntern())
            throw new EmployeeException("Department does not currently accept interns", HttpStatusCode.valueOf(HttpStatus.UNPROCESSABLE_CONTENT.value()));

        //Salary threshold for interns and full-time staffs
        if(validateSalary(payloadSalary, payloadIsAnIntern)) {
            String employeeRole = payloadIsAnIntern ? "Intern" : "Full-Time employee";
            long salaryIntern = 15000;
            long salaryFullTime = 30000;
            String showSalary = payloadIsAnIntern ? String.valueOf(salaryIntern) : String.valueOf(salaryFullTime);
            throw new EmployeeException("Minimum salary for "+employeeRole+" is "+showSalary, HttpStatusCode.valueOf(HttpStatus.UNPROCESSABLE_CONTENT.value()));
        }

        //Save to database
        Employee employee = toEmployeeEntity(payloadFirstName,payloadLastName,payloadEmail,department,payloadSalary,payloadDateOfJoining,payloadActive,
                payloadIsAnIntern);

        employeeRepository.save(employee);
    }

    @Override
    public ResponseWrapper<EmployeeResponseDTO> updateEmployee(Long id,@Valid EmployeeRequestDTO payload) {
        //Check if employee ID exists
        Employee employee = fetchEmployeeById(id);

        String payloadFirstName = payload.firstName().trim();
        String payloadLastName = payload.lastName().trim();
        String payloadEmail = payload.email().trim();
        Long payloadDepartmentId = payload.departmentId();
        BigDecimal payloadSalary = payload.salary();
        LocalDate payloadDateOfJoining = payload.dateOfJoining();
        Boolean payloadActive = payload.active();
        Boolean payloadIsAnIntern = payload.isAnIntern();

        //Checking for duplicate emails
        employeeRepository.findByEmailIgnoreCaseAndNotId(id, payloadEmail)
                .ifPresent(emp -> {
                    throw new DuplicateEmailException("Employee email already exists", HttpStatusCode.valueOf(HttpStatus.CONFLICT.value()));
                });

        //Checking if department id exists
        Department department = departmentRepository.findById(payloadDepartmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id "+payloadDepartmentId, HttpStatusCode.valueOf(HttpStatus.NOT_FOUND.value())));

        //Checking if department is active
        if(!department.getIsActive())
            throw new DepartmentException("Department with id "+payloadDepartmentId+" is currently inactive", HttpStatusCode.valueOf(HttpStatus.UNPROCESSABLE_CONTENT.value()));

        //Checking if department accepts interns
        if(payloadIsAnIntern && !department.getIsAcceptingIntern())
            throw new EmployeeException("Department does not currently accept interns", HttpStatusCode.valueOf(HttpStatus.UNPROCESSABLE_CONTENT.value()));

        //Salary threshold for interns and full-time staffs
        if(validateSalary(payloadSalary, payloadIsAnIntern)) {
            String employeeRole = payloadIsAnIntern ? "Intern" : "Full-Time employee";
            long salaryIntern = 15000;
            long salaryFullTime = 30000;
            String showSalary = payloadIsAnIntern ? String.valueOf(salaryIntern) : String.valueOf(salaryFullTime);
            throw new EmployeeException("Minimum salary for "+employeeRole+" is "+showSalary, HttpStatusCode.valueOf(HttpStatus.UNPROCESSABLE_CONTENT.value()));
        }

        //Updating employee and saving
        employee.setFirstName(payloadFirstName);
        employee.setLastName(payloadLastName);
        employee.setEmail(payloadEmail);
        employee.setDepartment(department);
        employee.setSalary(payloadSalary);
        employee.setDateOfJoining(payloadDateOfJoining);
        employee.setActive(payloadActive);
        employee.setIsAnIntern(payloadIsAnIntern);

        employee = employeeRepository.save(employee);

        return ResponseWrapper.<EmployeeResponseDTO>builder()
                .data(toEmployeeResponseDTO(employee))
                .message("Employee updated")
                .statusCode(HttpStatusCode.valueOf(HttpStatus.OK.value()))
                .build();
    }

    @Override
    public void softDeleteEmployee(Long id) {
        Employee employee = fetchEmployeeById(id);

        employee.setActive(false);
        employeeRepository.save(employee);
    }

    @Override
    public void hardDeleteEmployee(Long id) {
        Employee employee = fetchEmployeeById(id);

        if(employee.getActive()) throw new InvalidActiveStateException("Employee must not be active", HttpStatusCode.valueOf(HttpStatus.CONFLICT.value()));

        employeeRepository.delete(employee);
    }

    @Override
    public ResponseWrapper<List<EmployeeResponseDTO>> fetchEmployeeBySalaryRange(BigDecimal min, BigDecimal max) {
        List<EmployeeResponseDTO> employees = employeeRepository.findBySalaryRange(min, max)
                .stream().map(this::toEmployeeResponseDTO).toList();

        return ResponseWrapper.<List<EmployeeResponseDTO>>builder()
                .data(employees)
                .message("Employees fetched")
                .statusCode(HttpStatusCode.valueOf(HttpStatus.OK.value()))
                .build();
    }

    private Employee fetchEmployeeById(Long id){
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee does not exist with id "+id, HttpStatusCode.valueOf(HttpStatus.NO_CONTENT.value())));
    }

    private boolean validateSalary(BigDecimal payloadSalary, boolean payloadIsAnIntern){
        BigDecimal minimumSalaryForInterns = BigDecimal.valueOf(15000);
        BigDecimal minimumSalaryForFullTime = BigDecimal.valueOf(30000);

        return (payloadIsAnIntern && minimumSalaryForInterns.compareTo(payloadSalary) > 0)
                || (!payloadIsAnIntern && minimumSalaryForFullTime.compareTo(payloadSalary) > 0);
    }

    private EmployeeResponseDTO toEmployeeResponseDTO(Employee employee){
        return new EmployeeResponseDTO(employee.getId(), employee.getFirstName(), employee.getLastName(), employee.getEmail(),
                employee.getDepartment().getDepartmentName(), employee.getSalary(), employee.getDateOfJoining(),
                employee.getActive(),employee.getIsAnIntern());
    }

    private Employee toEmployeeEntity(String firstName, String lastName, String email, Department department, BigDecimal salary,
                                      LocalDate dateOfJoining, boolean active, boolean isAnIntern){
        return Employee.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .department(department)
                .salary(salary)
                .dateOfJoining(dateOfJoining)
                .active(active)
                .isAnIntern(isAnIntern)
                .build();
    }

    private Sort sortingLogic(String sort){
        if(sort.equalsIgnoreCase("ASC"))
            return Sort.by(Sort.Direction.ASC, "id");
        else if(sort.equalsIgnoreCase("DESC"))
            return Sort.by(Sort.Direction.DESC, "id");
        else
            throw new EmployeeException("Invalid sort param", HttpStatusCode.valueOf(HttpStatus.UNPROCESSABLE_CONTENT.value()));
    }
}
