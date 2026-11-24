package bank.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import bank.exception.*;

/**
 * Controller class managing account collections and transaction orchestration.
 * Demonstrates Collections Framework, Generics, and Business Logic Separation.
 */
public class Bank implements Serializable {
    private static final long serialVersionUID = 1L;

    private final List<BankAccount> accounts = new ArrayList<>();

    public void addAccount(BankAccount account) {
        if (account != null) {
            accounts.add(account);
        }
    }

    public BankAccount findAccount(String accountNumber) throws AccountNotFoundException {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            throw new AccountNotFoundException("Account number cannot be empty.");
        }
        for (BankAccount acc : accounts) {
            if (acc.getAccountNumber().equalsIgnoreCase(accountNumber.trim())) {
                return acc;
            }
        }
        throw new AccountNotFoundException("No account registered with number: " + accountNumber.trim());
    }

    public void deposit(String accountNumber, double amount)
            throws AccountNotFoundException, InvalidAmountException {
        BankAccount acc = findAccount(accountNumber);
        acc.deposit(amount);
    }

    public void withdraw(String accountNumber, double amount)
            throws AccountNotFoundException, InsufficientBalanceException, InvalidAmountException, MaxWithdrawLimitException {
        BankAccount acc = findAccount(accountNumber);
        acc.withdraw(amount);
    }

    public List<BankAccount> getAccounts() {
        return Collections.unmodifiableList(accounts);
    }

    public int getAccountCount() {
        return accounts.size();
    }
}
