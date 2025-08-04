# 🍽️ Restaurant Management System (Java + PostgreSQL)

## 📌 Overview
The **Restaurant Management System** is a Java-based console application that manages restaurant operations including:

- **Customer Table Booking** (Online)
- **Waiter Order Placement** (via tablets)
- **Kitchen Order Tracking** (real-time status updates)
- **Manager Billing & Payments**
- **Admin Sales Reports & User Management**

The system uses **PostgreSQL** for persistent storage and follows a **DAO-Service-UI architecture** for clean separation of concerns.

---

## 🚀 Features

### 👤 Authentication & Roles
- Role-based login: **Customer**, **Waiter**, **Kitchen Staff**, **Manager**, **Admin**
- **Customer Registration** allowed for new customers
- **Only Manager** can register **Waiters** and **Kitchen Staff**
- **Only one Manager & one Admin** in the system

---

### 🪑 Customer Features
- Book available tables
- View booking history
- Cancel bookings (if they exist)

---

### 🍽️ Waiter Features
- Place orders for assigned tables
- Select multiple menu items & quantities
- Orders are sent directly to the kitchen

---

### 👨‍🍳 Kitchen Features
- View all **pending orders**
- Mark orders as **Prepared**

---

### 📋 Manager Features
- Generate bills for completed orders
- Process customer payments
- View **orders without bills** and **unpaid bills** before action
- Once payment is made:
  - Bill is marked **Paid**
  - Order is marked **Served**
  - Table is marked **Available** again

---

### 📊 Admin Features
- View **Daily Sales Reports** for a given date
- View **All Reports** (Paid, Unpaid, Cancelled bills)
- Reports include:
  - Total sales
  - Number of orders
  - Status breakdown (Paid / Unpaid / Cancelled)

---

## 🛠️ Technologies Used
- **Java 17+**
- **PostgreSQL 14+**
- **JDBC** for database connectivity
- **DAO-Service Architecture**

---

## 📂 Project Structure
src/  
├── org.restaurant.models/ # Entity classes  
├── org.restaurant.dao.interfaces/ # DAO interfaces  
├── org.restaurant.dao.implementations/ # DAO implementations  
├── org.restaurant.services/ # Business logic services  
├── org.restaurant/Main.java # Console UI entry point

---

## ⚙️ Database Schema

The main entities are:
- **Users** (Customers, Waiters, Kitchen Staff, Manager, Admin)
- **Tables** (Available / Reserved)
- **Bookings**
- **Menu Items**
- **Orders & Order Items**
- **Bills**
- **Payments**

---

## 📋 Requirements

### Software
- Java 17 or later
- PostgreSQL 14 or later
- JDBC driver for PostgreSQL (`postgresql-42.x.x.jar`)

### Database Setup
1. Create a PostgreSQL database:
   ```sql
   CREATE DATABASE restaurant_db;
2. Create the required tables using the schema provided in `schema.sql` .
3. Set up role constraints in the `users` table to allow only the following roles:
   - Admin
   - Manager
   - Waiter
   - Kitchen
   - Customer
   ```sql
   - CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (role IN ('Admin', 'Manager', 'Waiter', 'Kitchen Staff', 'Customer')),
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL
    );
    
    CREATE TABLE tables (
    table_id SERIAL PRIMARY KEY,
    capacity INT NOT NULL CHECK (capacity > 0),
    status VARCHAR(20) NOT NULL CHECK (status IN ('Available', 'Reserved', 'Occupied'))
    );
    
    CREATE TABLE bookings (
    booking_id SERIAL PRIMARY KEY,
    table_id INT NOT NULL REFERENCES tables(table_id) ON DELETE CASCADE,
    customer_id INT NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    booking_datetime TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL CHECK (status IN ('Reserved', 'Cancelled', 'Completed')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );
    
    CREATE TABLE menuitems (
    item_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(50) NOT NULL CHECK (category IN ('Starter', 'Main Course', 'Dessert', 'Drink')),
    price DECIMAL(10, 2) NOT NULL CHECK (price >= 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );
    
    CREATE TABLE orders (
    order_id SERIAL PRIMARY KEY,
    table_id INT NOT NULL REFERENCES tables(table_id) ON DELETE CASCADE,
    waiter_id INT NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    status VARCHAR(20) NOT NULL CHECK (status IN ('Pending', 'Prepared', 'Served')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );
    
    CREATE TABLE orderitems (
    order_item_id SERIAL PRIMARY KEY,
    order_id INT NOT NULL REFERENCES orders(order_id) ON DELETE CASCADE,
    item_id INT NOT NULL REFERENCES menuitems(item_id) ON DELETE CASCADE,
    quantity INT NOT NULL CHECK (quantity > 0)
    );
    
    CREATE TABLE bills (
    bill_id SERIAL PRIMARY KEY,
    order_id INT NOT NULL UNIQUE REFERENCES orders(order_id) ON DELETE CASCADE, -- 1 bill per order
    total_amount DECIMAL(10, 2) NOT NULL CHECK (total_amount >= 0),
    payment_status VARCHAR(20) NOT NULL CHECK (payment_status IN ('Unpaid', 'Paid', 'Cancelled')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );
    
    CREATE TABLE payments (
    payment_id SERIAL PRIMARY KEY,
    bill_id INT NOT NULL UNIQUE REFERENCES bills(bill_id) ON DELETE CASCADE, -- 1 payment per bill
    amount_paid DECIMAL(10, 2) NOT NULL CHECK (amount_paid >= 0),
    payment_method VARCHAR(50) NOT NULL,
    payment_datetime TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    );
```
---

### 🏗️ Setup Instructions
1. Clone the repository:
```bash
    git clone https://github.com/t-r02/RestaurantManagement.git
    cd RestaurantManagement
```

2. Update DBConnection.java with your PostgreSQL credentials:

```java
    private static final String URL = "jdbc:postgresql://localhost:5432/restaurant_db";
    private static final String USER = "your_username";
    private static final String PASSWORD = "your_password";
```
3. Compile the project:
```bash
    javac -cp .:postgresql-42.x.x.jar $(find src -name "*.java") -d out
```
4. Run the application:
```bash
    java -cp out:postgresql-42.x.x.jar org.restaurant.Main
```
---

### 🔑 Default Users

| Role | Username | Password |
| --- | --- | --- |
| Admin | admin | admin123 |
| Manager | manager | man123 |
| Waiter | waiter1 | wait123 |
| Waiter | waiter2 | wait234 |
| Kitchen | kitchen1 | kit123 |
| Kitchen | kitchen2 | kit234 |


### 🧩 Additional Features
- Default Data Initialization (users, tables, and menu items created on first run if missing)
- Bill shows ordered items, prices, and total amount
- Payment automatically frees up the table
- Daily report includes unpaid & cancelled bills

### 📈 Future Improvements
- Web-based UI with Spring Boot
- Integration with POS hardware
- Email/SMS booking confirmations
- Data export in CSV/PDF format

### 📞 Contact
For any issues or feature requests, please open an issue on the GitHub repository (https://github.com/t-r02/RestaurantManagement.git) or contact the project maintainer.