# MySQL Database Example with JDBC

This project demonstrates how to interact with a MySQL database using JDBC (Java Database Connectivity) in Java. The example shows basic database operations including creating a database, creating a table, inserting data, and querying data.

## Project Overview

The main code is in `MySQLExample.java` which performs the following operations:
1. Connects to MySQL server
2. Creates a new database
3. Creates a users table
4. Inserts sample data
5. Queries and displays the data

## Code Structure and Explanation

### Database Configuration
```java
private static final String DB_HOST = "localhost";
private static final int DB_PORT = 3306;
private static final String DB_USER = "root";
private static final String DB_PASSWORD = "";
```
These constants define the database connection parameters.

### Main Operations

1. **Database Creation**
```java
String createDbSql = "CREATE DATABASE IF NOT EXISTS " + dbName;
stmt.executeUpdate(createDbSql);
```
Creates a new database if it doesn't exist.

2. **Table Creation**
```java
String createTableSql = """
    CREATE TABLE IF NOT EXISTS users (
        id INT AUTO_INCREMENT PRIMARY KEY,
        username VARCHAR(50) NOT NULL UNIQUE,
        email VARCHAR(100),
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    ) ENGINE=InnoDB;
    """;
```
Creates a users table with columns for id, username, email, and creation timestamp.

3. **Data Insertion**
```java
String insertSql = "INSERT INTO users(username, email) VALUES (?, ?)";
// Using PreparedStatement to prevent SQL injection
pstmt.setString(1, "rakasya");
pstmt.setString(2, "rakasya@example.com");
```
Demonstrates safe data insertion using PreparedStatement.

4. **Data Query**
```java
String selectSql = "SELECT id, username, email, created_at FROM users ORDER BY id";
```
Queries and displays all users in the database.

## Features

- **Transaction Management**: Uses explicit transaction control with `setAutoCommit(false)`
- **SQL Injection Prevention**: Uses PreparedStatement for safe data insertion
- **Error Handling**: Includes comprehensive try-catch blocks with rollback capability
- **Resource Management**: Uses try-with-resources for proper connection handling

## Project Structure

- `src/`: Source code directory
  - `MySQLExample.java`: Main application file
- `lib/`: Dependencies directory (JDBC driver)
- `bin/`: Compiled class files

## Prerequisites

1. MySQL Server installed and running
2. Java Development Kit (JDK)
3. MySQL JDBC Driver
4. Database access with appropriate permissions

## Setup Instructions

1. Ensure MySQL is running on localhost:3306
2. Configure the database credentials in `MySQLExample.java`:
   - DB_USER
   - DB_PASSWORD
3. Add MySQL JDBC driver to the project classpath
4. Compile and run the application

## Running the Application

The application will:
1. Create a database named 'contohdb' if it doesn't exist
2. Create a users table if it doesn't exist
3. Insert sample user data
4. Display all users in the table
