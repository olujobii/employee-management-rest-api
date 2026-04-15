package com.olujobii.employeerestapi.repository;

import com.olujobii.employeerestapi.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Long> {

    @Query("select e from Employee e where e.email = ?1")
    Optional<Employee> findByEmail(String email);
}
