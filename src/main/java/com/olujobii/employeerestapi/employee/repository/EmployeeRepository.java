package com.olujobii.employeerestapi.employee.repository;

import com.olujobii.employeerestapi.employee.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Long> {

    Optional<Employee> findByEmail(String email);

    @Query("select e from Employee e where e.id != ?1 and e.email = ?2")
    Optional<Employee> findByEmailWhereIdIsNotEqualTo(Long id, String email);

    Page<Employee> findByActiveTrue(Boolean isActive, Pageable pageable);

    @Query("SELECT e FROM Employee e WHERE e.salary BETWEEN :min AND :max")
    List<Employee> findBySalaryRange(@Param("min")BigDecimal min, @Param("max")BigDecimal max);
}
