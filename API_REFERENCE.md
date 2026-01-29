# API Reference Guide - Inventory Management System

> Complete API endpoint reference for the Inventory Management System

## Base URL

```
http://localhost:8081
```

## Table of Contents

- [Authentication](#authentication)
- [Organization APIs](#organization-apis)
- [Branch APIs](#branch-apis)
- [Department APIs](#department-apis)
- [Category APIs](#category-apis)
- [Category Type APIs](#category-type-apis)
- [Item APIs](#item-apis)
- [Company APIs](#company-apis)
- [Employee APIs](#employee-apis)
- [Role APIs](#role-apis)
- [Calendar APIs](#calendar-apis)
- [Fiscal Year APIs](#fiscal-year-apis)
- [Code Generator APIs](#code-generator-apis)
- [Access Control APIs](#access-control-apis)

---

## Authentication

**Note:** Current version does not require token-based authentication. Future versions may implement JWT or OAuth2.

---

## Organization APIs

### 1. Create Organization

Creates or updates the main organization profile.

**Endpoint:** `POST /organization/submit-organization`

**Content-Type:** `multipart/form-data`

**Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| orgName | String | Yes | Organization name |
| address | String | Yes | Full address |
| mobile | String | Yes | Primary contact number |
| alternateMobile | String | No | Alternative contact |
| email | String | Yes | Organization email |
| registrationNumber | String | No | Business registration number |
| panVatNumber | String | No | Tax identification number |
| inventoryMethod | String | Yes | FIFO, LIFO, or Average |
| logo | File | No | Organization logo image |

**Example Request:**
```bash
curl -X POST http://localhost:8081/organization/submit-organization \
  -F "orgName=ABC Corporation" \
  -F "address=Kathmandu, Nepal" \
  -F "mobile=9841234567" \
  -F "email=info@abc.com" \
  -F "inventoryMethod=FIFO" \
  -F "logo=@/path/to/logo.png"
```

### 2. Get Organization

Retrieves organization details.

**Endpoint:** `GET /organization/get-organization`

**Example Request:**
```bash
curl -X GET http://localhost:8081/organization/get-organization
```

**Response:**
```json
{
  "id": 1,
  "orgName": "ABC Corporation",
  "address": "Kathmandu, Nepal",
  "mobile": "9841234567",
  "alternateMobile": "9851234567",
  "email": "info@abc.com",
  "registrationNumber": "REG-12345",
  "panVatNumber": "PAN-67890",
  "inventoryMethod": "FIFO",
  "logo": "/uploads/logos/abc_logo.png",
  "createdAt": "2026-01-15T10:30:00",
  "updatedAt": "2026-01-29T14:00:00"
}
```

### 3. Update Inventory Method

Updates the inventory valuation method.

**Endpoint:** `PUT /organization/update-inventorymethod`

**Content-Type:** `application/json`

**Request Body:**
```json
{
  "inventoryMethod": "LIFO"
}
```

### 4. Get Inventory Method

Gets current inventory method.

**Endpoint:** `GET /organization/get-inventorymethod`

**Response:**
```json
{
  "inventoryMethod": "FIFO"
}
```

---

## Branch APIs

### 1. Create Branch

Creates a new branch location.

**Endpoint:** `POST /api/branch/setup`

**Content-Type:** `application/json`

**Request Body:**
```json
{
  "branchName": "Kathmandu Branch",
  "branchTypeCode": "HQ",
  "branchManager": "John Doe",
  "branchPhone": "01-4123456",
  "branchEmail": "ktm@company.com",
  "country": "Nepal",
  "province": "Bagmati",
  "city": "Kathmandu",
  "contactPersonName": "Jane Smith",
  "contactPersonPhone": "9841234567",
  "contactPersonEmail": "jane@company.com"
}
```

**Success Response:**
```json
{
  "message": "Branch created successfully",
  "branch": {
    "id": 1,
    "branchCode": "001",
    "branchName": "Kathmandu Branch",
    "branchTypeCode": "HQ",
    "branchManager": "John Doe",
    "branchPhone": "01-4123456",
    "branchEmail": "ktm@company.com",
    "country": "Nepal",
    "province": "Bagmati",
    "city": "Kathmandu",
    "display": true,
    "createdAt": "2026-01-29T14:30:00"
  }
}
```

### 2. List All Branches

Gets all active branches.

**Endpoint:** `GET /api/branch/branch-list`

**Response:**
```json
[
  {
    "id": 1,
    "branchCode": "001",
    "branchName": "Kathmandu Branch",
    "branchManager": "John Doe",
    "branchEmail": "ktm@company.com",
    "display": true
  },
  {
    "id": 2,
    "branchCode": "002",
    "branchName": "Pokhara Branch",
    "branchManager": "Sarah Smith",
    "branchEmail": "pkr@company.com",
    "display": true
  }
]
```

### 3. Get Branch by ID

**Endpoint:** `GET /api/branch/branch/{id}`

**Example:** `GET /api/branch/branch/1`

### 4. Update Branch

**Endpoint:** `PUT /api/branch/update/{id}`

**Content-Type:** `application/json`

**Request Body:** Same as create branch

### 5. Delete Branch

Soft deletes a branch.

**Endpoint:** `DELETE /api/branch/delete/{id}`

**Example:** `DELETE /api/branch/delete/1`

### 6. Get Branch Types

Gets available branch type codes.

**Endpoint:** `GET /api/branch/types`

**Response:**
```json
[
  {
    "code": "HQ",
    "codeName": "Headquarters"
  },
  {
    "code": "REG",
    "codeName": "Regional Office"
  },
  {
    "code": "WAR",
    "codeName": "Warehouse"
  }
]
```

---

## Department APIs

### 1. Create Department

**Endpoint:** `POST /api/department/setup`

**Request Body:**
```json
{
  "departmentName": "Information Technology"
}
```

**Response:**
```json
{
  "message": "Department created successfully",
  "department": {
    "id": 1,
    "departmentCode": "001",
    "departmentName": "Information Technology",
    "display": true,
    "createdAt": "2026-01-29T15:00:00"
  }
}
```

### 2. List All Departments

**Endpoint:** `GET /api/department/department-list`

### 3. Get Department by ID

**Endpoint:** `GET /api/department/department/{id}`

### 4. Update Department

**Endpoint:** `PUT /api/department/update/{id}`

### 5. Delete Department

**Endpoint:** `DELETE /api/department/delete/{id}`

---

## Category APIs

### 1. Create Category

**Endpoint:** `POST /api/category/category-save`

**Single Category:**
```json
{
  "categoryName": "Office Furniture",
  "categoryTypeCode": "FUR001"
}
```

**Multiple Categories (Bulk):**
```json
[
  {
    "categoryName": "Office Furniture",
    "categoryTypeCode": "FUR001"
  },
  {
    "categoryName": "Electronic Devices",
    "categoryTypeCode": "ELE001"
  }
]
```

### 2. List Categories

**Endpoint:** `GET /api/category/category-list`

### 3. Get Category by ID

**Endpoint:** `GET /api/category/category/{id}`

### 4. Update Category

**Endpoint:** `PUT /api/category/category-update/{id}`

### 5. Delete Category

**Endpoint:** `DELETE /api/category/category-delete/{id}`

### 6. Get Category Types

**Endpoint:** `GET /api/category/categorytypes`

### 7. Suggest Categories

Search/autocomplete for categories.

**Endpoint:** `GET /api/category/category-suggest?q={searchTerm}`

**Example:** `GET /api/category/category-suggest?q=office`

---

## Category Type APIs

### 1. Save Category Type

**Endpoint:** `POST /api/categorytype/save`

**Single Entry:**
```json
{
  "category": "Electronics",
  "subCategory": "Laptops"
}
```

**Bulk Entry:**
```json
[
  {
    "category": "Electronics",
    "subCategory": "Laptops"
  },
  {
    "category": "Electronics",
    "subCategory": "Desktops"
  },
  {
    "category": "Electronics",
    "subCategory": "Monitors"
  }
]
```

---

## Item APIs

### 1. Create Item

**Endpoint:** `POST /api/item/submit-item`

**Single Item:**
```json
{
  "itemName": "Dell Latitude 5420",
  "categoryCode": "ELE001",
  "branchCode": "001",
  "location": "Warehouse A - Shelf 3",
  "minimumOrderLevel": 5,
  "reorderLevel": 10,
  "maximumOrderLevel": 50,
  "unit": "pieces",
  "description": "14-inch business laptop"
}
```

**Multiple Items:**
```json
[
  {
    "itemName": "HP Laptop",
    "categoryCode": "ELE001",
    "branchCode": "001",
    "location": "Warehouse A",
    "minimumOrderLevel": 5,
    "reorderLevel": 10,
    "maximumOrderLevel": 30,
    "unit": "pieces"
  },
  {
    "itemName": "Logitech Mouse",
    "categoryCode": "ACC001",
    "branchCode": "001",
    "location": "Warehouse B",
    "minimumOrderLevel": 20,
    "reorderLevel": 50,
    "maximumOrderLevel": 200,
    "unit": "pieces"
  }
]
```

### 2. List Items

**Endpoint:** `GET /api/item/get-items`

### 3. Get Item by ID

**Endpoint:** `GET /api/item/get-item/{id}`

### 4. Update Item

**Endpoint:** `PUT /api/item/update-item/{id}`

### 5. Delete Item

**Endpoint:** `DELETE /api/item/delete-item/{id}`

---

## Company APIs

### 1. Create Company (Simple)

**Endpoint:** `POST /api/company/company-save`

**Request Body:**
```json
{
  "companyName": "XYZ Suppliers Ltd",
  "companyTypeCode": "SUP",
  "address": "Lalitpur, Nepal",
  "mobile": "9841111222",
  "email": "contact@xyz.com"
}
```

### 2. Create Company (Advanced with Categories)

**Endpoint:** `POST /api/company/save`

**Request Body:**
```json
{
  "companyName": "ABC Electronics",
  "companyTypeCode": "SUP",
  "address": "Kathmandu",
  "mobile": "9841234567",
  "email": "info@abc.com",
  "selectedCategoryList": ["ELE001", "ACC001"],
  "selectedItemList": ["001001", "001002"]
}
```

### 3. List Companies

**Endpoint:** `GET /api/company/company-list`

### 4. Get Company Types

**Endpoint:** `GET /api/company/company-types`

### 5. Generate Next Company Code

**Endpoint:** `GET /api/company/next-company-code`

---

## Employee APIs

### 1. Create Employee

**Endpoint:** `POST /api/employees/save`

**Content-Type:** `multipart/form-data`

**Form Fields:**

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| firstname | String | Yes | First name |
| middleName | String | No | Middle name |
| lastName | String | Yes | Last name |
| email | String | Yes | Email address (unique) |
| mobile | String | No | Mobile number (unique) |
| gender | String | No | Male/Female/Other |
| type | String | No | Full-time/Part-time/Contract |
| branchId | Long | Yes | Branch ID |
| departmentId | Long | Yes | Department ID |
| userRoleId | Long | No | Role ID (for user registration) |
| permissionsId | Long | No | Permissions ID (for user registration) |
| image | File | No | Profile picture |

**Example with cURL:**
```bash
curl -X POST http://localhost:8081/api/employees/save \
  -F "firstname=John" \
  -F "lastName=Doe" \
  -F "email=john.doe@example.com" \
  -F "mobile=9841234567" \
  -F "gender=Male" \
  -F "type=Full-time" \
  -F "branchId=1" \
  -F "departmentId=1" \
  -F "userRoleId=2" \
  -F "permissionsId=5" \
  -F "image=@/path/to/photo.jpg"
```

**Response:**
```json
{
  "employee": {
    "id": 15,
    "firstname": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "mobile": "9841234567",
    "password": "johndoe4567",
    "status": "ACTIVE",
    "suspend": false,
    "picture": "/image/uploads/john_1643234567.jpg"
  },
  "message": "Employee created successfully.",
  "whatsappSent": true
}
```

**Password:** Auto-generated as `johndoe4567` (firstname + lastname + last 4 digits of mobile)

### 2. Get Branches for Dropdown

**Endpoint:** `GET /api/branches/list`

### 3. Get Departments for Dropdown

**Endpoint:** `GET /api/departments/department-list`

### 4. Get Roles for Dropdown

**Endpoint:** `GET /api/roles/role-list`

---

## Role APIs

### 1. Create Role

**Endpoint:** `POST /api/save-roles`

**Request Body:**
```json
{
  "roleName": "Warehouse Manager"
}
```

**Response:**
```json
{
  "message": "Role created successfully",
  "role": {
    "id": 5,
    "roleName": "Warehouse Manager",
    "display": true,
    "createdAt": "2026-01-29T16:00:00"
  }
}
```

---

## Calendar APIs

### 1. Save Calendar

**Endpoint:** `POST /api/calendar/save`

**Request Body:**
```json
[
  {
    "bsDate": "2081-10-15",
    "adDate": "2025-01-29",
    "bsYear": "2081",
    "bsMonth": "10",
    "bsDay": "15",
    "adYear": "2025",
    "adMonth": "01",
    "adDay": "29",
    "dayOfWeek": "Wednesday",
    "isHoliday": false,
    "holidayName": null
  }
]
```

### 2. Get All Calendars

**Endpoint:** `GET /api/calendar/all`

### 3. Get Calendar by Year

**Endpoint:** `GET /api/calendar/year/{bsYear}`

**Example:** `GET /api/calendar/year/2081`

### 4. Get Calendar by Month

**Endpoint:** `GET /api/calendar/month/{year}/{month}`

**Example:** `GET /api/calendar/month/2081/10`

### 5. Delete Calendar Year

**Endpoint:** `PUT /api/calendar/delete/year/{bsYear}`

---

## Fiscal Year APIs

### 1. Save Fiscal Year

**Endpoint:** `POST /api/fiscalyear/save`

**Request Body:**
```json
{
  "fiscalBsStart": "2081-04-01",
  "fiscalBsEnd": "2082-03-31",
  "fiscalAdStart": "2024-07-16",
  "fiscalAdEnd": "2025-07-15"
}
```

---

## Code Generator APIs

### 1. Save Generate Settings

**Endpoint:** `POST /api/generate`

**Request Body:**
```json
{
  "name": "BRANCH",
  "autoGenerate": true,
  "prefix": "BR",
  "startFrom": 1
}
```

### 2. Get Generate Settings

**Endpoint:** `GET /api/generate?name={name}`

**Example:** `GET /api/generate?name=BRANCH`

### 3. Save Manual Code

**Endpoint:** `POST /api/code-name/manual`

**Request Body:**
```json
{
  "code": "BR001",
  "codeName": "Kathmandu Branch",
  "type": "BRANCH"
}
```

---

## Access Control APIs

### 1. List All Employees

**Endpoint:** `GET /api/access/employees-list`

**Response:**
```json
[
  {
    "id": 1,
    "firstname": "John",
    "lastName": "Doe",
    "email": "john@example.com",
    "mobile": "9841234567",
    "status": "ACTIVE",
    "suspend": false,
    "userRole": {
      "id": 2,
      "roleName": "Manager"
    },
    "branch": {
      "id": 1,
      "branchName": "Kathmandu"
    },
    "department": {
      "id": 1,
      "departmentName": "IT"
    }
  }
]
```

### 2. List Active Roles

**Endpoint:** `GET /api/access/active-roles`

**Response:**
```json
[
  {
    "id": 1,
    "roleName": "Administrator",
    "display": true
  },
  {
    "id": 2,
    "roleName": "Manager",
    "display": true
  }
]
```

---

## Common Response Codes

| Code | Meaning | Description |
|------|---------|-------------|
| 200 | OK | Request successful |
| 201 | Created | Resource created successfully |
| 400 | Bad Request | Invalid request parameters |
| 404 | Not Found | Resource not found |
| 409 | Conflict | Duplicate entry (email, code, etc.) |
| 500 | Internal Server Error | Server error |

---

## Error Response Format

```json
{
  "message": "Email already exists.",
  "timestamp": "2026-01-29T16:30:00",
  "status": 400
}
```

---

## Testing with Postman

### Import Collection Steps:

1. Create new collection "IMS API"
2. Set environment variable: `base_url` = `http://localhost:8081`
3. Use `{{base_url}}` in all requests
4. Test endpoints in this order:
   - Organization setup
   - Branch creation
   - Department creation
   - Category setup
   - Item creation
   - Employee registration

### Example Postman Request:

**Create Branch:**
- Method: POST
- URL: `{{base_url}}/api/branch/setup`
- Headers: `Content-Type: application/json`
- Body (raw JSON):
```json
{
  "branchName": "Test Branch",
  "branchManager": "Test Manager",
  "branchEmail": "test@example.com"
}
```

---

## Notes

- All timestamps are in ISO 8601 format
- Date format: `YYYY-MM-DD` for both BS and AD
- Phone numbers: 10 digits (Nepal format)
- Email must be unique across all employees
- Mobile number must be unique if provided
- Deleted records (soft delete) have `display=false`

---

**Version:** 1.0  
**Last Updated:** January 29, 2026
