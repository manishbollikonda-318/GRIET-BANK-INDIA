package bank.model;

import java.io.Serializable;
import bank.exception.InsufficientBalanceException;
import bank.exception.InvalidAmountException;
import bank.exception.MaxWithdrawLimitException;

/**
 * Abstract base class representing a general bank account.
 * Demonstrates Abstraction and Encapsulation.
 */
public abstract class BankAccount implements Serializable {
    private static final long serialVersionUID = 1L;

    protected String accountNumber;
    protected String accountHolderName;
    protected double balance;
    protected double minimumBalance;

    public BankAccount(String accountHolderName, double balance, double minimumBalance) {
        this.accountNumber = String.valueOf(10000 + (int) (Math.random() * 90000));
        this.accountHolderName = accountHolderName;
        this.balance = balance;
        this.minimumBalance = minimumBalance;
    }

    /**
     * Deposits positive amount into account.
     */
    public synchronized void deposit(double amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Deposit amount must be strictly greater than zero.");
        }
        this.balance += amount;
    }

    /**
     * Withdraws amount from account if minimum balance requirement is met.
     */
    public synchronized void withdraw(double amount)
            throws InsufficientBalanceException, InvalidAmountException, MaxWithdrawLimitException {
        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be strictly greater than zero.");
        }
        if ((this.balance - amount) < this.minimumBalance) {
            throw new InsufficientBalanceException(
                String.format("Insufficient funds. Minimum required balance is $%.2f (Current Balance: $%.2f)",
                        minimumBalance, balance)
            );
        }
        this.balance -= amount;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public double getBalance() {
        return balance;
    }

    public double getMinimumBalance() {
        return minimumBalance;
    }

    public abstract String getAccountType();

    public abstract String getSpecificDetail();

    @Override
    public String toString() {
        return String.format("[%s] #%s | %s | Balance: $%.2f | %s",
                getAccountType(), accountNumber, accountHolderName, balance, getSpecificDetail());
    }
}
