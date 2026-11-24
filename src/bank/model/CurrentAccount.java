package bank.model;

/**
 * Commercial checking account requiring business registration/trade license.
 * Demonstrates Inheritance.
 */
public class CurrentAccount extends BankAccount {
    private static final long serialVersionUID = 1L;
    public static final double DEFAULT_MIN_BALANCE = 5000.0;

    private String tradeLicenseNumber;

    public CurrentAccount(String accountHolderName, double balance, String tradeLicenseNumber) {
        super(accountHolderName, balance, DEFAULT_MIN_BALANCE);
        this.tradeLicenseNumber = tradeLicenseNumber;
    }

    public String getTradeLicenseNumber() {
        return tradeLicenseNumber;
    }

    @Override
    public String getAccountType() {
        return "Current Account";
    }

    @Override
    public String getSpecificDetail() {
        return "Trade License: " + tradeLicenseNumber;
    }
}
