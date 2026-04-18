package com.olujobii.employeerestapi.department.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

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

    @NotNull
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @NotNull
    private LocalDateTime updatedAt;

    @Builder
    private Department(String departmentName, Boolean isAcceptingIntern){
        this.departmentName = departmentName;
        this.isAcceptingIntern = isAcceptingIntern;
    }

    @PrePersist
    public void onCreate(){
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    public void onUpdate(){
        updatedAt = LocalDateTime.now();
    }
}
