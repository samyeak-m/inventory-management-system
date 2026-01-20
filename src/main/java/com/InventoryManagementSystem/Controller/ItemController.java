package com.InventoryManagementSystem.Controller;

import com.InventoryManagementSystem.Dto.ItemDto;
import com.InventoryManagementSystem.Service.ItemService;
import com.InventoryManagementSystem.Util.AESEncryption;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
@RequestMapping("/api/item")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @Autowired
    AESEncryption aesEncryption;

    @GetMapping("/get-items")
    public ResponseEntity<?> getItems() {
        List<ItemDto> items = itemService.getItemDtos();
        return ResponseEntity.ok(items);
    }

    @GetMapping("/get-item/{id}")
    public ResponseEntity<ItemDto> getItemById(@PathVariable String id) {
        try {
            ItemDto item = itemService.getItemById(id);
            if (item == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(item);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/submit-item")
    public ResponseEntity<String> saveItem(@RequestBody JsonNode payload) {
        try {

            if (payload.isArray()) {

                for (JsonNode node : payload) {
                    if (!node.isObject()) {
                        return ResponseEntity.badRequest()
                                .body("Invalid item in array – must be a JSON object");
                    }

                    String itemName = node.has("itemName") && !node.get("itemName").isNull()
                            ? node.get("itemName").asText().trim()
                            : null;

                    Long categoryId = node.has("categoryId") && !node.get("categoryId").isNull()
                            ? node.get("categoryId").asLong()
                            : null;

                    if (itemName == null || itemName.isEmpty()) {
                        return ResponseEntity.badRequest()
                                .body("Missing or empty 'itemName' in one of the items");
                    }
                    if (categoryId == null) {
                        return ResponseEntity.badRequest()
                                .body("Missing 'categoryId' in one of the items");
                    }

                    ItemDto dto = new ItemDto();
                    dto.setItemName(itemName);
                    dto.setCategoryId(categoryId);

                    if (node.has("unit") && !node.get("unit").isNull())
                        dto.setUnit(node.get("unit").asText().trim());

                    if (node.has("location") && !node.get("location").isNull())
                        dto.setLocation(node.get("location").asText().trim());

                    if (node.has("minimumOrderLevel") && !node.get("minimumOrderLevel").isNull())
                        dto.setMinimumOrderLevel(node.get("minimumOrderLevel").asInt());

                    if (node.has("reorderLevel") && !node.get("reorderLevel").isNull())
                        dto.setReorderLevel(node.get("reorderLevel").asInt());

                    if (node.has("maximumOrderLevel") && !node.get("maximumOrderLevel").isNull())
                        dto.setMaximumOrderLevel(node.get("maximumOrderLevel").asInt());

                    itemService.saveItem(dto);
                }

                return ResponseEntity.ok("Items saved successfully!");
            }

            if (payload.isObject()) {

                String itemName = payload.has("itemName") && !payload.get("itemName").isNull()
                        ? payload.get("itemName").asText().trim()
                        : null;

                Long categoryId = payload.has("categoryId") && !payload.get("categoryId").isNull()
                        ? payload.get("categoryId").asLong()
                        : null;

                if (itemName == null || itemName.isEmpty()) {
                    return ResponseEntity.badRequest().body("Missing or empty 'itemName'");
                }
                if (categoryId == null) {
                    return ResponseEntity.badRequest().body("Missing 'categoryId'");
                }

                ItemDto dto = new ItemDto();
                dto.setItemName(itemName);
                dto.setCategoryId(categoryId);

                if (payload.has("unit") && !payload.get("unit").isNull())
                    dto.setUnit(payload.get("unit").asText().trim());

                if (payload.has("location") && !payload.get("location").isNull())
                    dto.setLocation(payload.get("location").asText().trim());

                if (payload.has("minimumOrderLevel") && !payload.get("minimumOrderLevel").isNull())
                    dto.setMinimumOrderLevel(payload.get("minimumOrderLevel").asInt());

                if (payload.has("reorderLevel") && !payload.get("reorderLevel").isNull())
                    dto.setReorderLevel(payload.get("reorderLevel").asInt());

                if (payload.has("maximumOrderLevel") && !payload.get("maximumOrderLevel").isNull())
                    dto.setMaximumOrderLevel(payload.get("maximumOrderLevel").asInt());

                itemService.saveItem(dto);
                return ResponseEntity.ok("Item saved successfully!");
            }

            return ResponseEntity.badRequest()
                    .body("Payload must be a JSON object or an array of objects");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Server error: " + e.getMessage());
        }
    }

    @PutMapping("/update-item/{id}")
    public ResponseEntity<String> updateItem(@PathVariable String id, @RequestBody ItemDto dto) {
        try {
            dto.setItemCode(id); // Ensure the path variable is used
            itemService.updateItem(dto);
            return ResponseEntity.ok("Item updated successfully!");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body("Update failed: " + ex.getMessage());
        }
    }

    @DeleteMapping("/delete-item/{id}")
    public ResponseEntity<String> deleteItem(@PathVariable String id) {
        try {
            itemService.deleteItem(id);
            return ResponseEntity.ok("Item deleted successfully!");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
