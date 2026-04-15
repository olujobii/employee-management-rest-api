package com.olujobii.employeerestapi.employee.mapper;

import com.olujobii.employeerestapi.employee.dto.EmployeeRequestDto;
import com.olujobii.employeerestapi.employee.entity.Employee;

public class EmployeeMapper {

    public static Employee toEmployeeEntity(EmployeeRequestDto employeeRequestDto){
        return new Employee(
                employeeRequestDto.firstName().trim(),
                employeeRequestDto.lastName().trim(),
                employeeRequestDto.email().trim().toLowerCase(),
                employeeRequestDto.department().trim().toUpperCase(),
                employeeRequestDto.salary(),
                employeeRequestDto.dateOfJoining(),
                employeeRequestDto.active()
        );
    }
}
