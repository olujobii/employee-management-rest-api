package com.olujobii.employeemanagementsystem.repository;

import com.olujobii.employeemanagementsystem.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Long> {

    Optional<Employee> findByEmailIgnoreCase(String email);

    @Query("select e from Employee e where e.id != :id and upper(e.email) = upper(e.email) ")
    Optional<Employee> findByEmailIgnoreCaseAndNotId(Long id, String email);
}
