package com.InventoryManagementSystem.Controller;

import java.time.LocalDateTime;
import java.util.List;

import com.InventoryManagementSystem.Model.CodeName;
import com.InventoryManagementSystem.Repository.CodeNameRepository;
import com.InventoryManagementSystem.Util.CodeGeneratorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.InventoryManagementSystem.Model.Company;
import com.InventoryManagementSystem.Repository.CompanyRepository;
import com.InventoryManagementSystem.Model.Category;
import com.InventoryManagementSystem.Model.Item;
import com.InventoryManagementSystem.Repository.CategoryRepository;
import com.InventoryManagementSystem.Repository.ItemRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("api/company")
public class CompanyController {
    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CodeNameRepository codeNameRepository;

    @Autowired
    private com.InventoryManagementSystem.Service.CompanyService companyService;
    
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/company-save")
    public String saveCompany(@RequestBody Company company) {
        try {
            companyService.saveCompany(company);
            return "success";
        } catch (Exception e) {
            return "error";
        }
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveCompanyApi(@RequestBody JsonNode payload) {
        try {
            // If client sent `companyTypeCode` as a simple string (e.g. "MA"),
            // convert it to an object node { "code": "MA" } so Jackson can
            // deserialize into the `CodeName` entity reference expected by Company.
            if (payload.has("companyTypeCode") && payload.get("companyTypeCode").isTextual()) {
                ((com.fasterxml.jackson.databind.node.ObjectNode) payload).set(
                        "companyTypeCode",
                        objectMapper.createObjectNode().put("code", payload.get("companyTypeCode").asText())
                );
            }

            // Map basic fields to Company
            Company company = objectMapper.treeToValue(payload, Company.class);

            // Resolve categories (accepts array under 'category' or 'categories' or 'categoryIds')
            JsonNode categoriesNode = payload.has("category") ? payload.get("category") : (payload.has("categories") ? payload.get("categories") : (payload.has("categoryIds") ? payload.get("categoryIds") : null));
            if (categoriesNode != null) {
                Set<Category> cats = new HashSet<>();
                if (categoriesNode.isArray()) {
                    for (JsonNode n : categoriesNode) {
                        if (n.isNull()) continue;
                        if (n.has("categoryId") && !n.get("categoryId").isNull()) {
                            Long id = n.get("categoryId").asLong();
                            categoryRepository.findById(id).ifPresent(cats::add);
                        } else if (n.isNumber()) {
                            Long id = n.asLong();
                            categoryRepository.findById(id).ifPresent(cats::add);
                        } else if (n.has("categoryName") || n.isTextual()) {
                            String name = n.has("categoryName") ? n.get("categoryName").asText() : n.asText();
                            if (name != null && !name.isBlank()) {
                                List<Category> found = categoryRepository.findByCategoryNameContainingIgnoreCase(name);
                                if (!found.isEmpty()) cats.add(found.get(0));
                            }
                        }
                    }
                } else if (categoriesNode.isTextual()) {
                    String text = categoriesNode.asText().trim();
                    if (!text.isEmpty()) {
                        // treat as single category name or id
                        try {
                            Long id = Long.parseLong(text);
                            categoryRepository.findById(id).ifPresent(cats::add);
                        } catch (NumberFormatException ex) {
                            List<Category> found = categoryRepository.findByCategoryNameContainingIgnoreCase(text);
                            if (!found.isEmpty()) cats.add(found.get(0));
                        }
                    }
                }
                if (!cats.isEmpty()) company.setCategories(cats);
            }

            // Resolve items (accepts array under 'item' or 'items' or 'itemCodes')
            JsonNode itemsNode = payload.has("item") ? payload.get("item") : (payload.has("items") ? payload.get("items") : (payload.has("itemCodes") ? payload.get("itemCodes") : null));
            if (itemsNode != null) {
                Set<Item> items = new HashSet<>();
                if (itemsNode.isArray()) {
                    for (JsonNode n : itemsNode) {
                        if (n.isNull()) continue;
                        if (n.has("itemCode") && !n.get("itemCode").isNull()) {
                            String code = n.get("itemCode").asText();
                            itemRepository.findById(code).ifPresent(items::add);
                        } else if (n.isTextual()) {
                            String code = n.asText();
                            if (!code.isBlank()) itemRepository.findById(code).ifPresent(items::add);
                        }
                    }
                } else if (itemsNode.isTextual()) {
                    String text = itemsNode.asText().trim();
                    if (!text.isEmpty()) {
                        itemRepository.findById(text).ifPresent(items::add);
                    }
                }
                if (!items.isEmpty()) company.setItems(items);
            }

            companyService.saveCompany(company);
            return ResponseEntity.ok("success");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("error: " + e.getMessage());
        }
    }

    @GetMapping("/company-types")
    public List<CodeName> getCompanyTypes() {
        return codeNameRepository.findByTypeIgnoreCase("COMPANY");
    }

    @GetMapping("/next-company-code")
    public String getNextCompanyCode(@RequestParam String typeCode) {
        List<Company> companies = companyRepository.findAll();
        return CodeGeneratorUtil.generateNextCode(
                typeCode, companies, Company::getCompanyCode
        );
    }
}