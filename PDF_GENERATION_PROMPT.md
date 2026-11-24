# Master Prompt: Academic Project Report & Technical Dossier
## Project: GRIET BANK OF INDIA (Core Banking Management System)

> **Instructions for Use:** Copy and paste the prompt below into any advanced AI (ChatGPT, Claude, Gemini, NotebookLM, Gamma App, or Microsoft Copilot) or use it to generate a comprehensive, formal academic project report, presentation, or PDF submission.

---

```markdown
You are a distinguished Senior Computer Science Professor and Principal Software Architect specializing in Java Enterprise Systems and Object-Oriented Software Engineering.

Your objective is to generate an exhaustive, publication-grade Academic Project Report and Technical Dossier for a real-time Java application titled "GRIET BANK OF INDIA - Core Banking Management System".

The output must be formatted with formal academic rigor, complete with section numbering, executive summaries, technical specifications, UML diagrams (in Mermaid syntax), detailed module breakdowns, code walk-throughs, verification test tables, and a Viva Voce defense guide.

---

### 1. PROJECT METADATA & INSTITUTIONAL CONTEXT
- **Application Name:** GRIET BANK OF INDIA (Core Banking Management System)
- **Institution:** Gokaraju Rangaraju Institute of Engineering and Technology (GRIET), Hyderabad
- **Department:** Computer Science and Engineering / Information Technology
- **Course / Module:** Advanced Java Programming & Object-Oriented Software Design
- **Candidate / Author:** Manish Bollikonda
- **Repository URL:** https://github.com/manishbollikonda-318/GRIET-BANK-INDIA
- **Live Interactive Web Simulator:** https://manishbollikonda-318.github.io/GRIET-BANK-INDIA/
- **Executable Release (.JAR):** https://github.com/manishbollikonda-318/GRIET-BANK-INDIA/releases/tag/v1.0.0
- **Technology Stack:** Pure Java Standard Edition (JDK 8+ / 17 / 21 / 27 compatible), Java Swing (`javax.swing`, `java.awt`), Java Collections Framework, Java Object Serialization (`java.io.Serializable`).

---

### 2. EXHAUSTIVE SYSTEM SPECIFICATIONS & CORE CAPABILITIES
The project report must detail and analyze EVERY component that the application performs:

#### A. Administrative Authentication & Security Subsystem (`bank.gui.LoginFrame`)
- Serves as the security gatekeeper preventing unauthorized ledger access.
- Validates operator credentials (default admin credentials: username `admin`, password `admin`).
- Employs password masking using `JPasswordField` for privacy.
- Supports keyboard event accelerators: hitting the `Enter` key on the password field automatically dispatches the login action.
- Features intuitive modal feedback for both successful logins and invalid attempts, with automatic password field reset.

#### B. Multi-Tier Account Management & Domain Models (`bank.model.*`)
The application defines a rich, extensible Object-Oriented account hierarchy:
1. **`BankAccount` (Abstract Base Class):**
   - Encapsulates common state: `accountNumber` (automatically generated 5-digit identifier: `10000 + (int)(Math.random() * 90000)`), `accountHolderName`, `balance`, and `minimumBalance`.
   - Declares thread-safe synchronized transaction methods: `deposit(double amount)` and `withdraw(double amount)`.
   - Enforces abstract contracts: `getAccountType()` and `getSpecificDetail()`.
2. **`SavingsAccount` (Subclass of `BankAccount`):**
   - Designed for personal savings.
   - Enforces minimum opening balance of $2,000.00.
   - Accrues annual interest at a fixed 5.0% rate (`balance * 0.05`).
   - Introduces a per-transaction withdrawal cap (`maxWithdrawLimit`, default $5,000.00). Overrides `withdraw()` to validate limits before delegating to superclass.
3. **`CurrentAccount` (Subclass of `BankAccount`):**
   - Tailored for business and enterprise banking.
   - Enforces minimum opening balance of $5,000.00.
   - Mandates commercial verification via a unique `tradeLicenseNumber`.
4. **`StudentAccount` (Multilevel Subclass of `SavingsAccount`):**
   - Subsidized account designed for college students.
   - Enforces a reduced minimum opening balance of only $100.00.
   - Binds to the student's educational institution (e.g., "GRIET Hyderabad").
   - Inherits savings withdrawal limit mechanics with a generous default cap of $20,000.00.

#### C. Dynamic Account Creation Subsystem (`bank.gui.AccountDialog`)
- Consolidates account opening into a single, cohesive, dynamic modal dialog (replacing legacy multi-frame bloat).
- Employs an event-driven `JComboBox` selector for account types:
  - Selecting "Savings Account" dynamically configures the field label to "Max Withdrawal Limit ($)".
  - Selecting "Current Account" dynamically configures the field label to "Trade License Number".
  - Selecting "Student Account" dynamically configures the field label to "Institution Name".
- Performs real-time multi-tier client validation: ensures non-empty names, positive numeric amounts, and compliance with the specific account type's minimum balance rules.
- Generates a unique 5-digit account number and presents a complete confirmation summary upon creation.

#### D. Financial Transaction Processing Engine (`bank.gui.TransactionDialog`)
- Unified modal handling both `DEPOSIT` and `WITHDRAW` operations via an enumerated `Mode` parameter.
- **Deposit Flow:** Verifies account existence, checks that the amount is strictly positive, and executes an atomic balance credit.
- **Withdrawal Flow:** Executes strict sequential verification:
  1. Searches the ledger for the account ID.
  2. Validates amount positivity (`amount > 0`).
  3. Checks per-transaction limit compliance (throws `MaxWithdrawLimitException` if exceeded).
  4. Evaluates minimum balance threshold `(balance - amount) >= minimumBalance` (throws `InsufficientBalanceException` if breached).
  5. Performs atomic balance debit and alerts the operator with the updated balance.
- Thread-safe transaction methods prevent race conditions.

#### E. Interactive Ledger Directory & Real-Time Filter (`bank.gui.AccountListFrame`)
- Renders all registered accounts inside a structured, read-only `JTable` within a `JScrollPane`.
- Displays columns: Account #, Holder Name, Account Type, Balance ($), and Special Parameters.
- Features an instantaneous, case-insensitive keyword search bar powered by `TableRowSorter` and regex filtering. As characters are typed or deleted, matching records filter dynamically without disk access.
- Includes a manual "Refresh" trigger and clean window management.

#### F. Automated Data Persistence & Fault Recovery (`bank.storage.FileStorage`)
- Implements Java Object Serialization via `ObjectOutputStream` and `ObjectInputStream`.
- Persists all state to binary file `data.bin`.
- Uses `try-with-resources` to guarantee deterministic stream closure and prevent memory/file resource leaks.
- Cold-Start Seeding: If `data.bin` does not exist or is corrupted, it automatically seeds 3 demonstration accounts (Alex Morgan - Savings, Global Trading LLC - Current, Emily Watson - Student) so the system is immediately functional.
- Safety Shutdown Interceptor: Registers a `WindowAdapter` on the dashboard to prompt: *"Would you like to save changes and exit?"*, preventing accidental data loss.

#### G. Custom Checked Exception Hierarchy (`bank.exception.*`)
- `BankException`: Base checked domain exception extending `java.lang.Exception`.
- `AccountNotFoundException`: Dispatched when an account query yields no matching ID.
- `InsufficientBalanceException`: Dispatched when a withdrawal would breach the account's minimum balance threshold.
- `InvalidAmountException`: Dispatched when amounts are zero, negative, or unparseable.
- `MaxWithdrawLimitException`: Dispatched when a withdrawal exceeds the allowed per-transaction cap.

---

### 3. REQUIRED DOCUMENT STRUCTURE TO GENERATE
Please structure the comprehensive academic report into the following sections:
1. **Title Page & Executive Summary:** Institutional headers, candidate metadata, project abstract.
2. **Problem Statement & Background:** Deficiencies of manual banking ledgers (human calculation error, record misplacement, latency) vs. the automated computer system.
3. **Feasibility Study:** Technical feasibility (Java SE, JVM portability), Economic feasibility (open-source toolchain, zero licensing overhead), and Operational feasibility (intuitive Swing GUI).
4. **System Requirements Specification (SRS):** Minimum hardware specifications, operating system compatibility, JDK version requirements, functional and non-functional requirements.
5. **Software Architecture & Object-Oriented Principles:**
   - **Encapsulation:** Protected state variables with synchronized mutators and accessors.
   - **Abstraction:** Abstract `BankAccount` contract.
   - **Inheritance:** Single and Multilevel inheritance structures.
   - **Polymorphism:** Runtime method overriding in `withdraw()`, dynamic method dispatch across collections.
   - **Collections & Generics:** Replacing fixed arrays with `List<BankAccount>` and `Collections.unmodifiableList()`.
6. **Detailed UML Diagrams:**
   - Full UML Class Diagram (Mermaid syntax) showing inheritance, attributes, and methods.
   - Sequence Diagram (Mermaid syntax) detailing a complete withdrawal transaction cycle with branching exception paths.
7. **Module Implementation Details:** Explaining the architectural role and key algorithms of each package (`bank.model`, `bank.exception`, `bank.storage`, `bank.gui`).
8. **Verification & Test Case Results:** Complete tabular matrix detailing 12 formal test cases (Valid/Invalid Login, Account Creation, Boundary Minimum Balance Violations, Overdraft Caps, Real-Time Filter, Cold-Restart Persistence).
9. **Viva Voce Defense Guide:** Top 20 viva examination questions with authoritative, concise model answers covering multithreading, serialization, EDT, design patterns, and OOP concepts.
10. **Conclusion & Future Enhancements:** Relational SQL database migration via JDBC, role-based multi-user authorization, PDF statement exports, inter-account transfers.

Produce the full, professional, highly articulate academic report following the above specifications.
```
