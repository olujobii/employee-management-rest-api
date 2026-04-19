package com.olujobii.employeerestapi.employee.service.impl;

import com.olujobii.employeerestapi.department.entity.Department;
import com.olujobii.employeerestapi.department.service.DepartmentService;
import com.olujobii.employeerestapi.employee.dto.request.EmployeePatchRequestDto;
import com.olujobii.employeerestapi.employee.dto.request.EmployeeRequestDto;
import com.olujobii.employeerestapi.employee.dto.response.EmployeeResponseDto;
import com.olujobii.employeerestapi.employee.dto.response.ImportResultDto;
import com.olujobii.employeerestapi.employee.entity.Employee;
import com.olujobii.employeerestapi.employee.mapper.EmployeeMapper;
import com.olujobii.employeerestapi.employee.mapper.EmployeeResponseMapper;
import com.olujobii.employeerestapi.employee.repository.EmployeeRepository;
import com.olujobii.employeerestapi.employee.service.EmployeeService;
import com.olujobii.employeerestapi.exception.*;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import lombok.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
@AllArgsConstructor
@Validated
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final DepartmentService departmentService;
    private final Validator validator;

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
                .orElseThrow(() -> new EmployeeNotFoundException(id, HttpStatus.NOT_FOUND));

        return EmployeeResponseMapper.toEmployeeResponseDto(employee.getId(),
                employee.getFirstName(),employee.getLastName(),employee.getEmail(),employee.getDepartment().getDepartmentName(),employee.getSalary(),
                employee.getDateOfJoining(),employee.getActive(),employee.getIsAnIntern());
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
                .orElseThrow(() -> new EmployeeNotFoundException(id, HttpStatus.NOT_FOUND));

        if(employeePatchRequestDto.salary() == null && employeePatchRequestDto.departmentName() == null
                && employeePatchRequestDto.active() == null)
            throw new InvalidEmployeePatchRequestBodyException("Only departmentId, salary or " +
                    "active fields can be passed in request body", HttpStatus.BAD_REQUEST);

        if(employeePatchRequestDto.salary() != null){
            validateSalary(employeePatchRequestDto,employee.getIsAnIntern());
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
    public ImportResultDto importEmployeeData(MultipartFile file) throws IOException {
        List<EmployeeExcelRequestDto> employeeExcelRequestDtoList;

        try(InputStream inputStream = file.getInputStream();
            Workbook workbook = new XSSFWorkbook(inputStream)){
            Sheet sheet = workbook.getSheetAt(0);
            final int totalHeaderColumn = 8;
            //Dynamically map through the header to know what column it is and store the right data to the appropriate field.
            Map<String, Integer> headerRowMap = new HashMap<>();
            sheet.getRow(0).forEach(cell -> {
                String headerColumnName = cell.getStringCellValue().trim().toLowerCase();
                headerRowMap.put(headerColumnName,cell.getColumnIndex());
            });

            //FIXME: Throw a custom exception here.
            if(headerRowMap.size() != totalHeaderColumn)
                throw new RuntimeException("The Excel file must have exactly 8 columns with these header names: ");

            //Parsing Data
            employeeExcelRequestDtoList = parsingExcelData(sheet,headerRowMap);
            System.out.println(employeeExcelRequestDtoList);
        }

        //Validating data
        List<EmployeeRequestDto> employeeRequestDtoList = validatingExcelData(employeeExcelRequestDtoList);

        //FIXME: Return correct data
        return new ImportResultDto(1,1,1,null);
    }


    private boolean validateInternAcceptance(EmployeeRequestDto employeeRequestDto, Department department){
        return employeeRequestDto.isAnIntern() && !department.getIsAcceptingIntern();
    }

    private void validateSalary(EmployeeRequestDto employeeRequestDto){
        BigDecimal employeeSalary = employeeRequestDto.salary();
        BigDecimal minimumInternSalary = new BigDecimal(15_000);
        BigDecimal minimumNonInternSalary = new BigDecimal(30_000);

        if(employeeRequestDto.isAnIntern() && employeeSalary.compareTo(minimumInternSalary) < 0)
            throw new EmployeeException("Minimum intern salary is 15,000", HttpStatus.BAD_REQUEST);

        if(!employeeRequestDto.isAnIntern() && employeeSalary.compareTo(minimumNonInternSalary) < 0)
            throw new EmployeeException("Minimum non intern salary is 30,000",HttpStatus.BAD_REQUEST);
    }

    private void validateSalary(EmployeePatchRequestDto employeePatchRequestDto, boolean isAnIntern){
        BigDecimal employeeSalary = employeePatchRequestDto.salary();
        BigDecimal minimumInternSalary = new BigDecimal(15_000);
        BigDecimal minimumNonInternSalary = new BigDecimal(30_000);

        if(isAnIntern && employeeSalary.compareTo(minimumInternSalary) < 0)
            throw new EmployeeException("Minimum intern salary is 15,000", HttpStatus.BAD_REQUEST);

        if(!isAnIntern && employeeSalary.compareTo(minimumNonInternSalary) < 0)
            throw new EmployeeException("Minimum non intern salary is 30,000",HttpStatus.BAD_REQUEST);
    }

    private List<EmployeeExcelRequestDto> parsingExcelData(Sheet sheet, Map<String, Integer> headerRowMap) {
        List<EmployeeExcelRequestDto> employeelist = new ArrayList<>();
        sheet.forEach(row -> {
            if(row.getRowNum() == 0)
                return;

            EmployeeExcelRequestDto empRequestDto = new EmployeeExcelRequestDto();
            for(Map.Entry<String, Integer> entry : headerRowMap.entrySet()){

                String headerName = entry.getKey();
                Cell cell = row.getCell(entry.getValue(), Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);

                if(cell == null)
                    continue;

                switch(headerName){
                    case "first_name":
                        if(cell.getCellType() == CellType.STRING)
                            empRequestDto.setFirstName(cell.getStringCellValue().trim());
                        break;
                    case "last_name":
                        if(cell.getCellType() == CellType.STRING)
                            empRequestDto.setLastName(cell.getStringCellValue().trim());
                        break;
                    case "email":
                        if(cell.getCellType() == CellType.STRING)
                            empRequestDto.setEmail(cell.getStringCellValue().trim());
                        break;
                    case "department_name":
                        if(cell.getCellType() == CellType.STRING)
                            empRequestDto.setDepartmentName(cell.getStringCellValue().trim());
                        break;
                    case "salary":
                        if(cell.getCellType() == CellType.NUMERIC)
                            empRequestDto.setSalary(BigDecimal.valueOf(cell.getNumericCellValue()));
                        break;
                    case "date_of_joining":
                        if(cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell))
                            empRequestDto.setDateOfJoining(cell.getLocalDateTimeCellValue().toLocalDate());
                        break;
                    case "active":
                        if(cell.getCellType() == CellType.BOOLEAN)
                            empRequestDto.setIsActive(cell.getBooleanCellValue());
                        break;
                    case "is_an_intern":
                        if(cell.getCellType() == CellType.BOOLEAN)
                            empRequestDto.setIsAnIntern(cell.getBooleanCellValue());
                        break;
                    default:
                        break;
                }
            }
            employeelist.add(empRequestDto);
        });

        return employeelist;
    }

    private List<EmployeeRequestDto> validatingExcelData(List<EmployeeExcelRequestDto> employeeExcelRequestDtoList) {

        return new ArrayList<>();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    private static class EmployeeExcelRequestDto{
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
