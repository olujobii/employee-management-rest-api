package com.olujobii.employeerestapi.employee.service.impl;

import com.olujobii.employeerestapi.employee.dto.request.EmployeeRequestDto;
import com.olujobii.employeerestapi.employee.dto.response.ImportResultDto;
import com.olujobii.employeerestapi.employee.dto.response.ValidationError;
import com.olujobii.employeerestapi.employee.service.EmployeeImportExportService;
import lombok.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
public class ExcelImportExportServiceImpl implements EmployeeImportExportService {

    @Override
    public ImportResultDto importEmployeeData(MultipartFile file) throws IOException {
        //Specified header rows that the Excel file schema must match. If it does not, then the operation fails immediately.
        Set<String> headerColumnNames = Set.of("FIRST_NAME","LAST_NAME","EMAIL","DEPARTMENT_NAME","SALARY","DATE_OF_JOINING","ACTIVE","IS_AN_INTERN");
        List<ValidationError> validationErrors;
        List<ParsedEmployeeRequestDto> parsedEmployeeRequestDtoList;

        try(InputStream inputStream = file.getInputStream();
            Workbook workbook = new XSSFWorkbook(inputStream)){
            Sheet sheet = workbook.getSheetAt(0);

            //Get Sheet names and match them to headerColumnNames to make sure it follows the Excel file schema specified.
            Map<String, Integer> headerRowMap = new HashMap<>();
            Row headerRow = sheet.getRow(0);
            headerRow.forEach(cell -> {
                String headerName = cell.getStringCellValue().trim().toUpperCase();
                headerRowMap.put(headerName,cell.getColumnIndex());
            });

            //Checking header column name and size
            Set<String> importedHeaderColumnNames = headerRowMap.keySet();
            boolean headerSizeNotEqual = isHeaderColumnNameSizeNotEqual(headerColumnNames,importedHeaderColumnNames);
            String headerColumnNameNotValid = isHeaderColumnNameNotValid(headerColumnNames, importedHeaderColumnNames);

            //FIXME: Return custom exception
            if(headerSizeNotEqual) throw new RuntimeException(headerColumnNames.size()+" specific columns is required");

            if(headerColumnNameNotValid != null) throw new RuntimeException(headerColumnNameNotValid+" is not a valid column name");

            //Parse through data in file and record errors
            validationErrors = new LinkedList<>();
            parsedEmployeeRequestDtoList = parseEmployeeDataFromExcelFile(headerRowMap, sheet, validationErrors);

            //Mapping to EmployeeRequestDto
            List<EmployeeRequestDto> employeeRequestDtoList = mapToEmployeeRequestDto(parsedEmployeeRequestDtoList);

            //Performing validation and returning validated employees.
            List<EmployeeRequestDto> finalEmployeeRequestDtoList = validateEmployees(employeeRequestDtoList);
            validationErrors.forEach(System.out::println);
            employeeRequestDtoList.forEach(System.out::println);
        }


        //FIXME: Return correct data
        return new ImportResultDto(1,1,new ArrayList<>());
    }

    private boolean isHeaderColumnNameSizeNotEqual(Set<String> specificHeaderColumnNames,Set<String> importedHeaderColumnNames){
        return specificHeaderColumnNames.size() != importedHeaderColumnNames.size();
    }

    private String isHeaderColumnNameNotValid(Set<String> specificHeaderColumnNames,Set<String> importedHeaderColumnNames){
        for(var headerName : importedHeaderColumnNames){
            if(!specificHeaderColumnNames.contains(headerName))
                return headerName;
        }
        return null;
    }

