# Changelog

All notable changes to the Inventory Management System will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added
- Comprehensive documentation suite
  - README.md with project overview and quick links
  - DOCUMENTATION.md with complete technical documentation
  - API_REFERENCE.md with detailed endpoint specifications
  - QUICKSTART.md with step-by-step setup guide
  - CHANGELOG.md for version tracking

### Enhanced
- Documentation includes:
  - Detailed password generation examples
  - Complete API request/response examples
  - Operations module workflows
  - Security best practices
  - Performance optimization guide
  - Backup and restore procedures
  - Deployment guide for production
  - Monitoring and maintenance guide
  - Common troubleshooting scenarios

## [0.0.1-SNAPSHOT] - 2026-01-29

### Features

#### Parameterization Module
- Organization setup with logo management
- Multi-branch management with auto-generated codes
- Department management
- Hierarchical category and subcategory system
- Item master data with stock level tracking
- Company and supplier management
- Dual calendar support (Nepali BS and English AD)
- Fiscal year management
- Code generator with auto/manual modes

#### Operations Module
- Requisition slip creation and approval workflow
- Purchase order generation
- Goods receiving note (GRN) processing
- Item issue tracking
- Maintenance record management
- Stock level monitoring

#### Reporting Module
- Stock ledger reports
- Purchase order reports
- Requisition reports  
- Goods issue reports
- GRN reports
- Maintenance reports
- Recent activity logs
- Stock detail reports

#### Utilities
- Built-in calculator
- Dual calendar (Nepali/English) view
- Dictionary lookup with pronunciation
- Code generator utility
- Sticky notes with rich text
- Reminders with browser notifications
- Unit converter (length, weight, currency)
- Access control and user management

#### Security Features
- AES-256 encryption for sensitive data
- Automatic password generation
- Role-based access control (RBAC)
- Permission management with 32 combinations
- User account suspension
- Soft delete for data integrity

#### Technical Features
- Spring Boot 3.5.0 framework
- MySQL 8.x database with JPA/Hibernate
- Thymeleaf template engine
- Dynamic form rendering system
- RESTful API architecture
- File upload support (up to 20MB)
- WhatsApp Business API integration (optional)
- Nepali calendar integration

### API Endpoints

#### Organization APIs
- POST `/organization/submit-organization` - Create/update organization
- GET `/organization/get-organization` - Get organization details
- PUT `/organization/update-inventorymethod` - Update inventory method
- GET `/organization/get-inventorymethod` - Get inventory method

#### Branch APIs  
- POST `/api/branch/setup` - Create branch
- GET `/api/branch/branch-list` - List all branches
- GET `/api/branch/branch/{id}` - Get branch by ID
- PUT `/api/branch/update/{id}` - Update branch
- DELETE `/api/branch/delete/{id}` - Delete branch
- GET `/api/branch/types` - Get branch types

#### Department APIs
- POST `/api/department/setup` - Create department
- GET `/api/department/department-list` - List departments
- GET `/api/department/department/{id}` - Get department by ID
- PUT `/api/department/update/{id}` - Update department
- DELETE `/api/department/delete/{id}` - Delete department

#### Category APIs
- POST `/api/category/category-save` - Create category (single/bulk)
- GET `/api/category/category-list` - List categories
- GET `/api/category/categorytypes` - Get category types
- PUT `/api/category/category-update/{id}` - Update category
- DELETE `/api/category/category-delete/{id}` - Delete category
- GET `/api/category/category-suggest` - Search categories

#### Item APIs
- POST `/api/item/submit-item` - Create item (single/bulk)
- GET `/api/item/get-items` - List items
- GET `/api/item/get-item/{id}` - Get item by ID
- PUT `/api/item/update-item/{id}` - Update item
- DELETE `/api/item/delete-item/{id}` - Delete item

#### Employee APIs
- POST `/api/employees/save` - Create employee
- GET `/api/branches/list` - Get branches dropdown
- GET `/api/departments/department-list` - Get departments dropdown
- GET `/api/roles/role-list` - Get roles dropdown

#### Calendar APIs
- POST `/api/calendar/save` - Save calendar entries
- GET `/api/calendar/all` - Get all calendars
- GET `/api/calendar/year/{bsYear}` - Get calendar by year
- GET `/api/calendar/month/{year}/{month}` - Get month calendar
- PUT `/api/calendar/delete/year/{bsYear}` - Delete calendar year

#### Fiscal Year APIs
- POST `/api/fiscalyear/save` - Save fiscal year

#### Access Control APIs
- GET `/api/access/employees-list` - List all employees
- GET `/api/access/active-roles` - List active roles

### Configuration

#### Default Configuration
- Server Port: 8081
- Database: MySQL 8.x on localhost:3306
- Database Name: ims (auto-created)
- Default Username: root
- Default Password: (empty)
- File Upload Limit: 20MB
- Encryption: AES-256

#### Password Generation
- Formula: firstname (lowercase) + lastname (lowercase) + last 4 digits of mobile
- Example: John Doe (9841234567) → johndoe4567

#### Permissions
- 32 permission combinations auto-seeded
- Flags: request, approve, purchase, users, staff
- Default employee: ID 1, Permissions ID 2

### Known Issues
- Passwords stored in plaintext (requires BCrypt hashing for production)
- No token-based authentication (consider JWT implementation)
- WhatsApp integration requires manual configuration

### Deprecated
- None

### Removed
- None

### Fixed
- None

### Security
- AES-256 encryption implemented for sensitive data
- Role-based access control system
- Permission management with granular control
- ⚠️ **Production Warning:** Implement password hashing before production deployment

---

## Version History

- **[0.0.1-SNAPSHOT]** - 2026-01-29 - Initial development version with comprehensive documentation

---

## Notes

### Versioning Scheme

We use [Semantic Versioning](https://semver.org/):
- **MAJOR** version for incompatible API changes
- **MINOR** version for backwards-compatible functionality additions
- **PATCH** version for backwards-compatible bug fixes

### Categories

Changes are grouped into these categories:
- **Added** - New features
- **Changed** - Changes to existing functionality
- **Deprecated** - Soon-to-be removed features
- **Removed** - Removed features
- **Fixed** - Bug fixes
- **Security** - Security improvements or fixes

---

## Future Roadmap

### Planned Features (v0.1.0)
- [ ] Password hashing with BCrypt
- [ ] JWT-based authentication
- [ ] Email notification system
- [ ] Advanced reporting with charts
- [ ] Export reports to PDF/Excel
- [ ] Barcode/QR code generation
- [ ] Mobile responsive design improvements
- [ ] API rate limiting
- [ ] Audit trail for all operations

### Planned Features (v0.2.0)
- [ ] Multi-tenant support
- [ ] Cloud storage integration
- [ ] Advanced analytics dashboard
- [ ] Automated reorder suggestions
- [ ] Integration with accounting systems
- [ ] Mobile application (Android/iOS)
- [ ] Advanced search and filtering
- [ ] Batch operations for bulk updates

### Planned Features (v1.0.0)
- [ ] Complete test coverage
- [ ] Performance optimization
- [ ] Internationalization (i18n)
- [ ] Offline mode support
- [ ] Real-time notifications
- [ ] API versioning
- [ ] Comprehensive admin dashboard
- [ ] Advanced security features (2FA, SSO)

---

**Maintained by:** IMS Development Team  
**Last Updated:** January 29, 2026
