package com.olujobii.employeerestapi.department.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long departmentId;

    @NotBlank
    @Size(max = 100)
    private String departmentName;

    @NotNull
    private Boolean isAcceptingIntern;

    @Builder
    private Department(String departmentName, Boolean isAcceptingIntern){
        this.departmentName = departmentName;
        this.isAcceptingIntern = isAcceptingIntern;
    }
}
