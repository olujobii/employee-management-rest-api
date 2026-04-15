package com.olujobii.employeerestapi.employee.repository;

import com.olujobii.employeerestapi.employee.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Long> {

    @Query("select e from Employee e where e.email = ?1")
    Optional<Employee> findByEmail(String email);

    @Query("select e from Employee e where e.id != ?1 and e.email = ?2")
    Optional<Employee> findByEmailWhereIdIsNotEqualTo(Long id, String email);
}
