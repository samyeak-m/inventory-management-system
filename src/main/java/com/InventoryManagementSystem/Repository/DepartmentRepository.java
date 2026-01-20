package com.InventoryManagementSystem.Repository;

import com.InventoryManagementSystem.Model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    List<Department> findAllByDisplayTrue();

    boolean existsByDepartmentNameIgnoreCase(String departmentName);

    boolean existsByDepartmentNameIgnoreCaseAndDisplayTrue(String departmentName);

    Optional<Department> findByDepartmentCode(String departmentCode);

    Optional<Department> findByDepartmentNameIgnoreCase(String departmentName);

    // Reuse an inactive (soft-deleted) department by the lowest code
    Optional<Department> findFirstByDisplayFalseOrderByDepartmentCodeAsc();
}