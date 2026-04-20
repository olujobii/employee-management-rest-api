package com.olujobii.employeerestapi.employee.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ValidationError {
    private int rowNum;
    private String field;
    private String message;


    @Override
    public String toString(){
        return "Row "+rowNum+": "+field+" - "+message;
    }
}
