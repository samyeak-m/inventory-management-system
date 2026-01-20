package com.InventoryManagementSystem.Service;

import com.InventoryManagementSystem.Dto.DepartmentDto;
import com.InventoryManagementSystem.Model.Department;
import com.InventoryManagementSystem.Repository.DepartmentRepository;
import com.InventoryManagementSystem.Util.NumericCodeGeneratorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Transactional
    public ResponseEntity<?> handleDepartmentSetup(DepartmentDto dto) {
        String name = dto.getDepartmentName();
        if (name == null || name.isBlank()) {
            return ResponseEntity.badRequest().body("Department name is required.");
        }

        // Block only active duplicates
        if (departmentRepository.existsByDepartmentNameIgnoreCaseAndDisplayTrue(name)) {
            return ResponseEntity.badRequest().body("Department name already exists.");
        }

        // If soft-deleted exists by name, restore it
        Department existing = departmentRepository.findByDepartmentNameIgnoreCase(name).orElse(null);
        if (existing != null && Boolean.FALSE.equals(existing.getDisplay())) {
            existing.setDisplay(true);
            existing.setUpdatedAt(LocalDateTime.now());
            departmentRepository.save(existing);
            return ResponseEntity.ok("Department restored.");
        }

        // Try to reuse an inactive row/code first
        return departmentRepository.findFirstByDisplayFalseOrderByDepartmentCodeAsc()
                .map(inactive -> {
                    inactive.setDepartmentName(name.toUpperCase());
                    inactive.setDisplay(true);
                    inactive.setUpdatedAt(LocalDateTime.now());
                    if (inactive.getCreatedAt() == null) {
                        inactive.setCreatedAt(LocalDateTime.now());
                    }
                    departmentRepository.save(inactive);
                    return ResponseEntity.ok("Department created with code: " + inactive.getDepartmentCode());
                })
                .orElseGet(() -> {
                    // generate next unique code against ALL rows
                    List<Department> all = departmentRepository.findAll();
                    String nextCode = NumericCodeGeneratorUtil.generateNextThreeDigits(all,
                            Department::getDepartmentCode);

                    Department d = new Department();
                    d.setDepartmentName(name.toUpperCase());
                    d.setDepartmentCode(nextCode);
                    d.setDisplay(true);
                    d.setCreatedAt(LocalDateTime.now());
                    d.setUpdatedAt(LocalDateTime.now());
                    departmentRepository.save(d);

                    return ResponseEntity.ok("Department created with code: " + nextCode);
                });
    }

    public List<Department> getAllDepartments() {
        return departmentRepository.findAllByDisplayTrue();
    }

    public Department getDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Department not found with id: " + id));
    }

    public void deleteDepartment(Long id) {
        Department d = departmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Department not found with id: " + id));
        d.setDisplay(false);
        d.setUpdatedAt(LocalDateTime.now());
        departmentRepository.save(d);
    }

    public void updateDepartment(Long id, DepartmentDto dto) {
        Department d = departmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Department not found with id: " + id));

        String newName = dto.getDepartmentName();
        if (newName != null && !newName.isBlank()) {
            boolean nameTaken = departmentRepository.existsByDepartmentNameIgnoreCase(newName)
                    && !d.getDepartmentName().equalsIgnoreCase(newName);
            if (nameTaken) {
                throw new IllegalArgumentException("Department name already exists.");
            }
            d.setDepartmentName(newName.toUpperCase());
        }

        d.setUpdatedAt(LocalDateTime.now());
        if (dto.getDisplay() != null) {
            d.setDisplay(dto.getDisplay());
        }
        departmentRepository.save(d);
    }
}