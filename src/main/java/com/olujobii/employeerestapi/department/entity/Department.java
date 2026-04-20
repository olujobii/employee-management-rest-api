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
    @Column(name = "department_id")
    private Long departmentId;

    @NotBlank
    @Size(max = 100)
    @Column(unique = true, name = "department_name", nullable = false)
    private String departmentName;

    @NotNull
    @Column(name = "is_accepting_intern")
    private Boolean isAcceptingIntern;

    @NotNull
    @Column(updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @NotNull
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder
    private Department(String departmentName, Boolean isAcceptingIntern){
        this.departmentName = departmentName;
        this.isAcceptingIntern = isAcceptingIntern;
    }

    @PrePersist
    private void onCreate(){
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    private void onUpdate(){
        updatedAt = LocalDateTime.now();
    }
}
