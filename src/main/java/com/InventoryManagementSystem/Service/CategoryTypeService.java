package com.InventoryManagementSystem.Service;

import com.InventoryManagementSystem.Model.CategoryType;
import com.InventoryManagementSystem.Model.Generate;
import com.InventoryManagementSystem.Repository.CategoryTypeRepository;
import com.InventoryManagementSystem.Repository.GenerateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.InventoryManagementSystem.Model.CodeName;
import com.InventoryManagementSystem.Repository.CodeNameRepository;
import com.InventoryManagementSystem.Util.CodeGeneratorUtil;
import java.util.List;
import java.util.Map;

@Service
public class CategoryTypeService {

    @Autowired
    private GenerateRepository generateRepository;

    @Autowired
    private CodeNameRepository codeNameRepository;

    @Autowired
    private CategoryTypeRepository categoryTypeRepository;

    public String saveBulk(List<Map<String, String>> items) {

    StringBuilder response = new StringBuilder();
    int successCount = 0;

    for (Map<String, String> item : items) {

        String category = item.get("categoryName");
        String subCategory = item.get("subCategoryName");

        if (category == null || category.isBlank()
                || subCategory == null || subCategory.isBlank()) {
            // Skip invalid rows safely
            continue;
        }

        // Reuse existing single-save logic
        String result = saveCategoryAndSubCategory(category, subCategory);
        response.append(result).append(" | ");
        successCount++;
    }

    if (successCount == 0) {
        throw new IllegalArgumentException("No valid category data found to save.");
    }

    return "Bulk save completed. Total saved: " + successCount;
}


    public String saveCategoryAndSubCategory(String category, String subCategory) {
        if (category == null || category.isBlank() || subCategory == null || subCategory.isBlank()) {
            throw new IllegalArgumentException("Category and SubCategory are required.");
        }

        boolean isCategoryExists = codeNameRepository.existsByCodeName(category, "Category");
        boolean isSubCategoryExists = codeNameRepository.existsByCodeNamesub(subCategory, "SubCategory");

        Generate generate = generateRepository.findByNameIgnoreCase("Category");
        if (generate == null || !Boolean.TRUE.equals(generate.getGenerate())) {
            throw new IllegalStateException("Auto-generate must be enabled to save Category and SubCategory.");
        }

        StringBuilder message = new StringBuilder();

        // Resolve or create Category
        CodeName categoryCodeName;
        String categoryCode;
        if (!isCategoryExists) {
            categoryCode = CodeGeneratorUtil.generateNextCategoryNumericCode(codeNameRepository);
            categoryCodeName = new CodeName();
            categoryCodeName.setCodeName(category.toUpperCase());
            categoryCodeName.setCode(categoryCode);
            categoryCodeName.setType("CATEGORY");
            categoryCodeName.setDisplay(true);
            codeNameRepository.save(categoryCodeName);
            message.append("Category saved. ");
        } else {
            categoryCodeName = codeNameRepository
                    .findByCodeNameIgnoreCaseAndTypeIgnoreCase(category, "CATEGORY")
                    .stream().findFirst()
                    .orElseThrow(() -> new IllegalStateException("Category exists but could not be resolved."));
            categoryCode = categoryCodeName.getCode();
            message.append("Category already exists. ");
        }

        // Resolve or create SubCategory under Category prefix
        CodeName subCategoryCodeName;
        String subCategoryCode;
        if (!isSubCategoryExists) {
            subCategoryCode = CodeGeneratorUtil.generateNextSubCategoryNumericCode(categoryCode, codeNameRepository);
            subCategoryCodeName = new CodeName();
            subCategoryCodeName.setCodeName(subCategory.toUpperCase());
            subCategoryCodeName.setCode(subCategoryCode);
            subCategoryCodeName.setType("SUBCATEGORY");
            subCategoryCodeName.setDisplay(true);
            codeNameRepository.save(subCategoryCodeName);
            message.append("SubCategory saved.");
        } else {
            subCategoryCodeName = codeNameRepository
                    .findByCodeNameIgnoreCaseAndTypeIgnoreCase(subCategory, "SUBCATEGORY")
                    .stream().findFirst()
                    .orElseThrow(() -> new IllegalStateException("SubCategory exists but could not be resolved."));
            subCategoryCode = subCategoryCodeName.getCode();
            message.append("SubCategory already exists.");
        }

        // Save mapping (use hierarchical subCategory code as the combined code)
        if (!isCategoryExists || !isSubCategoryExists) {
            CategoryType categoryType = new CategoryType();
            categoryType.setCategory(categoryCodeName);
            categoryType.setSubCategory(subCategoryCodeName);
            categoryType.setCategorycode(subCategoryCodeName.getCode()); // e.g., 001001
            categoryType.setCategoryname((category + " " + subCategory).toUpperCase());
            categoryTypeRepository.save(categoryType);
        }

        return message.toString().trim();
    }
}