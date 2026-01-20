package com.InventoryManagementSystem.Controller;

import com.InventoryManagementSystem.Dto.CategoryDto;
import com.InventoryManagementSystem.Model.Category;
import com.InventoryManagementSystem.Model.CategoryType;
import com.InventoryManagementSystem.Repository.CategoryRepository;
import com.InventoryManagementSystem.Repository.CategoryTypeRepository;
import com.InventoryManagementSystem.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryTypeRepository categoryTypeRepository;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categorytypes")
    public List<CategoryType> getCategoryTypes() {
        return categoryTypeRepository.findAll();
    }

    @PostMapping("/category-save")
    public ResponseEntity<String> saveCategory(@RequestBody JsonNode payload) {
        try {
            if (payload.isArray()) {
                int count = 0;
                int idx = 0;
                for (JsonNode item : payload) {
                    idx++;
                    String categoryName = item.has("categoryName") && !item.get("categoryName").isNull() ? item.get("categoryName").asText() : null;
                    Long categoryTypeId = item.has("categoryTypeId") && !item.get("categoryTypeId").isNull() ? item.get("categoryTypeId").asLong() : null;
                    if (categoryName == null || categoryTypeId == null) {
                        return ResponseEntity.badRequest().body("Missing required fields for item " + idx + ": categoryName and categoryTypeId are required");
                    }
                    CategoryDto dto = new CategoryDto();
                    dto.setCategoryName(categoryName);
                    dto.setCategoryTypeId(categoryTypeId);
                    categoryService.saveCategory(dto);
                    count++;
                }
                return ResponseEntity.ok("Saved " + count + " items");
            } else if (payload.isObject()) {
                String categoryName = payload.has("categoryName") && !payload.get("categoryName").isNull() ? payload.get("categoryName").asText() : null;
                Long categoryTypeId = payload.has("categoryTypeId") && !payload.get("categoryTypeId").isNull() ? payload.get("categoryTypeId").asLong() : null;
                if (categoryName == null || categoryTypeId == null) {
                    return ResponseEntity.badRequest().body("Missing required fields: categoryName and categoryTypeId are required");
                }
                CategoryDto dto = new CategoryDto();
                dto.setCategoryName(categoryName);
                dto.setCategoryTypeId(categoryTypeId);
                categoryService.saveCategory(dto);
                return ResponseEntity.ok("Saved successfully!");
            } else {
                return ResponseEntity.badRequest().body("Unsupported JSON payload");
            }
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PutMapping("/category-update/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable Long id, @RequestBody CategoryDto dto) {
        categoryService.updateCategory(id, dto);
        return ResponseEntity.ok("Updated successfully!");
    }

    @DeleteMapping("/category-delete/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.ok("Category deleted successfully!");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.notFound().build();
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Cannot delete category: " + ex.getMessage());
        }
    }

    @GetMapping("/category-list")
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        try {
            Category category = categoryService.getCategoryById(id);
            return ResponseEntity.ok(category);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/category-suggest")
    public List<CategoryDto> suggestCategories(@RequestParam String q) {
        return categoryService.suggestCategoriesByName(q); // ✅ call the service
    }





}