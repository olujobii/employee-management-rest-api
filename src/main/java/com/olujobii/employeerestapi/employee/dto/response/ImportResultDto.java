package com.olujobii.employeerestapi.employee.dto.response;

import java.util.List;

public record ImportResultDto(int totalRows, int successCount, int failureCount, List<String> errors) {
}
