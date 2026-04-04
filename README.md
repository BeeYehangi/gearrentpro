# GearRent Pro - Multi-Branch Equipment Rental System

A JavaFX desktop application for managing equipment rentals across multiple branches.

## Technologies Used
- Java 21
- JavaFX 21
- MySQL 8.0
- JDBC
- Maven

## Project Structure
src/main/java/com/gearrentpro/
├── entity/       - Plain Java objects
├── dao/          - Database access (JDBC)
├── service/      - Business logic
├── controller/   - Controllers
├── ui/           - JavaFX screens
└── util/         - Utilities

## How to Configure the Database
1. Install MySQL 8.0+
2. Open MySQL Workbench
3. Run the SQL script located at `src/main/resources/db/schema.sql`
4. Open `src/main/java/com/gearrentpro/util/DBConnection.java`
5. Update the password field with your MySQL root password:
```java
private static final String PASSWORD = "your_password_here";
```

## How to Run
1. Make sure Java 21 and Maven are installed
2. Clone the repository
3. Configure the database (see above)
4. Run:
```bash
mvn javafx:run
```

## Default Login Credentials
| Role | Username | Password |
|------|----------|----------|
| Admin | admin | admin123 |
| Branch Manager | manager1 | manager123 |
| Branch Manager | manager2 | manager123 |
| Staff | staff1 | staff123 |

## Features
- Role-based access control (Admin, Branch Manager, Staff)
- Multi-branch equipment management
- Customer management with membership levels
- Reservations with overlap validation
- Rental creation with auto price calculation
- Weekend pricing and category factors
- Long rental and membership discounts
- Return processing with late fees and damage charges
- Overdue rentals tracking
- Branch revenue reports
- Equipment utilization reports
- System configuration management