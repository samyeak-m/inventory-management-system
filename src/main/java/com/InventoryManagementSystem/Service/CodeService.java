package com.InventoryManagementSystem.Service;

import com.InventoryManagementSystem.Dto.CodeNameDto;
import com.InventoryManagementSystem.Model.CodeName;
import com.InventoryManagementSystem.Repository.CodeNameRepository;
import com.InventoryManagementSystem.Repository.GenerateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CodeService {

    @Autowired
    private CodeNameRepository codeNameRepository;

    @Autowired
    private GenerateRepository generateRepository;

    public ResponseEntity<?> saveOrUpdateManual(CodeNameDto dto) {
        // Check for existing records with display=true
        boolean nameExists = codeNameRepository.existsByCodeName(dto.getCodeName());
        boolean codeExists = codeNameRepository.existsByCode(dto.getCode());

        // Check for records with display=false
        CodeName existing = codeNameRepository.findFirstByCodeNameIgnoreCaseOrCodeIgnoreCaseAndDisplay(
                dto.getCodeName(), dto.getCode(), false);

        if (nameExists && codeExists) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Both code name and code already exist.");
        } else if (nameExists) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Code name already exists.");
        } else if (codeExists) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Code already exists.");
        } else if (existing != null) {
            // Update display to true and other fields
            existing.setDisplay(true);
            existing.setCodeName(dto.getCodeName().toUpperCase());
            existing.setCode(dto.getCode().toUpperCase());
            existing.setType(dto.getType().toUpperCase());
            codeNameRepository.save(existing);
            return ResponseEntity.ok(existing);
        }

        CodeName codeName = new CodeName();
        codeName.setCodeName(dto.getCodeName().toUpperCase());
        codeName.setCode(dto.getCode().toUpperCase());
        codeName.setType(dto.getType().toUpperCase());
        codeName.setDisplay(true);
        codeNameRepository.save(codeName);

        return ResponseEntity.ok(codeName);

    }
}