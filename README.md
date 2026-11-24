# 🏛️ GRIET BANK OF INDIA — Core Banking Management System

[![Java](https://img.shields.io/badge/Java-8%20%7C%2011%20%7C%2017%20%7C%2021%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Swing GUI](https://img.shields.io/badge/GUI-Java%20Swing-5382a1?style=for-the-badge&logo=java&logoColor=white)](https://docs.oracle.com/javase/tutorial/uiswing/)
[![Live Web Demo](https://img.shields.io/badge/Live%20Demo-Online%20Simulator-10b981?style=for-the-badge&logo=google-chrome&logoColor=white)](https://manishbollikonda-318.github.io/GRIET-BANK-INDIA/)
[![Download Executable](https://img.shields.io/badge/Download-Standalone%20JAR%20v1.0.0-2563eb?style=for-the-badge&logo=java&logoColor=white)](https://github.com/manishbollikonda-318/GRIET-BANK-INDIA/releases/tag/v1.0.0)
[![Academic Report](https://img.shields.io/badge/PDF%20Report-Download%206--Page%20Dossier-ef4444?style=for-the-badge&logo=adobeacrobatreader&logoColor=white)](https://github.com/manishbollikonda-318/GRIET-BANK-INDIA/releases/download/v1.0.0/GRIET_Bank_India_Project_Report.pdf)
[![License](https://img.shields.io/badge/License-MIT-gray?style=for-the-badge)](LICENSE)

A robust, modular, and real-time desktop Banking Management System developed in **Pure Java** utilizing the **Java Swing** framework and core **Object-Oriented Programming (OOP)** principles for **Gokaraju Rangaraju Institute of Engineering and Technology (GRIET)**.

---

## 👨‍💻 Candidate & Institutional Details
- **Developer / Candidate:** Manish Bollikonda
- **Institution:** Gokaraju Rangaraju Institute of Engineering and Technology (GRIET), Hyderabad
- **Department:** Computer Science and Engineering / Information Technology
- **Repository:** [https://github.com/manishbollikonda-318/GRIET-BANK-INDIA](https://github.com/manishbollikonda-318/GRIET-BANK-INDIA)
- **Live Demo:** [https://manishbollikonda-318.github.io/GRIET-BANK-INDIA/](https://manishbollikonda-318.github.io/GRIET-BANK-INDIA/)

---

## 🌐 Quick Access Links

| Asset | Link | Description |
| :--- | :--- | :--- |
| 🌐 **Live Web Demo** | [Launch Interactive Simulator](https://manishbollikonda-318.github.io/GRIET-BANK-INDIA/) | Test deposits, withdrawals & search directly in browser |
| ⬇️ **Download Executable** | [GRIET_Bank_India.jar (v1.0.0)](https://github.com/manishbollikonda-318/GRIET-BANK-INDIA/releases/tag/v1.0.0) | Standalone executable JAR (double-click to run) |
| 📄 **Project Report PDF** | [Download 6-Page Report](https://github.com/manishbollikonda-318/GRIET-BANK-INDIA/releases/download/v1.0.0/GRIET_Bank_India_Project_Report.pdf) | Formal academic project documentation & SRS |
| 📊 **Presentation Prompt** | [View PPT Master Prompt](PPT_GENERATION_PROMPT.md) | Ready-to-use prompt for generating 12 presentation slides |
| 🎓 **Viva Voce Defense** | [View Viva Q&A Guide](VIVA_GUIDE.md) | 20+ examination questions with model answers |

---

## 📌 Project Overview

**GRIET Bank of India** is designed to replace manual, error-prone paper ledgers with an automated, computerized banking ledger. The system provides real-time transaction processing, multi-tier account governance, case-insensitive keyword filtering, and automated binary object persistence.

The software architecture strictly adheres to **Object-Oriented Programming (OOP)** design principles—**Encapsulation, Inheritance, Runtime Polymorphism, and Abstraction**—alongside a custom checked **Exception Hierarchy** and the **Java Collections Framework**.

---

## ✨ Key Features & Capabilities

- **🔐 Administrative Gatekeeper:** Secure staff login (`admin` / `admin`) with password masking and keyboard accelerator (`Enter` key submission).
- **🏦 Multi-Tier Account Governance:**
  - **Savings Account:** ₹2,000.00 minimum opening balance, per-transaction withdrawal caps, and 5.0% annual interest calculation (`balance * 0.05`).
  - **Current Account:** ₹5,000.00 minimum opening balance with mandatory commercial Trade License verification.
  - **Student Account:** Subsidized ₹100.00 minimum balance with institutional affiliation tracking ("GRIET Hyderabad").
- **⚡ Dynamic Event-Driven UI:** A unified modal dialog (`AccountDialog.java`) dynamically swaps form labels and validation rules in real time based on the selected account type.
- **💵 Financial Transaction Engine:** Synchronized, atomic deposit and withdrawal operations with sequential multi-step validation.
- **📋 Live Directory Search:** Interactive `JTable` integrated with `TableRowSorter` for real-time regex filtering across customer names and account IDs.
- **💾 Fail-Safe Persistence:** Automatic binary object serialization (`data.bin`) with cold-start demo data seeding and exit-prompt interception.
- **🛡️ Custom Checked Exceptions:** Dedicated domain exception hierarchy (`AccountNotFoundException`, `InsufficientBalanceException`, `InvalidAmountException`, `MaxWithdrawLimitException`).

---

## 📊 Account Specification Matrix

| Account Type | Minimum Opening Balance | Per-Transaction Limit | Distinctive Feature / Invariant |
| :--- | :--- | :--- | :--- |
| **Savings Account** | ₹2,000.00 | Configurable (₹5,000 default) | 5.0% Annual Interest Accrual |
| **Current Account** | ₹5,000.00 | Standard Banking Rule | Mandatory Trade License Registration |
| **Student Account** | ₹100.00 | ₹20,000.00 per transaction | Educational Institution Verification |

---

## 🏛️ The Four Pillars of OOP in this Project

```
               ┌──────────────────────────────┐
               │    BankAccount (Abstract)    │
               │ - accountNumber: String      │
               │ - balance: double            │
               │ - minimumBalance: double     │
               └──────────────┬───────────────┘
                              │
               ┌──────────────┴───────────────┐
               │                              │
      ┌────────▼─────────┐          ┌─────────▼────────┐
      │  SavingsAccount  │          │  CurrentAccount  │
      │  + maxWithdraw   │          │  + tradeLicense  │
      │  + 5% Interest   │          └──────────────────┘
      └────────┬─────────┘
               │ (Multilevel)
      ┌────────▼─────────┐
      │  StudentAccount  │
      │  + institution   │
      │  + ₹100 min bal  │
      └──────────────────┘
```

1. **Encapsulation:** State fields (`balance`, `accountNumber`, `minimumBalance`) are `protected` or `private`. Mutations are restricted to `synchronized` business methods (`deposit()`, `withdraw()`). Direct external modification is impossible.
2. **Abstraction:** `BankAccount` is an `abstract class` specifying contracts via `getAccountType()` and `getSpecificDetail()`.
3. **Inheritance:** Hierarchical (`SavingsAccount` and `CurrentAccount` extend `BankAccount`) and Multilevel (`StudentAccount` extends `SavingsAccount`).
4. **Polymorphism:** 
   - *Method Overriding:* `SavingsAccount` overrides `withdraw(double amount)` to validate withdrawal caps before invoking `super.withdraw(amount)`.
   - *Dynamic Method Dispatch:* `List<BankAccount>` executes subclass-specific methods at runtime without downcasting.

---

## 📂 Source Code Structure

```
BankingSystem/
├── build.sh                         # Automated build script (macOS/Linux)
├── run.sh                           # 1-Click execution script (macOS/Linux)
├── run.bat                          # 1-Click execution script (Windows)
├── GRIET_Bank_India_Project_Report.pdf # 6-Page Formal Academic Report
├── PROJECT_REPORT.md                # Markdown Academic Report
├── PPT_GENERATION_PROMPT.md         # Master Prompt to generate presentation slides
├── VIVA_GUIDE.md                    # 20+ Viva Voce questions & model answers
├── README.md                        # Documentation & user manual
└── src/
    ├── Application.java             # Entry-point launch proxy
    └── bank/
        ├── Application.java         # Bootstrap & System Look & Feel setup
        ├── model/                   # Domain entities & business controllers
        │   ├── BankAccount.java     # Abstract base account entity
        │   ├── SavingsAccount.java  # Savings account subclass
        │   ├── CurrentAccount.java  # Current account subclass
        │   ├── StudentAccount.java  # Student account multilevel subclass
        │   └── Bank.java            # Ledger controller using List<BankAccount>
        ├── exception/               # Checked business exception hierarchy
        │   ├── BankException.java   # Root domain exception
        │   ├── AccountNotFoundException.java
        │   ├── InsufficientBalanceException.java
        │   ├── InvalidAmountException.java
        │   └── MaxWithdrawLimitException.java
        ├── storage/                 # Persistence layer
        │   └── FileStorage.java     # Binary object serialization (data.bin)
        └── gui/                     # Event-driven Java Swing UI
            ├── LoginFrame.java      # Administrator login portal
            ├── MainDashboard.java   # Central command dashboard
            ├── AccountDialog.java   # Dynamic account creation modal
            ├── TransactionDialog.java # Unified deposit/withdrawal modal
            └── AccountListFrame.java  # Searchable & sortable JTable directory
```

---

## 🚀 How to Run the Application

### Option A: 1-Click Launch (macOS / Linux)
```bash
git clone https://github.com/manishbollikonda-318/GRIET-BANK-INDIA.git
cd GRIET-BANK-INDIA
./run.sh
```

### Option B: 1-Click Launch (Windows)
Double-click `run.bat` or run:
```cmd
run.bat
```

### Option C: Executable JAR File
Download [GRIET_Bank_India.jar](https://github.com/manishbollikonda-318/GRIET-BANK-INDIA/releases/tag/v1.0.0) and double-click it, or execute:
```bash
java -jar GRIET_Bank_India.jar
```

### Default Credentials
- **Username:** `admin`
- **Password:** `admin`

---

## 🧪 Verification & Test Results

| Test ID | Scenario | Input | Expected Outcome | Status |
| :--- | :--- | :--- | :--- | :--- |
| **TC-01** | Admin Authentication | `admin` / `admin` | Dashboard launched | **PASS** |
| **TC-02** | Invalid Authentication | `admin` / `wrong` | Access denied alert | **PASS** |
| **TC-03** | Create Savings Account | Name: "John", Bal: ₹3000, Limit: ₹1000 | 5-digit ID created | **PASS** |
| **TC-04** | Min Balance Invariant | Savings Bal: ₹500 (&lt; ₹2000) | Rejected with alert | **PASS** |
| **TC-05** | Positive Deposit | Amount: ₹500.00 | Balance incremented | **PASS** |
| **TC-06** | Negative Deposit Rejection | Amount: -₹100.00 | `InvalidAmountException` | **PASS** |
| **TC-07** | Valid Withdrawal | Amount: ₹1,000.00 | Balance debited | **PASS** |
| **TC-08** | Withdrawal Cap Check | Amount: ₹8,000 (&gt; ₹5000 limit) | `MaxWithdrawLimitException` | **PASS** |
| **TC-09** | Minimum Balance Breach | Amount: ₹40,000 (leaves &lt; min) | `InsufficientBalanceException` | **PASS** |
| **TC-10** | Non-Existent Account | ID: `99999` | `AccountNotFoundException` | **PASS** |
| **TC-11** | Real-Time Table Filter | Query: `"Alex"` | Table isolates row | **PASS** |
| **TC-12** | Cold Boot Recovery | Application Restart | State reloaded from `data.bin` | **PASS** |

---

## 📄 License
This project is licensed under the **MIT License**.
