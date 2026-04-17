package com.olujobii.employeerestapi.employee.mapper;

import com.olujobii.employeerestapi.department.entity.Department;
import com.olujobii.employeerestapi.employee.dto.EmployeeRequestDto;
import com.olujobii.employeerestapi.employee.entity.Employee;

public class EmployeeMapper {

    public static Employee toEmployeeEntity(EmployeeRequestDto employeeRequestDto, Department department){
        return Employee.builder()
                .firstName(employeeRequestDto.firstName().trim())
                .lastName(employeeRequestDto.lastName().trim())
                .email(employeeRequestDto.email().trim().toLowerCase())
                .department(department)
                .salary(employeeRequestDto.salary())
                .dateOfJoining(employeeRequestDto.dateOfJoining())
                .active(employeeRequestDto.active())
                .isAnIntern(employeeRequestDto.isAnIntern())
                .build();
    }
}
