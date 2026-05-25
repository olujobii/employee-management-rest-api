package com.olujobii.employeemanagementsystem.repository;

import com.olujobii.employeemanagementsystem.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Long> {

    Optional<Employee> findByEmailIgnoreCase(String email);

    @Query("select e from Employee e where e.id != :id and upper(e.email) = upper(e.email) ")
    Optional<Employee> findByEmailIgnoreCaseAndNotId(Long id, String email);

    @Query("select e from Employee e where e.salary BETWEEN :min AND :max")
    List<Employee> findBySalaryRange(BigDecimal min, BigDecimal max);
}
