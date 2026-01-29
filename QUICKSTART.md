# Quick Start Guide - Inventory Management System

> Get up and running with the IMS in under 10 minutes!

## Prerequisites Checklist

Before you begin, make sure you have:

- [ ] Java JDK 17 or higher installed
- [ ] MySQL Server 8.x installed and running
- [ ] Maven 3.6+ installed (or use included wrapper)
- [ ] Git installed (for cloning)

## Step-by-Step Setup

### 1. Clone the Repository (2 minutes)

```bash
git clone https://github.com/samyeak-m/inventory-management-system.git
cd inventory-management-system
```

### 2. Configure Database (1 minute)

**Option A: Use Default Settings**

If you have MySQL running on localhost with root user and no password, skip to step 3.

**Option B: Custom Configuration**

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ims?createDatabaseIfNotExist=true
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
```

### 3. Start MySQL (1 minute)

**Windows:**
```bash
net start mysql
```

**Linux/Mac:**
```bash
sudo systemctl start mysql
# or
sudo service mysql start
```

**Verify MySQL is running:**
```bash
mysql -u root -p -e "SELECT 1"
```

### 4. Build and Run (3-5 minutes)

**Using Maven:**
```bash
# Clean and build
mvn clean install

# Run the application
mvn spring-boot:run
```

**Using Maven Wrapper (no Maven installation needed):**
```bash
# Windows
mvnw.cmd clean install
mvnw.cmd spring-boot:run

# Linux/Mac
./mvnw clean install
./mvnw spring-boot:run
```

**Expected Output:**
```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/

 :: Spring Boot ::                (v3.5.0)

...
...
Local: http://localhost:8081
External: http://192.168.1.100:8081
```

### 5. Access the Application (1 minute)

Open your web browser and navigate to:

```
http://localhost:8081
```

You should see the login page of the Inventory Management System.

---

## First Time Setup Workflow

### Step 1: Setup Organization

1. Navigate to: `http://localhost:8081/organizationsetup`
2. Fill in the organization details:
   - Organization Name: Your Company Name
   - Address: Your Address
   - Mobile: Contact Number
   - Email: Organization Email
   - Inventory Method: Select FIFO (recommended for beginners)
3. Upload logo (optional)
4. Click **Submit**

### Step 2: Create First Branch

1. Navigate to: `http://localhost:8081/branchsetup`
2. Fill in branch details:
   - Branch Name: Main Office
   - Branch Manager: Manager Name
   - Contact details
3. Click **Submit**
4. Note: Branch code will be auto-generated (001)

### Step 3: Create Department

1. Navigate to: `http://localhost:8081/departmentsetup`
2. Enter department name: IT Department
3. Click **Submit**
4. Note: Department code will be auto-generated (001)

### Step 4: Setup Calendar

1. Navigate to: `http://localhost:8081/setupCalendar`
2. Select fiscal year start and end dates
3. Click **Generate Calendar**

### Step 5: Create Categories

1. Navigate to: `http://localhost:8081/categorytype`
2. Create a category type:
   - Category: Electronics
   - Subcategory: Laptops
3. Click **Submit**
4. Create more categories as needed

### Step 6: Register First Admin User

1. Navigate to: `http://localhost:8081/userregistration`
2. Fill in user details:
   - First Name: Admin
   - Last Name: User
   - Email: admin@company.com
   - Mobile: 9841234567
   - Branch: Main Office
   - Department: IT Department
   - Role: Administrator
   - Permissions: Select all
3. Click **Submit**
4. **IMPORTANT:** Note down the auto-generated password
   - Password will be: `adminuser4567` (firstname + lastname + last 4 digits of mobile)

### Step 7: Login

1. Navigate to: `http://localhost:8081/login`
2. Enter credentials:
   - Username/Email: admin@company.com
   - Password: adminuser4567
3. Click **Login**

---

## Testing the API

### Using cURL

**Test 1: Check Organization**
```bash
curl http://localhost:8081/organization/get-organization
```

**Test 2: List Branches**
```bash
curl http://localhost:8081/api/branch/branch-list
```

**Test 3: List Departments**
```bash
curl http://localhost:8081/api/department/department-list
```

