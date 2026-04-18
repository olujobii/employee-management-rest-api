package com.olujobii.employeerestapi.department.repository;

import com.olujobii.employeerestapi.department.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department,Long> {

    Optional<Department> findByDepartmentName(String departmentName);

    @Query("select d from Department d where d.departmentId != ?1 and d.departmentName = ?2")
    Optional<Department> findByDepartmentNameWhereIdNotEqualTo(Long id, String departmentName);
}
