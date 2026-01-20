package com.InventoryManagementSystem.Service;

import com.InventoryManagementSystem.Model.Company;
import com.InventoryManagementSystem.Repository.CompanyRepository;
import com.InventoryManagementSystem.Util.CodeGeneratorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CompanyService {

	@Autowired
	private CompanyRepository companyRepository;

	public Company saveCompany(Company company) {
		LocalDateTime now = LocalDateTime.now();
		if (company.getCreatedAt() == null) {
			company.setCreatedAt(now);
		}
		company.setUpdatedAt(now);

		// Generate companyCode if not set (companyCode is the entity id and must be assigned)
		if (company.getCompanyCode() == null || company.getCompanyCode().isBlank()) {
			String typeCode = company.getCompanyType();
			List<Company> companies = companyRepository.findAll();
			String next = CodeGeneratorUtil.generateNextCode(typeCode, companies, Company::getCompanyCode);
			company.setCompanyCode(next);
		}

		return companyRepository.save(company);
	}
}
