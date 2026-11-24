package bank.model;

/**
 * Educational account with reduced minimum balance requirements.
 * Demonstrates Multilevel Inheritance (BankAccount -> SavingsAccount -> StudentAccount).
 */
public class StudentAccount extends SavingsAccount {
    private static final long serialVersionUID = 1L;
    public static final double STUDENT_MIN_BALANCE = 100.0;
    public static final double STUDENT_DEFAULT_MAX_WITHDRAW = 20000.0;

    private String institutionName;

    public StudentAccount(String accountHolderName, double balance, String institutionName) {
        super(accountHolderName, balance, STUDENT_MIN_BALANCE, STUDENT_DEFAULT_MAX_WITHDRAW);
        this.institutionName = institutionName;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    @Override
    public String getAccountType() {
        return "Student Account";
    }

    @Override
    public String getSpecificDetail() {
        return "Institution: " + institutionName + " | Min Bal: ₹" + minimumBalance;
    }
}
