package com.olujobii.employeerestapi.employee.service.impl;

import com.olujobii.employeerestapi.department.entity.Department;
import com.olujobii.employeerestapi.department.service.DepartmentService;
import com.olujobii.employeerestapi.employee.dto.request.EmployeePatchRequestDto;
import com.olujobii.employeerestapi.employee.dto.request.EmployeeRequestDto;
import com.olujobii.employeerestapi.employee.dto.response.EmployeeResponseDto;
import com.olujobii.employeerestapi.employee.entity.Employee;
import com.olujobii.employeerestapi.employee.repository.EmployeeRepository;
import com.olujobii.employeerestapi.employee.service.EmployeeService;
import com.olujobii.employeerestapi.exception.*;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.*;

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
                    throw new DuplicateEmailException("Email already exists", HttpStatus.CONFLICT);
                });

        //Checking if department exists
        Department department = departmentService.searchDepartmentByName(employeeRequestDto.departmentName().trim());

        if(validateInternAcceptance(employeeRequestDto,department))
            throw new EmployeeException("Department is not currently accepting interns", HttpStatus.BAD_REQUEST);

        //Validation salary cap for interns and non-interns
        validateSalary(employeeRequestDto.salary(), employeeRequestDto.isAnIntern());

        //Map Request DTO to employee data
        Employee employee = mapToEmployeeEntity(employeeRequestDto,department);

        employeeRepository.save(employee);
    }

    @Override
    public List<EmployeeResponseDto> getEmployees(int pageNo, int pageSize, String sortBy, String sortDir, Boolean isActive){
        final int minimumPageNo = 1;
        final int minimumPageSize = 5;
        final String sortByField = sortBy.trim();
        final String sortDirField = sortDir.trim().toUpperCase();

        //check page size and page number is valid
        if(pageNo < minimumPageNo) throw new EmployeeException("Invalid page number. Page number cannot be less than "+minimumPageNo, HttpStatus.BAD_REQUEST);

        if(pageSize < minimumPageSize) throw new EmployeeException("Invalid page size. Page size cannot be less than "+minimumPageSize, HttpStatus.BAD_REQUEST);

        final int pageNoIndex = pageNo - 1;

        //SORTING LOGIC
        //Check if sortByField and SortDirField is valid
        if(isSortFieldNotValid(sortByField))
            throw new EmployeeException("Invalid sorting field: "+ sortByField,HttpStatus.BAD_REQUEST);

        if(isSortDirFieldNotValid(sortDirField))
            throw new EmployeeException("Invalid sorting order: "+sortDirField,HttpStatus.BAD_REQUEST);


        Sort sort = sortDirField.equals("ASC") ? Sort.by(sortByField).ascending() : Sort.by(sortByField).descending();

        Pageable pageable = PageRequest.of(pageNoIndex,pageSize,sort);

        //Filtering logic
        if(isActive != null && !isActive.equals(true)){
            throw new EmployeeException("You can only search for active employees",HttpStatus.BAD_REQUEST);
        }

        if(isActive != null)
            return employeeRepository.findByActiveTrue(true,pageable).stream().map(this::mapToEmployeeResponseDto)
                    .toList();

        return employeeRepository.findAll(pageable).stream().map(this::mapToEmployeeResponseDto)
                .toList();
    }

    private boolean isSortFieldNotValid(String sortByField) {
        Set<String> allowedSortByFields = Set.of("id","firstName","lastName","salary","dateOfJoining");

        return !allowedSortByFields.contains(sortByField);
    }

    private boolean isSortDirFieldNotValid(String sortDirField){
        Set<String> allowedSortDirFields = Set.of("ASC","DESC");

        return !allowedSortDirFields.contains(sortDirField);

    }

    @Override
    public EmployeeResponseDto getEmployeeById(Long id){
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id, HttpStatus.NOT_FOUND));

        return mapToEmployeeResponseDto(employee);
    }

    @Override
    public void updateEmployeeData(Long id,@Valid EmployeeRequestDto employeeRequestDto) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id, HttpStatus.NOT_FOUND));

        //Check if email exists and skip the id of the current record I want to update
        employeeRepository.findByEmailWhereIdIsNotEqualTo(id,
                employeeRequestDto.email().trim().toLowerCase())
                        .ifPresent(emp -> {
                            throw new DuplicateEmailException("Email already exist", HttpStatus.CONFLICT);
                        });

        //Check if department exists
        Department department = departmentService.searchDepartmentByName(employeeRequestDto.departmentName().trim());

        if(validateInternAcceptance(employeeRequestDto,department))
            throw new EmployeeException("Department does not accept intern",HttpStatus.BAD_REQUEST);

        //Validate salary
        validateSalary(employeeRequestDto.salary(),employeeRequestDto.isAnIntern());

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
                .orElseThrow(() -> new EmployeeNotFoundException(id, HttpStatus.NOT_FOUND));

        if(employeePatchRequestDto.salary() == null && employeePatchRequestDto.departmentName() == null
                && employeePatchRequestDto.active() == null)
            throw new InvalidEmployeePatchRequestBodyException("Only departmentId, salary or " +
                    "active fields can be passed in request body", HttpStatus.BAD_REQUEST);

        if(employeePatchRequestDto.salary() != null){
            validateSalary(employeePatchRequestDto.salary(),employee.getIsAnIntern());
            employee.setSalary(employeePatchRequestDto.salary());
        }

        if(employeePatchRequestDto.departmentName() != null){
            Department department = departmentService.searchDepartmentByName(employeePatchRequestDto.departmentName().trim());
            employee.setDepartment(department);
        }

        if(employeePatchRequestDto.active() != null)
            employee.setActive(employeePatchRequestDto.active());

        employeeRepository.save(employee);
    }

    @Override
    public void softDeleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id, HttpStatus.NOT_FOUND));

        if(!employee.getActive())
            return;

        employee.setActive(false);
        employeeRepository.save(employee);
    }

    @Override
    public void hardDeleteEmployee(Long id){
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id, HttpStatus.NOT_FOUND));

        if(employee.getActive())
            throw new EmployeeException("Cannot hard delete an active employee",HttpStatus.BAD_REQUEST);

        employeeRepository.deleteById(id);
    }

    @Override
    public List<EmployeeResponseDto> filterBySalaryRange(BigDecimal min, BigDecimal max) {
        return employeeRepository.findBySalaryRange(min,max).stream().map(this::mapToEmployeeResponseDto).toList();
    }


    private boolean validateInternAcceptance(EmployeeRequestDto employeeRequestDto, Department department){
        return employeeRequestDto.isAnIntern() && !department.getIsAcceptingIntern();
    }

    private void validateSalary(BigDecimal employeeSalary, boolean isAnIntern){
        BigDecimal minimumInternSalary = new BigDecimal(15_000);
        BigDecimal minimumNonInternSalary = new BigDecimal(30_000);

        if(isAnIntern && employeeSalary.compareTo(minimumInternSalary) < 0)
            throw new EmployeeException("Minimum intern salary is 15,000", HttpStatus.BAD_REQUEST);

        if(!isAnIntern && employeeSalary.compareTo(minimumNonInternSalary) < 0)
            throw new EmployeeException("Minimum non intern salary is 30,000",HttpStatus.BAD_REQUEST);
    }

    private EmployeeResponseDto mapToEmployeeResponseDto(Employee employee){
        return new EmployeeResponseDto(employee.getId(),employee.getFirstName(),employee.getLastName(),
                employee.getEmail(),employee.getDepartment().getDepartmentName(),employee.getSalary(),
                employee.getDateOfJoining(), employee.getActive(), employee.getIsAnIntern());
    }

    private Employee mapToEmployeeEntity(EmployeeRequestDto emp, Department department){
        return Employee.builder()
                .firstName(emp.firstName().trim())
                .lastName(emp.lastName().trim())
                .email(emp.email().trim())
                .department(department)
                .salary(emp.salary())
                .dateOfJoining(emp.dateOfJoining())
                .active(emp.active())
                .isAnIntern(emp.isAnIntern())
                .build();
    }

}
