package bank.gui;

import bank.exception.*;
import bank.model.Bank;
import bank.model.BankAccount;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Unified dialog handling both Deposit and Withdrawal transactions.
 */
public class TransactionDialog extends JDialog {
    private static final long serialVersionUID = 1L;

    public enum Mode { DEPOSIT, WITHDRAW }

    private final Bank bank;
    private final Mode mode;
    private final JTextField txtAccountNum;
    private final JTextField txtAmount;

    public TransactionDialog(JFrame parent, Bank bank, Mode mode) {
        super(parent, "GRIET Bank India - " + (mode == Mode.DEPOSIT ? "Deposit Funds" : "Withdraw Funds"), true);
        this.bank = bank;
        this.mode = mode;

        setSize(400, 260);
        setLocationRelativeTo(parent);
        setResizable(false);

        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(new EmptyBorder(20, 25, 20, 25));
        panel.setBackground(new Color(248, 250, 252));

        String titleText = mode == Mode.DEPOSIT ? "DEPOSIT TO ACCOUNT" : "WITHDRAW FROM ACCOUNT";
        JLabel lblTitle = new JLabel(titleText, SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTitle.setForeground(mode == Mode.DEPOSIT ? new Color(16, 185, 129) : new Color(245, 158, 11));
        panel.add(lblTitle, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(2, 2, 10, 15));
        form.setOpaque(false);

        form.add(new JLabel("Account Number:"));
        txtAccountNum = new JTextField();
        form.add(txtAccountNum);

        form.add(new JLabel("Amount ($):"));
        txtAmount = new JTextField();
        form.add(txtAmount);
        panel.add(form, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setOpaque(false);

        JButton btnAction = new JButton(mode == Mode.DEPOSIT ? "Deposit" : "Withdraw");
        btnAction.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnAction.setBackground(mode == Mode.DEPOSIT ? new Color(16, 185, 129) : new Color(245, 158, 11));
        btnAction.setForeground(Color.WHITE);
        btnAction.setFocusPainted(false);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(e -> dispose());

        btnAction.addActionListener(e -> handleTransaction());

        btnPanel.add(btnAction);
        btnPanel.add(btnCancel);
        panel.add(btnPanel, BorderLayout.SOUTH);

        add(panel);
    }

    private void handleTransaction() {
        String accNum = txtAccountNum.getText().trim();
        String amtStr = txtAmount.getText().trim();

        if (accNum.isEmpty() || amtStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both Account Number and Amount.", "Missing Input", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amtStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Amount must be a valid numeric value.", "Format Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            if (mode == Mode.DEPOSIT) {
                bank.deposit(accNum, amount);
                BankAccount acc = bank.findAccount(accNum);
                JOptionPane.showMessageDialog(this,
                        String.format("Successfully deposited $%.2f\nNew Balance: $%.2f", amount, acc.getBalance()),
                        "Deposit Complete", JOptionPane.INFORMATION_MESSAGE);
            } else {
                bank.withdraw(accNum, amount);
                BankAccount acc = bank.findAccount(accNum);
                JOptionPane.showMessageDialog(this,
                        String.format("Successfully withdrew $%.2f\nRemaining Balance: $%.2f", amount, acc.getBalance()),
                        "Withdrawal Complete", JOptionPane.INFORMATION_MESSAGE);
            }
            dispose();
        } catch (AccountNotFoundException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Account Error", JOptionPane.ERROR_MESSAGE);
        } catch (InvalidAmountException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Amount Error", JOptionPane.WARNING_MESSAGE);
        } catch (InsufficientBalanceException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Insufficient Funds", JOptionPane.WARNING_MESSAGE);
        } catch (MaxWithdrawLimitException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Limit Exceeded", JOptionPane.WARNING_MESSAGE);
        }
    }
}
