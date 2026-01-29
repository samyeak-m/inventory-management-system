# Inventory Management System (IMS)
## Complete Project Documentation

---

## Table of Contents

1. [Project Overview](#1-project-overview)
2. [Technology Stack](#2-technology-stack)
3. [Getting Started](#3-getting-started)
4. [Project Structure](#4-project-structure)
5. [Database Configuration](#5-database-configuration)
6. [Authentication & Security](#6-authentication--security)
7. [API Reference](#7-api-reference)
8. [Frontend Routes](#8-frontend-routes)
9. [Core Modules](#9-core-modules)
10. [Important Functions & Services](#10-important-functions--services)
11. [Form Configuration System](#11-form-configuration-system)
12. [Utility Features](#12-utility-features)
13. [Troubleshooting](#13-troubleshooting)

---

## 1. Project Overview

The Inventory Management System (IMS) is a comprehensive Spring Boot application designed to manage inventory operations including:

- **Parameterization**: Organization setup, calendar management, categories, companies, branches, departments, and items
- **Operations**: Requisition slips, purchase orders, goods receiving notes, item issues, and maintenance
- **Reports**: Stock ledger, purchase order reports, requisition reports, goods issue reports
- **Utilities**: Calculator, calendar, dictionary, code generator, notes, reminders, unit converter, and access control

---

## 2. Technology Stack

### Backend
| Technology | Version/Details |
|------------|-----------------|
| Java | JDK 17+ |
| Spring Boot | 3.x |
| Spring Data JPA | Hibernate ORM |
| MySQL | 8.x |
| Thymeleaf | Template Engine |
| Lombok | Boilerplate Reduction |

### Frontend
| Technology | Purpose |
|------------|---------|
| HTML5/CSS3 | Structure & Styling |
| JavaScript (ES6+) | Dynamic Functionality |
| Thymeleaf | Server-side Rendering |

### Security
- AES-256 Encryption for sensitive data
- Custom password generation

---

## 3. Getting Started

### Prerequisites

1. **Java Development Kit (JDK) 17 or higher**
   ```bash
   java -version
   ```

2. **MySQL Server 8.x**
   ```bash
   mysql --version
   ```

3. **Maven 3.6+** (or use included Maven wrapper)
   ```bash
   mvn -version
   ```

### Installation Steps

#### Step 1: Clone/Download the Project
```bash
cd d:\download\inventory-management-system
```

#### Step 2: Configure Database

Edit `src/main/resources/application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/ims?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# Server Port
server.port=8081

# Encryption Key (32 characters required)
encryption.secretKey=X7v9LmP2Qr8TyZ4Nk5WsBdFgHjCaVe3Q

# File Upload Limits
spring.servlet.multipart.max-file-size=20MB
spring.servlet.multipart.max-request-size=20MB
```

#### Step 3: Start MySQL Service
```bash
# Windows
net start mysql

# Or using MySQL Workbench/XAMPP
```

#### Step 4: Build and Run the Application

**Using Maven:**
```bash
# Navigate to project root
cd d:\download\inventory-management-system

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

**Using IDE (IntelliJ IDEA / Eclipse):**
1. Import as Maven project
2. Run `InventoryManagementSystemApplication.java`

#### Step 5: Access the Application

Once started, the application will display:
```
Local: http://localhost:8081
External: http://<your-ip>:8081
```

Open your browser and navigate to: **http://localhost:8081**

---

## 4. Project Structure

```
inventory-management-system/
├── src/
│   ├── main/
│   │   ├── java/com/InventoryManagementSystem/
│   │   │   ├── Controller/          # REST & MVC Controllers
│   │   │   ├── Dto/                  # Data Transfer Objects
│   │   │   ├── Model/                # JPA Entity Classes
│   │   │   ├── Repository/           # Spring Data Repositories
│   │   │   ├── Service/              # Business Logic Services
│   │   │   ├── Util/                 # Utility Classes
│   │   │   └── InventoryManagementSystemApplication.java
│   │   └── resources/
│   │       ├── static/
│   │       │   ├── css/              # Stylesheets
│   │       │   └── js/               # JavaScript files
│   │       ├── templates/            # Thymeleaf HTML templates
│   │       ├── application.properties
│   │       └── banner.txt
│   └── test/                         # Test classes
├── pom.xml                           # Maven dependencies
└── DOCUMENTATION.md                  # This file
```

---

## 5. Database Configuration

### Default Credentials

| Property | Value |
|----------|-------|
| Database URL | `jdbc:mysql://localhost:3306/ims` |
| Username | `root` |
| Password | *(empty)* |
| Database Name | `ims` (auto-created) |

### Entity Relationship Overview

```
Organization (1) ─────────────────────────────────────
     │
Branch (N) ──────────── Department (N)
     │                        │
     └──────── Employee ──────┘
                   │
              Role ─┴─ Permissions

Category ─── CategoryType ─── CodeName
     │
   Item ─────── ItemDetails
     │              │
     │         ReceiveItems ─── Warranty
     │              │           Insurance
     │              │           DamageItems
     │              │
Requisition ── RequestItem
     │
PurchaseOrder ── PurchaseItem
     │
GoodsReceivingNote ── AdditionalCosts
     │
ItemIssue ── IssuedItem

Calender ─── NepaliFiscalYear
         ─── EnglishFiscalYear
```

### Key Tables

| Table | Purpose |
|-------|---------|
| `organization` | Main organization settings |
| `branch` | Company branches/locations |
| `department` | Organizational departments |
| `employee` | Staff/user records |
| `role` | User roles (ADMIN, USER, etc.) |
| `permissions` | Role-based permissions |
| `category` | Item categories |
| `category_type` | Category classifications |
| `item` | Inventory items |
| `requisition` | Item requisition requests |
| `purchase_order` | Purchase orders |
| `goods_receiving_note` | Received goods tracking |
| `item_issue` | Item distribution records |
| `calender` | Nepali/English calendar mapping |

---

## 6. Authentication & Security

### User Authentication

**Default Login Credentials:**
> ⚠️ **Note**: The system generates passwords automatically for employees.

**Password Generation Formula:**
```
Password = FirstName (lowercase) + LastName (lowercase) + Last 4 digits of mobile
```

**Example:**
- First Name: `John`
- Last Name: `Doe`
- Mobile: `9841234567`
- Generated Password: `johndoe4567`

### AES Encryption

The system uses AES-256 encryption for sensitive data.

**Configuration:**
```properties
encryption.secretKey=X7v9LmP2Qr8TyZ4Nk5WsBdFgHjCaVe3Q
```

**Usage in Code:**
```java
@Autowired
private AESEncryption aesEncryption;

// Encrypt
String encrypted = aesEncryption.encrypt("sensitive-data");

// Decrypt
String decrypted = aesEncryption.decrypt(encrypted);
```

### Role-Based Access Control

**Permission Flags:**
| Permission | Description |
|------------|-------------|
| `request` | Can create requisitions |
| `approve` | Can approve requests |
| `purchase` | Can create purchase orders |
| `users` | Can manage users |
| `staff` | Can manage staff |

**Seeded Permissions:**
The `PermissionsSeeder` automatically creates all 32 permission combinations on first run.

---

## 7. API Reference

### Base URL
```
http://localhost:8081/api
```

### Organization APIs

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/organization/submit-organization` | Create organization |
| PUT | `/organization/update-organization/{id}` | Update organization |
| GET | `/organization/get-organization` | Get organization details |
| PUT | `/organization/update-inventorymethod` | Update inventory method |
| GET | `/organization/get-inventorymethod` | Get inventory method |

**Request Example (Create Organization):**
```http
POST /organization/submit-organization
Content-Type: multipart/form-data

orgName: My Company
address: Kathmandu, Nepal
mobile: 9841234567
alternateMobile: 9851234567
email: info@company.com
registrationNumber: REG-12345
panVatNumber: PAN-67890
inventoryMethod: FIFO
logo: [file]
```

---

### Branch APIs

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/branch/setup` | Create branch |
| GET | `/api/branch/branch-list` | List all branches |
| GET | `/api/branch/branch/{id}` | Get branch by ID |
| PUT | `/api/branch/update/{id}` | Update branch |
| DELETE | `/api/branch/delete/{id}` | Delete branch |
| GET | `/api/branch/types` | Get branch types |
| GET | `/api/branch/generate/branch-generate` | Get auto-generate settings |

**Request Example (Create Branch):**
```json
POST /api/branch/setup
{
  "branchName": "Kathmandu Branch",
  "branchManager": "John Doe",
  "branchPhone": "01-4123456",
  "branchEmail": "ktm@company.com",
  "contactPersonName": "Jane Doe",
  "contactPersonPhone": "9841234567"
}
```

---

### Department APIs

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/department/setup` | Create department |
| GET | `/api/department/department-list` | List all departments |
| GET | `/api/department/department/{id}` | Get department by ID |
| PUT | `/api/department/update/{id}` | Update department |
| DELETE | `/api/department/delete/{id}` | Delete department |

**Request Example:**
```json
POST /api/department/setup
{
  "departmentName": "Human Resources"
}
```

---

### Category APIs

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/category/category-save` | Create category |
| GET | `/api/category/category-list` | List all categories |
| GET | `/api/category/categorytypes` | Get category types |
| PUT | `/api/category/category-update/{id}` | Update category |
| DELETE | `/api/category/category-delete/{id}` | Delete category |

---

### Category Type APIs

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/categorytype/save` | Create category type (single or bulk) |

**Single Save:**
```json
POST /api/categorytype/save
{
  "category": "Electronics",
  "subCategory": "Mobile Phones"
}
```

**Bulk Save:**
```json
POST /api/categorytype/save
[
  {"category": "Electronics", "subCategory": "Mobile Phones"},
  {"category": "Electronics", "subCategory": "Laptops"}
]
```

---

### Item APIs

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/item/submit-item` | Create item |
| GET | `/api/item/get-items` | List all items |
| GET | `/api/item/get-item/{id}` | Get item by ID |
| PUT | `/api/item/update-item/{id}` | Update item |
| DELETE | `/api/item/delete-item/{id}` | Delete item |

**Request Example:**
```json
POST /api/item/submit-item
{
  "itemName": "Dell Laptop",
  "categoryId": 1,
  "branchId": 1,
  "location": "Warehouse A",
  "minimumOrderLevel": 5,
  "reorderLevel": 10,
  "maximumOrderLevel": 50,
  "unit": "pcs"
}
```

---

### Company APIs

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/company/company-save` | Create company |
| GET | `/api/company/company-list` | List all companies |

---

### Employee APIs

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/employee/save` | Create employee |
| GET | `/api/branches/list` | Get branches for dropdown |
| GET | `/api/departments/department-list` | Get departments for dropdown |
| GET | `/api/roles/role-list` | Get roles for dropdown |

---

### Role APIs

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/save-roles` | Create role |

**Request Example:**
```json
POST /api/save-roles
{
  "roleName": "Manager"
}
```

---

### Calendar APIs

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/calendar/save` | Save calendar entries |
| GET | `/api/calendar/all` | Get all calendar entries |
| GET | `/api/calendar/year/{bsYear}` | Get calendar by BS year |
| GET | `/api/calendar/month/{year}/{month}` | Get month calendar |
| PUT | `/api/calendar/delete/year/{bsYear}` | Delete calendar year |

---

### Fiscal Year APIs

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/fiscalyear/save` | Save fiscal year |

**Request Example:**
```json
POST /api/fiscalyear/save
{
  "fiscalBsStart": "2081-04-01",
  "fiscalBsEnd": "2082-03-31",
  "fiscalAdStart": "2024-07-16",
  "fiscalAdEnd": "2025-07-15"
}
```

---

### Code Generator APIs

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/generate` | Save/update generate settings |
| GET | `/api/generate?name={name}` | Get generate by name |
| POST | `/api/code-name/manual` | Save manual code |

---

### Access Control APIs

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/access/employees-list` | List all employees |
| GET | `/api/access/active-roles` | List active roles |

---

## 8. Frontend Routes

### Page Routes (HTML Views)

| Route | Page | Description |
|-------|------|-------------|
| `/` | Home | Landing page with login |
| `/login` | Login | User login page |

#### Parameterization
| Route | Page |
|-------|------|
| `/organizationsetup` | Organization Setup |
| `/setupCalendar` | Calendar Setup |
| `/fiscalyear` | Fiscal Year Entry |
| `/monthsetup` | Month Setup |
| `/categorydetails` | Category List |
| `/categoryform` | Add Category |
| `/categoryedit` | Edit Category |
| `/categorytype` | Category Type Setup |
| `/companydetails` | Company List |
| `/company` | Add Company |
| `/companyview` | View Company |
| `/branchdetails` | Branch List |
| `/branchsetup` | Add Branch |
| `/branchedit` | Edit Branch |
| `/branchview` | View Branch |
| `/departmentdetails` | Department List |
| `/departmentsetup` | Add Department |
| `/departmentedit` | Edit Department |
| `/itemview` | Item List |
| `/additemform` | Add Item |
| `/itemedit` | Edit Item |
| `/itemdetails` | View Item |

#### Operations
| Route | Page |
|-------|------|
| `/requisitionslip` | Create Requisition |
| `/purchaseorder` | Create Purchase Order |
| `/goodreceivingnote` | Goods Receiving Note |
| `/itemissueform` | Item Issue Form |
| `/maintainanceform` | Maintenance Form |

#### Reports
| Route | Page |
|-------|------|
| `/recentReports` | Activity Log |
| `/stockledgerreport` | Stock Ledger |
| `/stock_detail` | Stock Detail |
| `/purchaseorderreport` | Purchase Order Report |
| `/requisitionreport` | Requisition Report |
| `/goodissuereport` | Good Issue Report |
| `/goodreceivingnotereports` | GRN Reports |
| `/maintainancereport` | Maintenance Report |

#### Utilities
| Route | Page |
|-------|------|
| `/calculator` | Calculator |
| `/calender` | Calendar View |
| `/dictionary` | Dictionary |
| `/codegenerator` | Code Generator |
| `/notes` | Sticky Notes |
| `/reminder` | Reminders |
| `/unitconverter` | Unit Converter |
| `/accesscontrol` | Access Control |
| `/userregistration` | User Registration |
| `/companystaff` | Add Staff |
| `/companystaffdetails` | Staff List |
| `/roleform` | Add Role |
| `/inventorymethod` | Inventory Method |
| `/backup` | Backup & Restore |

---

## 9. Core Modules

### 9.1 Organization Module

Manages the main organization settings including name, contact details, logo, and inventory valuation method.

**Key Features:**
- Organization profile management
- Logo upload support
- Inventory method selection (FIFO, LIFO, Average)

**Files:**
- `OrganizationController.java`
- `OrganizationService.java`
- `Organization.java` (Model)
- `organizationsetup.js`

---

### 9.2 Branch Module

Manages company branches/locations with address and contact information.

**Key Features:**
- Auto-generated branch codes
- Address management (Country, Province, City)
- Contact person details
- Soft delete support

**Code Generation:**
Branch codes are generated as 3-digit numeric codes (001, 002, etc.)

**Files:**
- `BranchController.java`
- `BranchService.java`
- `Branch.java` (Model)

---

### 9.3 Department Module

Manages organizational departments.

**Key Features:**
- Auto-generated department codes
- Soft delete with code reuse
- Duplicate prevention

**Files:**
- `DepartmentController.java`
- `DepartmentService.java`
- `Department.java` (Model)

---

### 9.4 Category Module

Hierarchical category management with types and subcategories.

**Key Features:**
- Category types linking categories and subcategories
- Code-based identification
- Auto/manual code generation

**Hierarchy:**
```
CodeName (Category) → CodeName (SubCategory) → CategoryType → Category
```

**Files:**
- `CategoryController.java`
- `CategoryTypeController.java`
- `CategoryService.java`
- `CategoryTypeService.java`

---

### 9.5 Item Module

Core inventory item management.

**Key Features:**
- Stock level management (Min, Reorder, Max)
- Category association
- Branch assignment
- Unit tracking

**Stock Level Fields:**
| Field | Purpose |
|-------|---------|
| `minimumOrderLevel` | Alert threshold |
| `reorderLevel` | Reorder trigger point |
| `maximumOrderLevel` | Maximum stock capacity |

**Files:**
- `ItemController.java`
- `ItemService.java`
- `Item.java` (Model)

---

### 9.6 Calendar Module

Dual calendar support for Nepali (BS) and English (AD) dates.

**Key Features:**
- BS to AD date mapping
- Holiday marking
- Fiscal year association
- Month-wise data entry

**Files:**
- `CalenderController.java`
- `CalenderService.java`
- `Calender.java` (Model)
- `calender.js` (Frontend)

---

### 9.7 Employee/Staff Module

Employee management with role-based access.

**Key Features:**
- Auto-generated passwords
- Role assignment
- Branch/Department association
- Profile picture upload
- WhatsApp integration ready

**Files:**
- `EmployeeController.java`
- `EmployeeService.java`
- `Employee.java` (Model)

---

## 10. Important Functions & Services

### 10.1 Code Generation Utilities

#### `CodeGeneratorUtil.java`

**Purpose:** Generate unique codes for entities.

```java
// Generate unique two-letter code
public static String generateUniqueCode(
    String categoryType, 
    Generate generate,
    CodeNameRepository codeNameRepository, 
    String type
)

// Generate next sequential code
public static <T> String generateNextCode(
    String baseCode, 
    List<T> items, 
    Function<T, String> codeExtractor
)

// Generate 3-digit category code
public static String generateNextCategoryNumericCode(
    CodeNameRepository codeNameRepository
)
```

#### `NumericCodeGeneratorUtil.java`

**Purpose:** Generate numeric codes in format BBBSSS (6 digits).

```java
// Generate 6-digit code (e.g., 001001)
public static <T> String generateSixDigitCode(
    String baseThreeDigits, 
    List<T> items,
    Function<T, String> codeExtractor
)

// Generate next 3-digit code (001, 002, etc.)
public static <T> String generateNextThreeDigits(
    List<T> items, 
    Function<T, String> codeExtractor
)
```

---

### 10.2 File Upload Utility

#### `FileUploadUtil.java`

```java
public static String saveFile(String uploadDir, MultipartFile file) throws IOException
```

**Usage:**
```java
String fileName = FileUploadUtil.saveFile("uploads/logos", logoFile);
```

---

### 10.3 AES Encryption

#### `AESEncryption.java`

```java
// Encrypt plaintext
public String encrypt(String plainText)

// Decrypt ciphertext
public String decrypt(String encryptedText)
```

**Configuration Required:**
- 32-character secret key in `application.properties`

---

### 10.4 Service Layer Important Methods

#### BranchService
```java
// Create or restore branch
public ResponseEntity<?> handleBranchSetup(BranchDto dto, ...)

// Soft delete branch
public void deleteBranch(Long id)
```

#### DepartmentService
```java
// Create department with soft-delete handling
public ResponseEntity<?> handleDepartmentSetup(DepartmentDto dto)

// Reuses deleted department codes
```

#### CategoryService
```java
// Save category with auto-code generation
public void saveCategory(CategoryDto dto)

// Suggest categories by name
public List<CategoryDto> suggestCategoriesByName(String q)
```

#### ItemService
```java
// Save item with code generation
public void saveItem(ItemDto dto)

// Convert entity to DTO
public ItemDto toDto(Item item)
```

#### EmployeeService
```java
// Save employee with auto-password
public ResponseEntity<?> saveEmployee(EmployeeDto dto, MultipartFile image)

// Password formula: firstName + lastName + last4MobileDigits
private String generatePassword(EmployeeDto dto)
```

---

## 11. Form Configuration System

The application uses a dynamic form rendering system based on JavaScript configuration objects.

### Configuration Structure

```javascript
window.formConfig = {
    title: "Form Title",
    method: "POST",
    submitAPI: "/api/endpoint",
    fetchAPI: "/api/fetch-data",  // For edit forms
    id: "formId",
    fields: [...],
    buttons: [...],
    tables: [...]  // Optional
};
```

### Field Types

| Type | Description |
|------|-------------|
| `text` | Text input |
| `number` | Number input |
| `email` | Email input |
| `password` | Password input |
| `date` | Date picker |
| `select` | Dropdown select |
| `textarea` | Multi-line text |
| `checkbox` | Single checkbox |
| `checkbox-group` | Multiple checkboxes |
| `radioGroup` | Radio buttons |
| `file` | File upload |
| `tagInput` | Tag/chip input |
| `group` | Field grouping |

### Example Configuration

```javascript
window.formConfig = {
    title: "Add New Branch",
    method: "POST",
    submitAPI: "/api/branch/setup",
    fields: [
        {
            type: "text",
            label: "Branch Name",
            name: "branchName",
            required: true,
            placeholder: "Enter branch name",
            group: 1
        },
        {
            type: "text",
            label: "Manager",
            name: "branchManager",
            placeholder: "Enter manager name",
            group: 1
        },
        {
            type: "email",
            label: "Email",
            name: "branchEmail",
            required: true,
            group: 2
        },
        {
            type: "tel",
            label: "Phone",
            name: "branchPhone",
            pattern: "[0-9]{10}",
            group: 2
        },
        {
            type: "select",
            label: "Branch Type",
            name: "branchTypeCode",
            required: true,
            optionsAPI: "/api/branch/types",
            optionValue: "code",
            optionLabel: "codeName",
            group: 3
        }
    ],
    buttons: [
        { 
            type: "submit", 
            label: "Save", 
            position: "bottom", 
            class: "btn btn-primary" 
        },
        { 
            type: "button", 
            label: "Cancel", 
            position: "bottom", 
            redirect: "/branchdetails",
            class: "btn btn-secondary" 
        }
    ]
};
```

### Core JavaScript Classes

| Class | File | Purpose |
|-------|------|---------|
| `FormRenderer` | `FromRenderer.js` | Renders form HTML |
| `FieldGenerator` | `FieldGenerator.js` | Generates field HTML |
| `FieldValidation` | `FieldValidation.js` | Field validation |
| `FormHandler` | `FormHandler.js` | Form submission |
| `TableGenerator` | `TableGenerator.js` | Dynamic tables |
| `TagInput` | `TagInput.js` | Tag input component |
| `TableManager` | `dummy.js` | Table rendering & actions |
| `Main` | `Main.js` | Initialization |

---

## 12. Utility Features

### 12.1 Calculator
- Basic arithmetic operations
- Memory functions (MC, MR, M+, M-)
- Square root and percentage
- **Route:** `/calculator`

### 12.2 Dictionary
- Word lookup using Dictionary API
- Pronunciation audio
- Search history
- **Route:** `/dictionary`

### 12.3 Unit Converter
- Length conversions
- Weight conversions
- Currency conversion (real-time rates)
- **Route:** `/unitconverter`

### 12.4 Sticky Notes
- Create/edit/delete notes
- Rich text formatting
- Image insertion
- Local storage persistence
- **Route:** `/notes`

### 12.5 Reminder
- Date/time based reminders
- Browser notifications
- Persistent storage
- **Route:** `/reminder`

### 12.6 Calendar View
- Nepali calendar display
- Holiday highlighting
- Month/year navigation
- **Route:** `/calender`

### 12.7 Code Generator
- Auto/manual code generation per entity
- Configurable prefix codes
- **Route:** `/codegenerator`

### 12.8 Access Control
- User management
- Role assignment
- Permission management
- User suspension
- **Route:** `/accesscontrol`

---

## 13. Troubleshooting

### Common Issues

#### 1. Database Connection Error
```
Cannot create connection to database server
```
**Solution:**
- Verify MySQL is running
- Check credentials in `application.properties`
- Ensure database user has proper permissions

#### 2. Port Already in Use
```
Port 8081 is already in use
```
**Solution:**
```bash
# Find process using port
netstat -ano | findstr :8081

# Kill process (Windows)
taskkill /PID <PID> /F

# Or change port in application.properties
server.port=8082
```

#### 3. File Upload Issues
```
Maximum upload size exceeded
```
**Solution:**
Check `application.properties`:
```properties
spring.servlet.multipart.max-file-size=20MB
spring.servlet.multipart.max-request-size=20MB
```

#### 4. Encryption Key Error
```
Invalid AES key length
```
**Solution:**
Ensure `encryption.secretKey` is exactly 32 characters.

#### 5. Form Not Rendering
**Solution:**
- Check browser console for JavaScript errors
- Verify `formConfig` is defined before form scripts load
- Ensure all required JS files are loaded

### Logs Location
Application logs are output to console by default. Enable file logging:
```properties
logging.file.name=logs/ims.log
logging.level.root=INFO
logging.level.com.InventoryManagementSystem=DEBUG
```

---

## Quick Reference Card

### Start Application
```bash
cd d:\download\inventory-management-system
mvn spring-boot:run
```

### Access URLs
- **Application:** http://localhost:8081
- **API Base:** http://localhost:8081/api

### Database
- **Host:** localhost:3306
- **Database:** ims
- **User:** root
- **Password:** *(empty)*

### Key Files
| Purpose | Location |
|---------|----------|
| Configuration | `src/main/resources/application.properties` |
| Main Class | `InventoryManagementSystemApplication.java` |
| Controllers | `src/main/java/.../Controller/` |
| Services | `src/main/java/.../Service/` |
| Templates | `src/main/resources/templates/` |
| Static Files | `src/main/resources/static/` |

---

## Version Information

- **Document Version:** 1.0
- **Last Updated:** January 29, 2026
- **Application Version:** Based on current codebase
- **Spring Boot:** 3.x
- **Java:** 17+

---