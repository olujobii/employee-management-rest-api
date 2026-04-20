package com.olujobii.employeerestapi.employee.dto.response;

import java.util.List;

public record ImportResultDto(int successCount, int failureCount, List<ValidationError> validationErrors) {
}
