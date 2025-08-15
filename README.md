# 🏦 ATM System – Java + MySQL

## 📌 Description

A Java-based ATM System with MySQL database integration.
Features include:

* User authentication (Account Number + PIN)
* Balance inquiry
* Deposit & withdrawal
* Transfer between accounts
* Transaction history

## 🛠 Technologies Used

* Java 21
* MySQL
* JDBC

## 📂 Project Structure

```
ATMSystem/
│── src/             # All Java source files
│── lib/             # MySQL Connector JAR
│── database.sql     # SQL script to create DB tables
│── README.md        # Full instructions
│── .gitignore       # Ignore compiled/temp files
```

## 📝 .gitignore

```
bin/
*.class
*.log
*.db
.DS_Store
Thumbs.db
```

## 📝 database.sql

```sql
CREATE DATABASE atm_system;
USE atm_system;

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    account_number VARCHAR(20) NOT NULL UNIQUE,
    pin VARCHAR(10) NOT NULL,
    name VARCHAR(100) NOT NULL,
    balance DECIMAL(15,2) DEFAULT 0
);

CREATE TABLE transactions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    account_number VARCHAR(20) NOT NULL,
    type ENUM('deposit', 'withdrawal', 'transfer') NOT NULL,
    amount DECIMAL(15,2) NOT NULL,
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (account_number) REFERENCES users(account_number)
);
```

## 🛠 How to Run

1. Install Java 21 and MySQL.
2. Create the database:

```sql
CREATE DATABASE atm_system;
USE atm_system;
-- Run the SQL from `database.sql`
```

3. Add MySQL Connector JAR to the `lib` folder.
4. Compile:

```bash
javac -d bin --class-path "lib/*" src/*.java
```

5. Run:

```bash
java --class-path "bin;lib/*" MainClassName
```

## 👤 Author

**Ganesan P**

---

## 📌 GitHub Repo Description

**Java-based ATM System with MySQL integration. Features login, balance inquiry, deposit, withdrawal, transfer, and transaction history.**

## 📌 Suggested GitHub Tags

```
java
mysql
jdbc
atm-system
banking-application
java-project
desktop-application
database
banking
```
