package bank.gui;

import bank.model.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Unified dialog for creating new Savings, Current, or Student accounts.
 * Dynamically adjusts input fields based on selected account type.
 */
public class AccountDialog extends JDialog {
    private static final long serialVersionUID = 1L;

    private final Bank bank;
    private final JComboBox<String> cmbType;
    private final JTextField txtName;
    private final JTextField txtBalance;
    private final JLabel lblExtra;
    private final JTextField txtExtra;

    public AccountDialog(JFrame parent, Bank bank) {
        super(parent, "GRIET Bank India - Open New Account", true);
        this.bank = bank;

        setSize(460, 360);
        setLocationRelativeTo(parent);
        setResizable(false);

        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(new EmptyBorder(20, 25, 20, 25));
        panel.setBackground(new Color(248, 250, 252));

        JLabel lblHeading = new JLabel("GRIET BANK - CREATE ACCOUNT", SwingConstants.CENTER);
        lblHeading.setFont(new Font("SansSerif", Font.BOLD, 17));
        lblHeading.setForeground(new Color(30, 41, 59));
        panel.add(lblHeading, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(4, 2, 10, 14));
        form.setOpaque(false);

        form.add(new JLabel("Account Type:"));
        cmbType = new JComboBox<>(new String[]{"Savings Account", "Current Account", "Student Account"});
        form.add(cmbType);

        form.add(new JLabel("Account Holder Name:"));
        txtName = new JTextField();
        form.add(txtName);

        form.add(new JLabel("Initial Deposit (₹):"));
        txtBalance = new JTextField();
        form.add(txtBalance);

        lblExtra = new JLabel("Max Withdraw Limit (₹):");
        txtExtra = new JTextField("5000");
        form.add(lblExtra);
        form.add(txtExtra);

        cmbType.addActionListener(e -> updateExtraField());

        panel.add(form, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setOpaque(false);

        JButton btnSubmit = new JButton("Create Account");
        btnSubmit.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnSubmit.setBackground(new Color(37, 99, 235));
        btnSubmit.setForeground(Color.WHITE);
        btnSubmit.setFocusPainted(false);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(e -> dispose());

        btnSubmit.addActionListener(e -> handleSubmit());

        btnPanel.add(btnSubmit);
        btnPanel.add(btnCancel);
        panel.add(btnPanel, BorderLayout.SOUTH);

        add(panel);
    }

    private void updateExtraField() {
        String type = (String) cmbType.getSelectedItem();
        if ("Savings Account".equals(type)) {
            lblExtra.setText("Max Withdraw Limit (₹):");
            txtExtra.setText("5000");
        } else if ("Current Account".equals(type)) {
            lblExtra.setText("Trade License Number:");
            txtExtra.setText("TL-2024-");
        } else {
            lblExtra.setText("Institution Name:");
            txtExtra.setText("State University");
        }
    }

    private void handleSubmit() {
        String name = txtName.getText().trim();
        String balanceStr = txtBalance.getText().trim();
        String extra = txtExtra.getText().trim();

        if (name.isEmpty() || balanceStr.isEmpty() || extra.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields.", "Input Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double balance;
        try {
            balance = Double.parseDouble(balanceStr);
            if (balance <= 0) {
                JOptionPane.showMessageDialog(this, "Initial balance must be positive.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid numeric amount for balance.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String type = (String) cmbType.getSelectedItem();
        BankAccount newAccount;

        try {
            if ("Savings Account".equals(type)) {
                double limit = Double.parseDouble(extra);
                if (balance < SavingsAccount.DEFAULT_MIN_BALANCE) {
                    JOptionPane.showMessageDialog(this,
                            "Savings account requires minimum opening balance of ₹" + SavingsAccount.DEFAULT_MIN_BALANCE,
                            "Minimum Balance Violation", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                newAccount = new SavingsAccount(name, balance, limit);
            } else if ("Current Account".equals(type)) {
                if (balance < CurrentAccount.DEFAULT_MIN_BALANCE) {
                    JOptionPane.showMessageDialog(this,
                            "Current account requires minimum opening balance of ₹" + CurrentAccount.DEFAULT_MIN_BALANCE,
                            "Minimum Balance Violation", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                newAccount = new CurrentAccount(name, balance, extra);
            } else {
                if (balance < StudentAccount.STUDENT_MIN_BALANCE) {
                    JOptionPane.showMessageDialog(this,
                            "Student account requires minimum opening balance of ₹" + StudentAccount.STUDENT_MIN_BALANCE,
                            "Minimum Balance Violation", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                newAccount = new StudentAccount(name, balance, extra);
            }

            bank.addAccount(newAccount);
            JOptionPane.showMessageDialog(this,
                    "Account Created Successfully!\n" +
                    "Account Number: " + newAccount.getAccountNumber() + "\n" +
                    "Holder: " + newAccount.getAccountHolderName() + "\n" +
                    "Opening Balance: ₹" + String.format("%.2f", newAccount.getBalance()),
                    "Account Created", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid numeric value for the transaction limit.", "Format Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
