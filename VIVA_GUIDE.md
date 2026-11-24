# Java Project Viva Voce & Code Explanation Guide

# GRIET BANK OF INDIA
### Code Walkthrough, OOP Mapping, and Comprehensive Viva Q&A
**Institution:** Gokaraju Rangaraju Institute of Engineering and Technology (GRIET)

This guide is designed to prepare you to **confidently explain every single line of code**, architectural decision, and Object-Oriented principle in your Java assignment presentation and Viva Voce examination.

---

## 🧭 1. High-Level Architecture Overview

Your codebase is divided into **4 distinct packages** under `bank.*`:

```
bank/
├── model/       -> Encapsulates business logic, data structures, and banking constraints
├── exception/   -> Defines a clean, custom checked exception hierarchy
├── storage/     -> Manages file input/output and object persistence (Serialization)
└── gui/         -> Renders modern, responsive Swing user interfaces and handles user actions
```

### Why this is superior to typical student code:
- **Separation of Concerns:** Business logic (`model`) is decoupled from presentation (`gui`). If you ever wanted to replace Swing with JavaFX or a Web API, you wouldn't need to change `Bank` or `BankAccount`.
- **Maintainable & Concise:** Instead of creating 4 separate frames for different account types, a single `AccountDialog` dynamically updates its UI based on the user's dropdown selection.
- **Fail-Safe Persistence:** Uses `try-with-resources` to guarantee that file streams are automatically closed, preventing memory leaks or file locks.

---

## 🏛️ 2. The Four Pillars of OOP in this Project

When an examiner asks, *"Where have you implemented Object-Oriented Programming?"*, provide these direct answers:

### 1. Encapsulation
- **What it is:** Bundling data and methods that operate on that data into a single unit, while restricting direct access to object internals.
- **Where in code:**
  - `BankAccount.java`: Fields like `accountNumber`, `accountHolderName`, `balance`, and `minimumBalance` are `protected` or `private`.
  - Mutators (`deposit()`, `withdraw()`) enforce validation rules (e.g., amount must be positive, balance cannot drop below minimum). External code cannot bypass validation by doing `account.balance = -500;`.
  - Access is provided safely via public getters (`getAccountNumber()`, `getBalance()`).

### 2. Abstraction
- **What it is:** Hiding internal implementation complexity and exposing only necessary interfaces.
- **Where in code:**
  - `BankAccount.java` is declared `public abstract class BankAccount`. You cannot instantiate a generic bank account directly (`new BankAccount(...)` is prohibited by the compiler).
  - Abstract methods `public abstract String getAccountType();` and `public abstract String getSpecificDetail();` force each specific account subclass to define its own unique traits without cluttering the parent.

### 3. Inheritance
- **What it is:** Mechanism where a child class acquires properties and behaviors of a parent class.
- **Where in code:**
  - **Single Inheritance:** `SavingsAccount extends BankAccount` and `CurrentAccount extends BankAccount`. They inherit `deposit()`, `getBalance()`, and fields from `BankAccount`.
  - **Multilevel Inheritance:** `StudentAccount extends SavingsAccount`, which in turn extends `BankAccount`. `StudentAccount` inherits all savings account rules (such as withdrawal caps) while defining a much lower minimum balance (₹100.00).

### 4. Polymorphism
- **What it is:** Ability of an object to take many forms.
- **Where in code:**
  - **Method Overriding (Runtime Polymorphism):** `SavingsAccount` overrides `withdraw(double amount)`. Before deducting funds, it validates whether `amount > maxWithdrawLimit`. If valid, it invokes `super.withdraw(amount)`.
  - **Dynamic Method Dispatch:** In `Bank.java` and `AccountListFrame.java`, accounts are stored in a `List<BankAccount>`. When iterating through the list and invoking `acc.toString()` or `acc.getAccountType()`, the JVM dynamically calls the child's implementation at runtime.
  - **Method Overloading (Compile-Time Polymorphism):** In `SavingsAccount`, two overloaded constructors exist: one with default minimum balance (`₹2,000`), and one accepting custom minimum balance.

---

## 🔍 3. Detailed Class-by-Class Explanation

### `bank.model.BankAccount` (Abstract Base Class)
- **Role:** Central entity representing any bank account.
- **Key Methods:**
  - `deposit(double amount)`: Checks `amount <= 0`. If true, throws `InvalidAmountException`. Otherwise adds `amount` to `balance`. Marked `synchronized` to ensure thread safety during concurrent operations.
  - `withdraw(double amount)`: Validates that `(balance - amount) >= minimumBalance`. If not, throws `InsufficientBalanceException`.
  - Account Number Generation: In constructor, generates a unique 5-digit number using `10000 + (int)(Math.random() * 90000)`.

### `bank.model.SavingsAccount`
- **Role:** Account for individual savers with withdrawal limits and interest.
- **Key Fields:** `maxWithdrawLimit`, `INTEREST_RATE = 0.05` (5%).
- **Key Method:** `calculateInterest()` returns `balance * 0.05`. Overridden `withdraw()` prevents withdrawals exceeding `maxWithdrawLimit`.

