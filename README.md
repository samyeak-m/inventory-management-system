# Inventory Management System (IMS)

> A comprehensive Spring Boot-based web application for managing inventory operations, including organization setup, item management, requisitions, purchase orders, goods receiving, and comprehensive reporting.

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Quick Start](#quick-start)
- [Default Credentials](#default-credentials)
- [Documentation](#documentation)
- [Project Structure](#project-structure)
- [Contributing](#contributing)
- [License](#license)

## 🎯 Overview

The Inventory Management System (IMS) is a full-stack enterprise application designed to streamline inventory operations for organizations with multiple branches and departments. It provides end-to-end functionality from organization setup to detailed reporting, with role-based access control and dual calendar support (Nepali BS and English AD).

## ✨ Features

### Core Modules
- **Parameterization**
  - Organization setup with logo management
  - Multi-branch and department management
  - Hierarchical category and subcategory system
  - Item master data with stock levels
  - Company and supplier management

- **Operations**
  - Requisition slip creation and approval
  - Purchase order generation
  - Goods receiving note (GRN) processing
  - Item issue tracking
  - Maintenance record management

- **Reporting**
  - Stock ledger reports
  - Purchase order reports
  - Requisition reports
  - Goods issue reports
  - Maintenance reports
  - Recent activity logs

- **Utilities**
  - Built-in calculator
  - Dual calendar (Nepali/English)
  - Dictionary lookup
  - Code generator
  - Sticky notes
  - Reminders with notifications
  - Unit converter
  - Access control and user management

### Security Features
- AES-256 encryption for sensitive data
- Automatic password generation
- Role-based access control (RBAC)
- Permission management system
- User suspension capabilities

## 🛠️ Technology Stack

### Backend
- **Java 17+** - Core language
- **Spring Boot 3.5.0** - Application framework
- **Spring Data JPA** - ORM with Hibernate
- **MySQL 8.x** - Database
- **Thymeleaf** - Server-side template engine
- **Lombok** - Boilerplate reduction

### Frontend
- **HTML5/CSS3** - Structure and styling
- **JavaScript (ES6+)** - Dynamic functionality
- **Thymeleaf** - Server-side rendering

### Key Dependencies
- Nepali Calendar API
- Jackson for JSON processing
- File upload utilities
- WhatsApp Business API integration (optional)

## 🚀 Quick Start

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

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd inventory-management-system
   ```

2. **Configure the database**
   
   Edit `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/ims?createDatabaseIfNotExist=true
   spring.datasource.username=root
   spring.datasource.password=
   ```

3. **Start MySQL service**
   ```bash
   # Windows
   net start mysql
   
   # Linux/Mac
   sudo systemctl start mysql
   ```

4. **Build and run the application**
   
   Using Maven:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```
   
   Or using the Maven wrapper:
   ```bash
   ./mvnw clean install
   ./mvnw spring-boot:run
   ```

5. **Access the application**
   
   Open your browser and navigate to:
   ```
   http://localhost:8081
   ```

## 🔐 Default Credentials

### Password Generation Formula

The system automatically generates passwords for employees using the following formula:

```
Password = firstName (lowercase) + lastName (lowercase) + last 4 digits of mobile
```

**Examples:**
| First Name | Last Name | Mobile | Generated Password |
|------------|-----------|--------|-------------------|
| John | Doe | 9841234567 | johndoe4567 |
| Sarah | Smith | 9851112233 | sarahsmith2233 |
| Ram | Sharma | 9801234567 | ramsharma4567 |

### Default Database Credentials

| Property | Value |
|----------|-------|
| Host | localhost:3306 |
| Database | ims (auto-created) |
| Username | root |
| Password | *(empty)* |

## 📖 Documentation

For complete documentation, please refer to [DOCUMENTATION.md](DOCUMENTATION.md), which includes:

- Detailed installation instructions
- Complete API reference with examples
- Database schema and relationships
- Frontend routes and pages
- Important functions and services
- Form configuration system
- Troubleshooting guide
- Security best practices

### Quick Links

- [API Reference](DOCUMENTATION.md#7-api-reference)
- [Getting Started Guide](DOCUMENTATION.md#3-getting-started)
- [Authentication & Security](DOCUMENTATION.md#6-authentication--security)
- [Core Modules](DOCUMENTATION.md#9-core-modules)
- [Troubleshooting](DOCUMENTATION.md#13-troubleshooting)

## 📁 Project Structure

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
│   │       ├── static/               # CSS, JS, Images
│   │       ├── templates/            # Thymeleaf HTML templates
│   │       └── application.properties
│   └── test/                         # Test classes
├── pom.xml                           # Maven dependencies
├── README.md                         # This file
└── DOCUMENTATION.md                  # Complete documentation
```

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the project
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- Nepali Calendar API contributors
- All contributors who have helped improve this project

---

**Note:** For complete API documentation, configuration details, and troubleshooting guides, please refer to [DOCUMENTATION.md](DOCUMENTATION.md).
