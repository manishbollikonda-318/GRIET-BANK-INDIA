package bank.gui;

import bank.model.Bank;
import bank.storage.FileStorage;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Main application dashboard displaying all operational banking modules.
 */
public class MainDashboard extends JFrame {
    private static final long serialVersionUID = 1L;

    private final Bank bank;
    private final JLabel lblStats;

    public MainDashboard(Bank bank) {
        this.bank = bank;

        setTitle("GRIET Bank India - Operations Dashboard");
        setSize(560, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmExit();
            }
        });

        JPanel content = new JPanel(new BorderLayout(15, 20));
        content.setBorder(new EmptyBorder(25, 30, 25, 30));
        content.setBackground(new Color(241, 245, 249));

        // Header Section
        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        headerPanel.setOpaque(false);

        JLabel lblTitle = new JLabel("GRIET BANK OF INDIA", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitle.setForeground(new Color(15, 23, 42));

        lblStats = new JLabel("Active Accounts: " + bank.getAccountCount(), SwingConstants.CENTER);
        lblStats.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblStats.setForeground(new Color(71, 85, 105));

        headerPanel.add(lblTitle);
        headerPanel.add(lblStats);
        content.add(headerPanel, BorderLayout.NORTH);

        // Buttons Grid
        JPanel grid = new JPanel(new GridLayout(5, 1, 0, 12));
        grid.setOpaque(false);

        JButton btnAdd = createActionButton("1. Create New Account", new Color(14, 116, 144));
        JButton btnDeposit = createActionButton("2. Deposit Funds", new Color(16, 185, 129));
        JButton btnWithdraw = createActionButton("3. Withdraw Funds", new Color(245, 158, 11));
        JButton btnList = createActionButton("4. View Account Directory", new Color(99, 102, 241));
        JButton btnExit = createActionButton("5. Save & Exit", new Color(239, 68, 68));

        btnAdd.addActionListener(e -> {
            new AccountDialog(this, bank).setVisible(true);
            updateStatus();
        });

        btnDeposit.addActionListener(e -> new TransactionDialog(this, bank, TransactionDialog.Mode.DEPOSIT).setVisible(true));
        btnWithdraw.addActionListener(e -> new TransactionDialog(this, bank, TransactionDialog.Mode.WITHDRAW).setVisible(true));
        btnList.addActionListener(e -> new AccountListFrame(bank).setVisible(true));
        btnExit.addActionListener(e -> confirmExit());

        grid.add(btnAdd);
        grid.add(btnDeposit);
        grid.add(btnWithdraw);
        grid.add(btnList);
        grid.add(btnExit);

        content.add(grid, BorderLayout.CENTER);
        add(content);
    }

    public void updateStatus() {
        lblStats.setText("Active Accounts: " + bank.getAccountCount());
    }

    private JButton createActionButton(String text, Color baseColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setBackground(Color.WHITE);
        btn.setForeground(new Color(30, 41, 59));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225), 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        return btn;
    }

    private void confirmExit() {
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Would you like to save changes and exit the application?",
                "Confirm Exit",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (choice == JOptionPane.YES_OPTION) {
            FileStorage.save(bank, FileStorage.DEFAULT_FILE);
            JOptionPane.showMessageDialog(this, "Data successfully saved. Goodbye!", "System Exit", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        } else if (choice == JOptionPane.NO_OPTION) {
            System.exit(0);
        }
    }
}
