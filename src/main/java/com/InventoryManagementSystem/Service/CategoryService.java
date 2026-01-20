package com.InventoryManagementSystem.Service;

import com.InventoryManagementSystem.Dto.CategoryDto;
import com.InventoryManagementSystem.Model.Category;
import com.InventoryManagementSystem.Model.CategoryType;
import com.InventoryManagementSystem.Repository.CategoryRepository;
import com.InventoryManagementSystem.Repository.CategoryTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.InventoryManagementSystem.Util.CodeGeneratorUtil.generateNextCode;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryTypeRepository categoryTypeRepository;

    @Transactional
    public void saveCategory(CategoryDto dto) {
        String name = dto.getCategoryName();
        Long typeId = dto.getCategoryTypeId();
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Category name is required.");
        }
        if (typeId == null) {
            throw new IllegalArgumentException("Category type is required.");
        }

        CategoryType type = categoryTypeRepository.findById(typeId)
                .orElseThrow(() -> new IllegalArgumentException("Category type not found."));
        String baseCode = type.getCategorycode();

        if (categoryRepository.existsByCategoryNameIgnoreCaseAndDisplayTrue(name)) {
            throw new IllegalArgumentException("Category name already exists.");
        }

        // 1) Restore by name if inactive, then STOP
        Category byName = categoryRepository.findByCategoryNameIgnoreCase(name).orElse(null);
        if (byName != null && Boolean.FALSE.equals(byName.getDisplay())) {
            byName.setDisplay(true);
            byName.setCategoryName(name.trim().toUpperCase());
            byName.setCategoryTypeId(type);
            categoryRepository.save(byName);
            return; // important: do not continue to insert
        }

        // 2) Reuse lowest-code inactive row, then STOP
        var inactiveOpt = categoryRepository
                .findFirstByDisplayFalseAndCategoryCodeStartingWithOrderByCategoryCodeAsc(baseCode);
        if (inactiveOpt.isPresent()) {
            Category inactive = inactiveOpt.get();
            inactive.setCategoryName(name.trim().toUpperCase());
            inactive.setCategoryTypeId(type);
            inactive.setDisplay(true);
            categoryRepository.save(inactive);
            return; // important: do not continue to insert
        }

        // 3) Else create a new unique code
        List<Category> siblings = categoryRepository.findByCategoryCodeStartingWith(baseCode);
        String nextCategoryCode = generateNextCode(baseCode, siblings, Category::getCategoryCode);

        Category category = new Category();
        category.setCategoryName(name.trim().toUpperCase());
        category.setCategoryTypeId(type);
        category.setCategoryCode(nextCategoryCode);
        category.setDisplay(true);

        categoryRepository.save(category);
    }

    public void updateCategory(Long id, CategoryDto dto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found."));

        String newName = dto.getCategoryName();
        if (newName != null && !newName.isBlank()
                && !category.getCategoryName().equalsIgnoreCase(newName)
                && categoryRepository.existsByCategoryName(newName)) {
            throw new IllegalArgumentException("Category name already exists.");
        }

        category.setCategoryName(dto.getCategoryName());

        CategoryType type = categoryTypeRepository.findById(dto.getCategoryTypeId())
                .orElseThrow(() -> new IllegalArgumentException("Category type not found."));
        // If type changes, update and regenerate code
        if (!category.getCategoryTypeId().getCtid().equals(dto.getCategoryTypeId())) {
            String baseCode = type.getCategorycode();
            List<Category> categories = categoryRepository.findByCategoryCodeStartingWith(baseCode);
            String nextCategoryCode = generateNextCode(baseCode, categories, Category::getCategoryCode);
            category.setCategoryCode(nextCategoryCode);
            category.setCategoryTypeId(type);
        } else {
            category.setCategoryTypeId(type);
        }

        categoryRepository.save(category);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAllActive();
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + id));
    }

    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + id));

        category.setDisplay(false);
        categoryRepository.save(category);
    }

    public List<CategoryDto> suggestCategoriesByName(String q) {
        List<Category> categories = categoryRepository.findByCategoryNameContainingIgnoreCase(q);
        return categories.stream()
                .map(cat -> {
                    CategoryDto dto = new CategoryDto();
                    dto.setCategoryName(cat.getCategoryName());
                    return dto;
                })
                .toList();
    }

}