    private List<ParsedEmployeeRequestDto> parseEmployeeDataFromExcelFile(Map<String,Integer> headerRowMap, Sheet sheet, List<ValidationError> validationErrors) {
        List<ParsedEmployeeRequestDto> parsedEmployeeRequestDtoList = new ArrayList<>();
        sheet.forEach(row -> {
            int rowNum = row.getRowNum();

            //Skip header row
            if(rowNum == 0)
                return;

            ParsedEmployeeRequestDto parsedEmployeeRequestDto = new ParsedEmployeeRequestDto();

            for(Map.Entry<String, Integer> entry : headerRowMap.entrySet()){
                String headerName = entry.getKey();
                Cell cell = row.getCell(entry.getValue(), Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);

                if(cell == null) {
                    validationErrors.add(setValidationError(rowNum,headerName,headerName.toLowerCase()+" is missing"));
                    continue;
                }
                switch(headerName){
                    case "FIRST_NAME":
                        if(cell.getCellType() == CellType.STRING) {
                            parsedEmployeeRequestDto.setFirstName(cell.getStringCellValue().trim());
                        }
                        else {
                            validationErrors.add(setValidationError(rowNum,headerName,"First name should be a string"));
                        }
                        break;
                    case "LAST_NAME":
                        if(cell.getCellType() == CellType.STRING){
                            parsedEmployeeRequestDto.setLastName(cell.getStringCellValue().trim());
                        }
                        else{
                            validationErrors.add(setValidationError(rowNum, headerName,"Last name should be string"));
                        }
                        break;
                    case "EMAIL":
                        if(cell.getCellType() == CellType.STRING){
                            parsedEmployeeRequestDto.setEmail(cell.getStringCellValue().trim());
                        }
                        else{
                            validationErrors.add(setValidationError(rowNum,headerName,"Email should be a valid email"));
                        }
                        break;
                    case "DEPARTMENT_NAME":
                        if(cell.getCellType() == CellType.STRING){
                            parsedEmployeeRequestDto.setDepartmentName(cell.getStringCellValue().trim());
                        }else{
                            validationErrors.add(setValidationError(rowNum,headerName,"Department name should be a string"));
                        }
                        break;
                    case "SALARY":
                        if(cell.getCellType() == CellType.NUMERIC){
                            parsedEmployeeRequestDto.setSalary(BigDecimal.valueOf(cell.getNumericCellValue()));
                        }
                        else{
                            validationErrors.add(setValidationError(rowNum,headerName,"Salary should be numeric"));
                        }
                        break;
                    case "DATE_OF_JOINING":
                        if(DateUtil.isCellDateFormatted(cell) && cell.getLocalDateTimeCellValue().toLocalDate().isBefore(LocalDate.now())){
                            parsedEmployeeRequestDto.setDateOfJoining(cell.getLocalDateTimeCellValue().toLocalDate());
                        }
                        else{
                            validationErrors.add(setValidationError(rowNum,headerName,"Date of joining should be a valid date"));
                        }
                        break;
                    case "ACTIVE":
                        if(cell.getCellType() == CellType.BOOLEAN){
                            parsedEmployeeRequestDto.setIsActive(cell.getBooleanCellValue());
                        }
                        else{
                            validationErrors.add(setValidationError(rowNum,headerName,"Active should be a boolean"));
                        }
                        break;
                    case "IS_AN_INTERN":
                        if(cell.getCellType() == CellType.BOOLEAN){
                            parsedEmployeeRequestDto.setIsAnIntern(cell.getBooleanCellValue());
                        }
                        else{
                            validationErrors.add(setValidationError(rowNum,headerName,"Is an Intern should be a boolean"));
                        }
                        break;
                    default:
                        validationErrors.add(setValidationError(rowNum,headerName,"Field is completely empty"));
                        break;
                }

            }
            parsedEmployeeRequestDtoList.add(parsedEmployeeRequestDto);
        });
        return parsedEmployeeRequestDtoList;
    }


    private ValidationError setValidationError(int rowNum, String field, String message){
        ValidationError validationError = new ValidationError();
        validationError.setRowNum(rowNum);
        validationError.setField(field);
        validationError.setMessage(message);

        return validationError;
    }

    private List<EmployeeRequestDto> mapToEmployeeRequestDto(List<ParsedEmployeeRequestDto> parsedEmployeeRequestDtoList) {
        return parsedEmployeeRequestDtoList.stream().map(emp -> new EmployeeRequestDto(emp.getFirstName(),
            emp.getLastName(),emp.getEmail(),emp.getDepartmentName(),emp.getSalary(),emp.getDateOfJoining(),emp.getIsActive(),emp.isAnIntern)).toList();
    }

    private List<EmployeeRequestDto> validateEmployees(List<EmployeeRequestDto> employeeRequestDtoList) {
        //TODO: Manually calling Bean
        return new ArrayList<>();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    private static class ParsedEmployeeRequestDto{
        String firstName;
        String lastName;
        String email;
        String departmentName;
        BigDecimal salary;
        LocalDate dateOfJoining;
        Boolean isActive;
        Boolean isAnIntern;
    }
}
