package com.olujobii.employeerestapi.employee.service;

import com.olujobii.employeerestapi.employee.dto.request.EmployeePatchRequestDto;
import com.olujobii.employeerestapi.employee.dto.request.EmployeeRequestDto;
import com.olujobii.employeerestapi.employee.dto.response.EmployeeResponseDto;
import com.olujobii.employeerestapi.employee.dto.response.ImportResultDto;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


public interface EmployeeService {
    void createEmployee(@Valid EmployeeRequestDto employeeRequestDto);

    List<EmployeeResponseDto> getEmployees();

     EmployeeResponseDto getEmployeeById(Long id);

    void updateEmployeeData(Long id, @Valid EmployeeRequestDto employeeRequestDto);

    void updateSpecificEmployeeData(Long id, EmployeePatchRequestDto employeePatchRequestDto);

    void softDeleteEmployee(Long id);

    void hardDeleteEmployee(Long id);

    ImportResultDto importEmployeeData(MultipartFile file) throws IOException;
}
