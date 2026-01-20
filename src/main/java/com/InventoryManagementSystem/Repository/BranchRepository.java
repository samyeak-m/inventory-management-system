package com.InventoryManagementSystem.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.InventoryManagementSystem.Model.Branch;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BranchRepository extends JpaRepository<Branch, Long> {
    boolean findByBranchNameIgnoreCase(String branchName);

    @Query("SELECT b FROM Branch b WHERE b.display = true")
    List<Branch> findAllActiveBranch();

    boolean existsByBranchNameIgnoreCase(String branchName);

    @Query("SELECT b FROM Branch b WHERE b.display = true")
    List<Branch> findAllActive();

    // New: block only active duplicates
    boolean existsByBranchNameIgnoreCaseAndDisplayTrue(String branchName);

    // New: find by name (for potential restore)
    Optional<Branch> findFirstByBranchNameIgnoreCase(String branchName);

    // New: reuse an inactive row with the lowest code
    Optional<Branch> findFirstByDisplayFalseOrderByBranchCodeAsc();
}