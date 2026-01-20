package com.InventoryManagementSystem.Controller;

import com.InventoryManagementSystem.Model.CategoryType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.InventoryManagementSystem.Service.CategoryTypeService;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.HttpStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categorytype")
public class CategoryTypeController {

    @Autowired
    private CategoryTypeService categoryTypeService;

   @PostMapping("/save")
public ResponseEntity<String> saveCategoryType(@RequestBody JsonNode payload) {
    try {
        // -------------------------------------------------
        // 1. BULK SAVE – payload is an array
        // -------------------------------------------------
        if (payload.isArray()) {
            List<Map<String, String>> items = new ArrayList<>();

            for (JsonNode node : payload) {
                if (!node.isObject()) {
                    return ResponseEntity.badRequest()
                            .body("Invalid item in array – must be a JSON object");
                }

                // Extract fields with null-checks and trimming
                String category = node.has("category") && !node.get("category").isNull()
                        ? node.get("category").asText().trim()
                        : null;

                String categoryType = node.has("categoryType") && !node.get("categoryType").isNull()
                        ? node.get("categoryType").asText().trim()
                        : null;

                if (category == null || category.isEmpty()) {
                    return ResponseEntity.badRequest()
                            .body("Missing or empty 'category' in one of the items");
                }
                if (categoryType == null || categoryType.isEmpty()) {
                    return ResponseEntity.badRequest()
                            .body("Missing or empty 'categoryType' in one of the items");
                }

                // Map that matches exactly what saveBulk(...) expects
                items.add(Map.of(
                        "categoryName", category,
                        "subCategoryName", categoryType
                ));
            }

            // One call – everything is saved in a single transaction
            String result = categoryTypeService.saveBulk(items);
            return ResponseEntity.ok(result);
        }

        // -------------------------------------------------
        // 2. SINGLE SAVE – payload is one object
        // -------------------------------------------------
        if (payload.isObject()) {
            String category = payload.has("category") && !payload.get("category").isNull()
                    ? payload.get("category").asText().trim()
                    : null;

            String categoryType = payload.has("categoryType") && !payload.get("categoryType").isNull()
                    ? payload.get("categoryType").asText().trim()
                    : null;

            if (category == null || category.isEmpty()) {
                return ResponseEntity.badRequest().body("Missing or empty 'category'");
            }
            if (categoryType == null || categoryType.isEmpty()) {
                return ResponseEntity.badRequest().body("Missing or empty 'categoryType'");
            }

            String result = categoryTypeService.saveCategoryAndSubCategory(category, categoryType);
            return ResponseEntity.ok(result);
        }

        // -------------------------------------------------
        // 3. Invalid payload
        // -------------------------------------------------
        return ResponseEntity.badRequest().body("Payload must be a JSON object or an array of objects");

    } catch (IllegalStateException e) {
        return ResponseEntity.badRequest().body("Auto-generation disabled: " + e.getMessage());
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Server error: " + e.getMessage());
    }
}
}