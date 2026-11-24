package bank.model;

import bank.exception.InsufficientBalanceException;
import bank.exception.InvalidAmountException;
import bank.exception.MaxWithdrawLimitException;

/**
 * Savings Account with interest accrual and transaction withdrawal caps.
 * Demonstrates Inheritance and Polymorphic Method Overriding.
 */
public class SavingsAccount extends BankAccount {
    private static final long serialVersionUID = 1L;
    public static final double DEFAULT_MIN_BALANCE = 2000.0;
    public static final double INTEREST_RATE = 0.05; // 5%

    private double maxWithdrawLimit;

    public SavingsAccount(String accountHolderName, double balance, double maxWithdrawLimit) {
        super(accountHolderName, balance, DEFAULT_MIN_BALANCE);
        this.maxWithdrawLimit = maxWithdrawLimit;
    }

    public SavingsAccount(String accountHolderName, double balance, double minimumBalance, double maxWithdrawLimit) {
        super(accountHolderName, balance, minimumBalance);
        this.maxWithdrawLimit = maxWithdrawLimit;
    }

    @Override
    public synchronized void withdraw(double amount)
            throws InsufficientBalanceException, InvalidAmountException, MaxWithdrawLimitException {
        if (amount > maxWithdrawLimit) {
            throw new MaxWithdrawLimitException(
                String.format("Withdrawal of $%.2f exceeds transaction limit of $%.2f", amount, maxWithdrawLimit)
            );
        }
        super.withdraw(amount);
    }

    public double getMaxWithdrawLimit() {
        return maxWithdrawLimit;
    }

    public double calculateInterest() {
        return balance * INTEREST_RATE;
    }

    @Override
    public String getAccountType() {
        return "Savings Account";
    }

    @Override
    public String getSpecificDetail() {
        return String.format("Withdraw Limit: $%.2f | Int. Rate: 5%%", maxWithdrawLimit);
    }
}
