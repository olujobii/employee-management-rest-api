package com.olujobii.employeemanagementsystem.repository;

import com.olujobii.employeemanagementsystem.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    Optional<Department> findByDepartmentNameIgnoreCase(String departmentName);

    @Query("select d from Department d where d.id != :id and upper(d.departmentName) = upper(:departmentName)")
    Optional<Department> findByDepartmentNameIgnoreCaseAndNotId(Long id, String departmentName);
}
