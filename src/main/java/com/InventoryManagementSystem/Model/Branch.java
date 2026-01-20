package com.InventoryManagementSystem.Model;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "branch")
@Data
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "branch_id", unique = true, nullable = false)
    private Long branchId;

    @Column(name = "branch_code", length = 20, nullable = false, unique = true)
    private String branchCode;

    @Column(name = "branch_name", nullable = false, unique = true)
    private String branchName;

    @Column(name = "branch_manager")
    private String branchManager;

    @Column(name = "branch_phone", length = 20)
    private String branchPhone;

    @Column(name = "branch_alternate_phone", length = 20)
    private String branchAlternatePhone;

    @Column(name = "branch_email", length = 100)
    private String branchEmail;

    @Column(name = "contact_person_name")
    private String contactPersonName;

    @Column(name = "contact_person_phone", length = 20)
    private String contactPersonPhone;

    @Column(name = "country_name")
    private String countryName;

    @Column(name = "state_name")
    private String stateName;

    @Column(name = "city_name")
    private String cityName;

    @Column(name = "address_name")
    private String addressName;

    @Column(name = "display", nullable = false)
    private Boolean display = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}