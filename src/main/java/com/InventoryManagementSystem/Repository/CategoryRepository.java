package com.InventoryManagementSystem.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.InventoryManagementSystem.Model.Category;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByCategoryCodeStartingWith(String baseCode);

    boolean existsByCategoryName(String categoryName);

    @Query("SELECT c FROM Category c WHERE c.display = true")
    List<Category> findAllActive();

    List<Category> findByCategoryNameContainingIgnoreCase(String q);

    // New: block only active duplicates (case-insensitive)
    boolean existsByCategoryNameIgnoreCaseAndDisplayTrue(String categoryName);

    // New: find by name (for potential restore)
    Optional<Category> findByCategoryNameIgnoreCase(String categoryName);

    // New: reuse an inactive row within the same base prefix (lowest code)
    Optional<Category> findFirstByDisplayFalseAndCategoryCodeStartingWithOrderByCategoryCodeAsc(String baseCode);
}