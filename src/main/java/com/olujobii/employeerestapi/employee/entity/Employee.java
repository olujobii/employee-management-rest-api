package com.olujobii.employeerestapi.employee.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.olujobii.employeerestapi.department.entity.Department;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    @Size(max = 50)
    private String firstName;

    @NotBlank
    @Size(max = 50)
    private String lastName;

    @NotBlank
    @Email
    @Column(unique = true)
    private String email;

    @ManyToOne
    @JoinColumn(name = "department_id")
    @NotNull(message = "department is required")
    private Department department;

    @NotNull
    @DecimalMin("0.00")
    private BigDecimal salary;

    @NotNull
    @PastOrPresent
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfJoining;

    @NotNull
    private Boolean active;

    @NotNull
    private Boolean isAnIntern;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @NotNull
    private LocalDateTime updatedAt;

    @Builder
    private Employee(String firstName, String lastName, String email, Department department, BigDecimal salary, LocalDate dateOfJoining, Boolean active, Boolean isAnIntern) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.department = department;
        this.salary = salary;
        this.dateOfJoining = dateOfJoining;
        this.active = active;
        this.isAnIntern = isAnIntern;
    }

    @PrePersist
    public void onCreated(){
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void onUpdated(){
        this.updatedAt = LocalDateTime.now();
    }
}