### `bank.model.CurrentAccount`
- **Role:** Commercial checking account for businesses.
- **Key Fields:** `tradeLicenseNumber`, default minimum balance `₹5,000.00`.

### `bank.model.StudentAccount`
- **Role:** Subsidized account for university students.
- **Key Fields:** `institutionName`, minimum balance `₹100.00`, maximum withdrawal `₹20,000.00`.

### `bank.model.Bank` (Business Logic Controller)
- **Role:** Holds the collection of active accounts and coordinates transactions.
- **Data Structure:** `private final List<BankAccount> accounts = new ArrayList<>();`.
  - *Why not an array like `BankAccount[100]`?* Fixed arrays waste memory, impose arbitrary size caps (100), and require messy manual index tracking. `ArrayList` dynamically resizes, implements `Iterable`, and integrates with Java Collections.
- **Key Methods:**
  - `findAccount(String accNum)`: Searches the list and returns matching `BankAccount`. If not found, throws `AccountNotFoundException`.
  - `deposit()` and `withdraw()`: Finds the account and invokes its respective polymorphic methods.
  - `getAccounts()`: Returns `Collections.unmodifiableList(accounts)` to prevent external code from tampering with the list directly.

### `bank.storage.FileStorage` (Persistence Engine)
- **Role:** Reads and writes the `Bank` object to disk using Java Serialization.
- **Key Methods:**
  - `load(String filePath)`: Opens an `ObjectInputStream(new FileInputStream(file))`. Reads the root `Bank` object. If the file does not exist, it seeds 3 sample accounts so the application is immediately testable upon first launch!
  - `save(Bank bank, String filePath)`: Serializes `Bank` and all referenced `BankAccount` objects into binary file `data.bin`.

### `bank.gui.LoginFrame`
- **Role:** Login window for staff.
- **Credentials:** `admin` / `admin`.
- **Key feature:** Pressing `Enter` in the password field immediately submits the credentials via `txtPassword.addActionListener(this::handleLogin)`.

### `bank.gui.MainDashboard`
- **Role:** Central navigation hub for all operations.
- **Key feature:** Uses a `WindowListener` to catch the window close event (`[X]` button) and prompt: *"Would you like to save changes and exit?"* to prevent accidental data loss.

### `bank.gui.AccountDialog`
- **Role:** Unified modal dialog to create Savings, Current, or Student accounts.
- **Key feature:** Listening to `cmbType` dropdown events dynamically swaps field labels (e.g. changing from "Trade License Number" to "Institution Name") in real time.

### `bank.gui.TransactionDialog`
- **Role:** Unified dialog handling both Deposits and Withdrawals depending on `Mode` enum (`DEPOSIT` or `WITHDRAW`).
- **Key feature:** Specific catch blocks for `AccountNotFoundException`, `InsufficientBalanceException`, `MaxWithdrawLimitException`, and `InvalidAmountException`, giving specific user-friendly dialogs for each error.

### `bank.gui.AccountListFrame`
- **Role:** Displays all accounts in an interactive `JTable`.
- **Key feature:** Implements `TableRowSorter` tied to `DocumentListener` on the search field. As the user types letters, the table rows are instantly filtered without reloading data from disk!

---

## ❓ 4. Top 20 Viva Voce Questions & Model Answers

### Q1: What is the main objective of this project?
**Answer:** The objective is to automate manual banking operations—including account opening, fund deposits, withdrawals, and ledger lookups—using an interactive, real-time Java Swing application backed by binary file persistence and Object-Oriented design.

### Q2: Why did you declare `BankAccount` as an abstract class instead of an interface?
**Answer:** An abstract class is chosen because all bank accounts share common state (`accountNumber`, `accountHolderName`, `balance`, `minimumBalance`) and concrete shared behavior (`deposit()`, `getBalance()`). An interface cannot hold mutable instance state or provide a shared constructor.

### Q3: Why is `java.io.Serializable` implemented in your model classes?
**Answer:** `Serializable` is a marker interface (an interface with no methods) that signals to the Java Virtual Machine that the class instances can be converted into a byte stream and written to disk via `ObjectOutputStream`, and restored later using `ObjectInputStream`.

### Q4: What does the `serialVersionUID` field do?
**Answer:** It is a unique version identifier for a Serializable class. When deserializing an object, the JVM compares the stream's `serialVersionUID` with the current class's `serialVersionUID` to ensure binary compatibility. If they don't match, an `InvalidClassException` is thrown.

### Q5: Why are the transaction methods in `BankAccount` marked `synchronized`?
**Answer:** `synchronized` ensures that if multiple threads attempt to access or modify the same account's balance simultaneously (e.g., two concurrent deposits or a simultaneous withdrawal), only one thread executes at a time. This prevents race conditions and data corruption.

