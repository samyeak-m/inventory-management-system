package com.InventoryManagementSystem.Service;

import com.InventoryManagementSystem.Dto.BranchDto;
import com.InventoryManagementSystem.Model.Branch;
import com.InventoryManagementSystem.Model.CodeName;
import com.InventoryManagementSystem.Repository.CodeNameRepository;
import com.InventoryManagementSystem.Repository.BranchRepository;
import com.InventoryManagementSystem.Util.NumericCodeGeneratorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BranchService {
    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private CodeNameRepository codeNameRepository;

    public ResponseEntity<?> handleBranchSetup(BranchDto dto,
            String country,
            String province,
            String city,
            String address) {
        String name = dto.getBranchName();
        if (name == null || name.isBlank()) {
            return ResponseEntity.badRequest().body("Branch name is required.");
        }

        // Block only ACTIVE duplicates
        if (branchRepository.existsByBranchNameIgnoreCaseAndDisplayTrue(name)) {
            return ResponseEntity.badRequest().body("Branch name already exists.");
        }

        // If a soft-deleted row exists by same name, restore it
        Branch byName = branchRepository.findFirstByBranchNameIgnoreCase(name).orElse(null);
        if (byName != null && Boolean.FALSE.equals(byName.getDisplay())) {
            byName.setBranchName(name.toUpperCase());
            byName.setBranchManager(dto.getBranchManager());
            byName.setBranchPhone(dto.getBranchPhone());
            byName.setBranchAlternatePhone(dto.getBranchAlternatePhone());
            byName.setBranchEmail(dto.getBranchEmail());
            byName.setContactPersonName(dto.getContactPersonName());
            byName.setContactPersonPhone(dto.getContactPersonPhone());
            byName.setDisplay(dto.getDisplay() != null ? dto.getDisplay() : true);
            if (byName.getCreatedAt() == null)
                byName.setCreatedAt(LocalDateTime.now());
            byName.setUpdatedAt(LocalDateTime.now());

            byName.setCountryName(country != null ? country.toUpperCase() : null);
            byName.setStateName(province != null ? province.toUpperCase() : null);
            byName.setCityName(city != null ? city.toUpperCase() : null);
            byName.setAddressName(address != null ? address.toUpperCase() : null);

            ensureCodeNameExists(name, "BRANCH");
            ensureCodeNameExists(country, "COUNTRY");
            ensureCodeNameExists(province, "STATE");
            ensureCodeNameExists(city, "CITY");
            ensureCodeNameExists(address, "ADDRESS");

            branchRepository.save(byName);
            return ResponseEntity.ok(byName);
        }

        // 1) Try to reuse an inactive row (lowest code)
        return branchRepository.findFirstByDisplayFalseOrderByBranchCodeAsc()
                .map(inactive -> {
                    inactive.setBranchName(name.toUpperCase());
                    inactive.setBranchManager(dto.getBranchManager());
                    inactive.setBranchPhone(dto.getBranchPhone());
                    inactive.setBranchAlternatePhone(dto.getBranchAlternatePhone());
                    inactive.setBranchEmail(dto.getBranchEmail());
                    inactive.setContactPersonName(dto.getContactPersonName());
                    inactive.setContactPersonPhone(dto.getContactPersonPhone());
                    inactive.setDisplay(dto.getDisplay() != null ? dto.getDisplay() : true);
                    if (inactive.getCreatedAt() == null)
                        inactive.setCreatedAt(LocalDateTime.now());
                    inactive.setUpdatedAt(LocalDateTime.now());

                    inactive.setCountryName(country != null ? country.toUpperCase() : null);
                    inactive.setStateName(province != null ? province.toUpperCase() : null);
                    inactive.setCityName(city != null ? city.toUpperCase() : null);
                    inactive.setAddressName(address != null ? address.toUpperCase() : null);

                    ensureCodeNameExists(name, "BRANCH");
                    ensureCodeNameExists(country, "COUNTRY");
                    ensureCodeNameExists(province, "STATE");
                    ensureCodeNameExists(city, "CITY");
                    ensureCodeNameExists(address, "ADDRESS");

                    branchRepository.save(inactive);
                    return ResponseEntity.ok(inactive);
                })
                .orElseGet(() -> {
                    // 2) Create new with next 3-digit code
                    List<Branch> existing = branchRepository.findAll();
                    String newBranchCode = NumericCodeGeneratorUtil.generateNextThreeDigits(existing,
                            Branch::getBranchCode);

                    Branch branch = new Branch();
                    branch.setBranchCode(newBranchCode);
                    branch.setBranchName(name.toUpperCase());
                    branch.setBranchManager(dto.getBranchManager());
                    branch.setBranchPhone(dto.getBranchPhone());
                    branch.setBranchAlternatePhone(dto.getBranchAlternatePhone());
                    branch.setBranchEmail(dto.getBranchEmail());
                    branch.setContactPersonName(dto.getContactPersonName());
                    branch.setContactPersonPhone(dto.getContactPersonPhone());
                    branch.setDisplay(dto.getDisplay() != null ? dto.getDisplay() : true);
                    branch.setCreatedAt(LocalDateTime.now());
                    branch.setUpdatedAt(LocalDateTime.now());

                    branch.setCountryName(country != null ? country.toUpperCase() : null);
                    branch.setStateName(province != null ? province.toUpperCase() : null);
                    branch.setCityName(city != null ? city.toUpperCase() : null);
                    branch.setAddressName(address != null ? address.toUpperCase() : null);

                    ensureCodeNameExists(name, "BRANCH");
                    ensureCodeNameExists(country, "COUNTRY");
                    ensureCodeNameExists(province, "STATE");
                    ensureCodeNameExists(city, "CITY");
                    ensureCodeNameExists(address, "ADDRESS");

                    branchRepository.save(branch);
                    return ResponseEntity.ok(branch);
                });
    }

    private void ensureCodeNameExists(String name, String type) {
        if (name == null || name.isBlank())
            return;
        boolean exists = codeNameRepository.existsByCodeName(name, type);
        if (exists)
            return;

        List<CodeName> sameType = codeNameRepository.findByTypeIgnoreCase(type);
        int max = sameType.stream()
                .map(CodeName::getCode)
                .filter(c -> c != null && c.matches("\\d{3}"))
                .mapToInt(Integer::parseInt)
                .max().orElse(0);
        String next = String.format("%03d", max + 1);
        while (codeNameRepository.existsByCode(next)) {
            int n = Integer.parseInt(next) + 1;
            if (n > 999)
                throw new IllegalStateException("Code overflow for type " + type);
            next = String.format("%03d", n);
        }

        CodeName cn = new CodeName();
        cn.setCodeName(name.trim().toUpperCase());
        cn.setCode(next);
        cn.setType(type.toUpperCase());
        cn.setDisplay(true);
        codeNameRepository.save(cn);
    }

    public List<Branch> getBranches() {
        return branchRepository.findAllActive();
    }

    public List<CodeName> getBranchTypes() {
        return codeNameRepository.findByTypeIgnoreCase("BRANCH");
    }

    public Branch getBranchById(Long id) {
        return branchRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Branch not found with id: " + id));
    }

    public void updateBranch(Long id, BranchDto dto) {
        Branch b = branchRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Branch not found with id: " + id));

        String newName = dto.getBranchName();
        if (newName != null && !newName.isBlank()
                && !b.getBranchName().equalsIgnoreCase(newName)
                && branchRepository.existsByBranchNameIgnoreCase(newName)) {
            throw new IllegalArgumentException("Branch name already exists.");
        }

        if (newName != null && !newName.isBlank())
            b.setBranchName(newName.toUpperCase());
        b.setBranchManager(dto.getBranchManager());
        b.setBranchPhone(dto.getBranchPhone());
        b.setBranchAlternatePhone(dto.getBranchAlternatePhone());
        b.setBranchEmail(dto.getBranchEmail());
        b.setContactPersonName(dto.getContactPersonName());
        b.setContactPersonPhone(dto.getContactPersonPhone());

        if (dto.getDisplay() != null)
            b.setDisplay(dto.getDisplay());
        b.setUpdatedAt(LocalDateTime.now());

        branchRepository.save(b);
    }

    public void deleteBranch(Long id) {
        Branch b = branchRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Branch not found with id: " + id));
        b.setDisplay(false);
        b.setUpdatedAt(LocalDateTime.now());
        branchRepository.save(b);
    }
}