**Test 4: Create an Item**
```bash
curl -X POST http://localhost:8081/api/item/submit-item \
  -H "Content-Type: application/json" \
  -d '{
    "itemName": "Test Laptop",
    "categoryCode": "ELE001",
    "branchCode": "001",
    "location": "Warehouse",
    "minimumOrderLevel": 5,
    "reorderLevel": 10,
    "maximumOrderLevel": 50,
    "unit": "pieces"
  }'
```

---

## Common Issues and Quick Fixes

### Issue 1: Port 8081 Already in Use

**Solution:**
Change port in `application.properties`:
```properties
server.port=8082
```

### Issue 2: MySQL Connection Refused

**Causes:**
- MySQL service not running
- Wrong credentials
- Wrong port

**Solution:**
```bash
# Check MySQL status
mysql -u root -p -e "SELECT 1"

# Start MySQL if not running
# Windows: net start mysql
# Linux: sudo systemctl start mysql
```

### Issue 3: Database 'ims' doesn't exist

**Solution:**
The application will create it automatically if you have:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ims?createDatabaseIfNotExist=true
```

### Issue 4: Build Fails

**Solution:**
```bash
# Clean Maven cache
mvn clean

# Force update dependencies
mvn clean install -U

# Skip tests if needed
mvn clean install -DskipTests
```

### Issue 5: Cannot Access Localhost

**Solution:**
Check firewall settings or try:
```
http://127.0.0.1:8081
```

---

## Quick Command Reference

### Maven Commands

```bash
# Clean build
mvn clean

# Compile
mvn compile

# Run tests
mvn test

# Package JAR
mvn package

# Run application
mvn spring-boot:run

# Clean and build
mvn clean install

# Skip tests
mvn clean install -DskipTests
```

### MySQL Commands

```bash
# Login to MySQL
mysql -u root -p

# Create database manually
mysql -u root -p -e "CREATE DATABASE ims;"

# Show databases
mysql -u root -p -e "SHOW DATABASES;"

# Backup database
mysqldump -u root -p ims > ims_backup.sql

# Restore database
mysql -u root -p ims < ims_backup.sql
```

### Application URLs

| Purpose | URL |
|---------|-----|
| Home | http://localhost:8081 |
| Login | http://localhost:8081/login |
| Organization Setup | http://localhost:8081/organizationsetup |
| Branch Setup | http://localhost:8081/branchsetup |
| Department Setup | http://localhost:8081/departmentsetup |
| Category Setup | http://localhost:8081/categorytype |
| Item Management | http://localhost:8081/itemview |
| User Registration | http://localhost:8081/userregistration |
| Calendar Setup | http://localhost:8081/setupCalendar |

---

## Next Steps

After completing the quick start:

1. **Read Full Documentation:** [DOCUMENTATION.md](DOCUMENTATION.md)
2. **Explore API Endpoints:** [API_REFERENCE.md](API_REFERENCE.md)
3. **Create Items:** Add your inventory items
4. **Setup Workflow:** Create requisitions, purchase orders
5. **Generate Reports:** View stock ledger and reports

---

## Need Help?

- **Full Documentation:** See [DOCUMENTATION.md](DOCUMENTATION.md)
- **API Reference:** See [API_REFERENCE.md](API_REFERENCE.md)
- **Troubleshooting:** See [DOCUMENTATION.md#13-troubleshooting](DOCUMENTATION.md#13-troubleshooting)

---

## Default Password Formula

Remember: Passwords are auto-generated as:
```
firstname (lowercase) + lastname (lowercase) + last 4 digits of mobile
```

**Examples:**
- John Doe with mobile 9841234567 → `johndoe4567`
- Admin User with mobile 9851234567 → `adminuser4567`
- Ram Sharma with mobile 9801234567 → `ramsharma4567`

---

## Quick Test Checklist

After setup, verify these work:

- [ ] Application starts without errors
- [ ] Can access http://localhost:8081
- [ ] Organization details saved
- [ ] Branch created with code 001
- [ ] Department created with code 001
- [ ] Calendar generated
- [ ] Category created
- [ ] User registered successfully
- [ ] Can login with generated password
- [ ] Can create an item
- [ ] Can view stock reports

---

**Congratulations! Your Inventory Management System is now running.**

**Total Setup Time:** Approximately 10 minutes

---

**Version:** 1.0  
**Last Updated:** January 29, 2026
