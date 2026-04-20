package com.olujobii.employeerestapi.employee.service;

import com.olujobii.employeerestapi.employee.dto.response.ImportResultDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface EmployeeImportExportService {

    ImportResultDto importEmployeeData(MultipartFile file) throws IOException;
}
