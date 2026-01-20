package com.InventoryManagementSystem.Service;

import com.InventoryManagementSystem.Dto.CategoryDto;
import com.InventoryManagementSystem.Dto.ItemDto;
import com.InventoryManagementSystem.Model.*;
import com.InventoryManagementSystem.Repository.*;
import com.InventoryManagementSystem.Util.CodeGeneratorUtil;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.InventoryManagementSystem.Util.CodeGeneratorUtil.generateNextCode;

@Service
public class ItemService {
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CategoryTypeRepository categoryTypeRepository;

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CodeNameRepository codeNameRepository;

    @Autowired
    private GenerateRepository generateRepository;

    public void saveItem(ItemDto dto) {
        if (itemRepository.existsByItemName(dto.getItemName())) {
            throw new IllegalArgumentException("Item name already exists.");
        }
        String itemName = dto.getItemName();

        // Resolve category first (used for code prefix)
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        // Use the category's own code as the base for item codes, e.g., 001001-002
        String baseCode = category.getCategoryCode();
        if (baseCode == null || baseCode.isBlank()) {
            throw new IllegalStateException("Category code is missing for " + category.getCategoryName());
        }
        System.out.println("Item baseCode (category): " + baseCode);

        // Find existing items under this category prefix and generate the next sequence
        List<Item> items = itemRepository.findByItemCodeStartingWith(baseCode);
        String nextItemCode = generateNextCode(baseCode, items, Item::getItemCode);

        Branch branch = new Branch();
        branch.setBranchId(1L);

        Item item = new Item();
        item.setItemCode(nextItemCode);
        item.setItemName(dto.getItemName());
        item.setDisplay(true);
        item.setLocation(dto.getLocation());
        item.setUnit(dto.getUnit());
        item.setMaximumOrderLevel(dto.getMaximumOrderLevel());
        item.setMinimumOrderLevel(dto.getMinimumOrderLevel());
        item.setReorderLevel(dto.getReorderLevel());
        item.setBranch(branch);
        item.setCategory(category);

        // Set timestamps
        LocalDateTime now = LocalDateTime.now();
        item.setCreatedAt(now);
        item.setUpdatedAt(now);

        // Save the item
        itemRepository.save(item);
    }

    public ItemDto getItemById(String id) {
        Item item = itemRepository.findByIdAndDisplay(id);
        return toDto(item);
    }
    // public ItemDto updateItemById(String id){
    // if(getItemById=null){
    //
    // }
    //
    // }

    public ItemDto toDto(Item item) {
        ItemDto dto = new ItemDto();
        dto.setItemCode(item.getItemCode());
        dto.setItemName(item.getItemName());
        dto.setCategoryId(item.getCategory() != null ? item.getCategory().getCategoryId() : null);
        dto.setCategoryName(item.getCategory() != null ? item.getCategory().getCategoryName() : null);
        dto.setBranchId(item.getBranch() != null ? item.getBranch().getBranchId() : null);
        dto.setLocation(item.getLocation());
        dto.setUnit(item.getUnit());
        dto.setMinimumOrderLevel(item.getMinimumOrderLevel());
        dto.setMaximumOrderLevel(item.getMaximumOrderLevel());
        dto.setReorderLevel(item.getReorderLevel());
        dto.setDisplay(item.getDisplay());
        dto.setCreatedAt(item.getCreatedAt());
        dto.setUpdatedAt(item.getUpdatedAt());
        return dto;
    }

    public List<ItemDto> getItemDtos() {
        return itemRepository.findAllByDisplay().stream().map(this::toDto).toList();
    }

    public void updateItem(ItemDto dto) {
        Item item = itemRepository.findByIdAndDisplay(dto.getItemCode());
        if (item == null)
            throw new IllegalArgumentException("Item not found");

        // Update fields as needed
        item.setItemName(dto.getItemName());
        item.setLocation(dto.getLocation());
        item.setUnit(dto.getUnit());
        item.setMinimumOrderLevel(dto.getMinimumOrderLevel());
        item.setReorderLevel(dto.getReorderLevel());
        item.setMaximumOrderLevel(dto.getMaximumOrderLevel());
        item.setUpdatedAt(java.time.LocalDateTime.now());

        // Update category if provided
        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Category not found"));
            item.setCategory(category);
        }

        itemRepository.save(item);
    }

    public void deleteItem(String itemCode) {
        Item item = itemRepository.findByIdAndDisplay(itemCode);
        if (item == null)
            throw new IllegalArgumentException("Item not found");
        item.setDisplay(false);
        item.setUpdatedAt(java.time.LocalDateTime.now());
        itemRepository.save(item);
    }
}
