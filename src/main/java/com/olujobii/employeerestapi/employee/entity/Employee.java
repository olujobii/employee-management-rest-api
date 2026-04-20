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
    @Column(name = "id")
    private Long id;

    @NotBlank
    @Size(max = 50)
    @Column(name = "first_name")
    private String firstName;

    @NotBlank
    @Size(max = 50)
    @Column(name = "last_name")
    private String lastName;

    @NotBlank
    @Email
    @Column(unique = true, name = "email")
    private String email;

    @ManyToOne
    @JoinColumn(name = "department_name", referencedColumnName = "department_name")
    @NotNull(message = "department is required")
    private Department department;

    @NotNull
    @DecimalMin("0.00")
    @Column(name = "salary")
    private BigDecimal salary;

    @NotNull
    @PastOrPresent
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "date_of_joining")
    private LocalDate dateOfJoining;

    @NotNull
    @Column(name = "active")
    private Boolean active;

    @NotNull
    @Column(name = "is_an_intern")
    private Boolean isAnIntern;

    @Column(updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @NotNull
    @Column(name = "updated_at")
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
    private void onCreated(){
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    private void onUpdate(){
        this.updatedAt = LocalDateTime.now();
    }
}
