# GRIET BANK INDIA - Core Banking Management System

A robust, modular, and real-time desktop Banking Management System developed in **Java** utilizing the **Java Swing** framework and core **Object-Oriented Programming (OOP)** design principles for **GRIET Bank of India**.

---

## 📌 Project Overview

**GRIET Bank India** is an interactive, real-time desktop application simulating modern institutional banking operations. It is designed to replace manual, error-prone record keeping with an automated, computerized banking ledger.

The project demonstrates production-grade Java architecture, including **Encapsulation, Inheritance, Polymorphism, Abstraction, Custom Exception Handling, Collections Framework, and Binary Object Serialization** for data persistence.

---

## ✨ Key Features

- **🔐 Administrative Authentication:** Secure staff login gatekeeper preventing unauthorized ledger access.
- **🏦 Multi-Tier Account Management:**
  - **Savings Account:** Minimum balance validation ($2,000.00), per-transaction withdrawal limits, and automatic 5% interest accrual calculation.
  - **Current Account:** Minimum balance validation ($5,000.00) tailored for commercial use with Trade License tracking.
  - **Student Account:** Low-threshold minimum balance ($100.00) with academic institution verification.
- **💵 Financial Transaction Engine:** Real-time deposits and withdrawals with comprehensive input validation and constraint checking.
- **📋 Live Account Directory & Search:** Interactive `JTable` with dynamic, case-insensitive keyword filtering (by name or account number) and real-time updates.
- **💾 Automated Data Persistence:** Automatic binary serialization and deserialization via Java Object Streams (`data.bin`), ensuring zero data loss across application restarts.
- **🛡️ Custom Exception Handling:** Tailored business exception hierarchy (`AccountNotFoundException`, `InsufficientBalanceException`, `InvalidAmountException`, `MaxWithdrawLimitException`).

---

## 🛠️ System Architecture & OOP Design

The application follows a clean **Model-View-Controller / Multi-Tier** architecture:

```
src/
├── Application.java                 # Top-level launch proxy
└── bank/
    ├── Application.java             # Main application entry point & Look & Feel configuration
    ├── model/                       # Business Domain Entities
    │   ├── BankAccount.java         # Abstract base class (Abstraction & Encapsulation)
    │   ├── SavingsAccount.java      # Subclass with withdrawal limits & interest (Inheritance & Polymorphism)
    │   ├── CurrentAccount.java      # Subclass with trade license tracking
    │   ├── StudentAccount.java      # Subclass with institution binding (Multilevel Inheritance)
    │   └── Bank.java                # Controller managing accounts via Java Collections
    ├── exception/                   # Domain Exception Hierarchy
    │   ├── BankException.java       # Base checked exception
    │   ├── AccountNotFoundException.java
    │   ├── InsufficientBalanceException.java
    │   ├── InvalidAmountException.java
    │   └── MaxWithdrawLimitException.java
    ├── storage/                     # Persistence Layer
    │   └── FileStorage.java         # Object serialization & deserialization engine
    └── gui/                         # Presentation Layer (Java Swing)
        ├── LoginFrame.java          # Administrative credentials gate
        ├── MainDashboard.java       # Central command menu
        ├── AccountDialog.java       # Unified dynamic account creation modal
        ├── TransactionDialog.java   # Unified deposit and withdrawal modal
        └── AccountListFrame.java    # Sortable and searchable ledger table
```

---

## 🚀 Getting Started

### Prerequisites
- **Java Development Kit (JDK):** Version 8 or higher (Tested with JDK 17, 21, and 27)

### Quick Run (macOS / Linux)
```bash
# Clone and enter the repository
git clone https://github.com/manishbollikonda-318/GRIET-BANK-INDIA.git
cd GRIET-BANK-INDIA

# Build and execute with one command
./run.sh
```

### Quick Run (Windows)
```cmd
run.bat
```

### Manual Compilation & Execution
```bash
# 1. Compile all source files into bin directory
mkdir -p bin
javac -d bin $(find src -name "*.java")

# 2. Run the application
java -cp bin Application
```

---

## 🔑 Default Credentials

- **Username:** `admin`
- **Password:** `admin`

---

## 📊 Account Specification Matrix

| Account Type | Minimum Balance | Default Transaction Limit | Special Requirement |
| :--- | :--- | :--- | :--- |
| **Savings Account** | $2,000.00 | Configurable ($5,000 default) | 5% Interest Accrual |
| **Current Account** | $5,000.00 | Standard Banking Rule | Valid Trade License Number |
| **Student Account** | $100.00 | $20,000.00 | Educational Institution Name |

---

## 🖥️ User Interface Screenshots

| Screen | Description |
| :--- | :--- |
| **Login Portal** | Administrative authentication gateway |
| **Operations Dashboard** | Central operational menu |
| **Account Creation** | Dynamic form updating fields by account type |
| **Account Directory** | Searchable table with real-time query filtering |

*(See `screenshot/` directory for visual reference images)*

---

## 📖 Documentation & Academic Submission

- **[PROJECT_REPORT.md](PROJECT_REPORT.md):** Complete academic project report including feasibility study, UML class diagrams, test execution results, and system specifications.
- **[VIVA_GUIDE.md](VIVA_GUIDE.md):** Comprehensive Viva Voce preparation guide with detailed code explanations, OOP concept mapping, and 20+ examination questions & answers.

---

## 📄 License
This project is released under the **MIT License**.