### Q6: What is the difference between checked and unchecked exceptions, and which did you use?
**Answer:** Checked exceptions inherit from `java.lang.Exception` and are checked at compile time; methods must either catch them or declare them using `throws`. Unchecked exceptions inherit from `RuntimeException`. We designed a custom checked exception hierarchy (`BankException`) to ensure that business constraints (like account not found or insufficient balance) are explicitly handled by the calling code.

### Q7: Explain the inheritance hierarchy in your accounts.
**Answer:** `BankAccount` is the abstract root. `SavingsAccount` and `CurrentAccount` inherit from `BankAccount` (Hierarchical Inheritance). `StudentAccount` inherits from `SavingsAccount` (Multilevel Inheritance), reusing its withdrawal limit mechanics while providing a subsidized `₹100` minimum balance.

### Q8: How does Method Overriding work in your application?
**Answer:** In `SavingsAccount`, the `withdraw(double amount)` method overrides the `withdraw` method defined in `BankAccount`. It first checks whether `amount > maxWithdrawLimit`. If it exceeds the limit, it throws `MaxWithdrawLimitException`. Otherwise, it delegates to `super.withdraw(amount)` to execute the base balance deduction.

### Q9: Why did you use `ArrayList<BankAccount>` instead of a fixed array like `BankAccount[100]`?
**Answer:** Fixed arrays have fixed capacities, leading to potential array index out-of-bounds exceptions or wasted memory when empty. `ArrayList` grows dynamically as needed, implements the Collections API, supports generics for type safety, and allows safe unmodifiable wrapping via `Collections.unmodifiableList()`.

### Q10: How does the application prevent an account balance from dropping below the minimum required balance?
**Answer:** In `BankAccount.withdraw()`, before modifying `balance`, it evaluates the condition:
`if ((this.balance - amount) < this.minimumBalance)`. If true, an `InsufficientBalanceException` is thrown and the balance remains untouched.

### Q11: What is the Event Dispatch Thread (EDT) in Java Swing?
**Answer:** In Swing, all GUI components and event handlers run on a single background thread called the Event Dispatch Thread (EDT). We launch our application inside `SwingUtilities.invokeLater(() -> { ... })` to ensure the GUI is constructed and updated safely on the EDT, preventing concurrency conflicts.

### Q12: How does the dynamic account creation dialog work?
**Answer:** In `AccountDialog`, when the operator changes the account type in the `JComboBox`, an `ActionListener` triggers `updateExtraField()`. This dynamically updates the label and hint text—displaying "Max Withdraw Limit" for Savings, "Trade License Number" for Current, and "Institution Name" for Student accounts.

### Q13: How does the search filter in the Account Directory table work?
**Answer:** A `TableRowSorter<DefaultTableModel>` is attached to the `JTable`. We attach a `DocumentListener` to the search text field. Whenever a character is typed or deleted, `sorter.setRowFilter(RowFilter.regexFilter("(?i)" + query))` is called. The `(?i)` flag makes the search case-insensitive, filtering matching rows in real time without querying disk storage.

### Q14: How does data persistence work when the user closes the app?
**Answer:** The `MainDashboard` registers a `WindowAdapter` that listens for `windowClosing`. It intercepts the close request and shows a confirmation dialog. If confirmed, `FileStorage.save(bank, "data.bin")` writes the root `Bank` object graph to disk, preserving all accounts and transaction updates.

### Q15: What design pattern is used in `FileStorage`?
**Answer:** It functions as a **Utility / Data Access Object (DAO)** pattern with static factory methods (`load` and `save`), abstracting the complexity of file streams and error handling away from the GUI and domain classes.

### Q16: How do you handle invalid user input, such as letters in an amount field?
**Answer:** When parsing user input via `Double.parseDouble(text)`, the code catches `NumberFormatException`. If the user types invalid characters, a descriptive `JOptionPane.showMessageDialog` alerts the operator without crashing the application.

### Q17: What does `super(accountHolderName, balance, DEFAULT_MIN_BALANCE)` do?
**Answer:** It invokes the constructor of the immediate superclass (`BankAccount`). It ensures that base class fields are properly initialized with validated initial values before child class initialization begins.

### Q18: What is the purpose of `Collections.unmodifiableList(accounts)`?
**Answer:** It implements the Principle of Least Privilege by returning a read-only wrapper around the accounts list. External GUI classes can iterate and display accounts, but cannot directly add or delete items without calling the `Bank` controller methods.

### Q19: How did you configure the UI to look native rather than the default Swing gray?
**Answer:** In `Application.java`, we invoke:
`UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());`
This instructs Swing to adopt the native visual styles, fonts, and controls of the host operating system (macOS Aqua, Windows Vista/10/11 native, or Linux GTK).

### Q20: If you had more time, how would you enhance this application?
**Answer:**
1. Migrate binary file storage to a relational SQL database via JDBC (e.g. MySQL/PostgreSQL) for multi-user networking.
2. Add role-based authentication (separate accounts for Tellers vs. Branch Managers).
3. Generate printable PDF transaction receipts and periodic account statements using iText.
4. Implement an inter-account fund transfer module with two-phase commit rollback support.
