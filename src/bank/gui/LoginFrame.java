package bank.gui;

import bank.model.Bank;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Authentication window providing administrator access to banking services.
 */
public class LoginFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    private final Bank bank;
    private final JTextField txtUsername;
    private final JPasswordField txtPassword;

    public LoginFrame(Bank bank) {
        this.bank = bank;

        setTitle("GRIET Bank India - Secure Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 310);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(new EmptyBorder(25, 30, 25, 30));
        mainPanel.setBackground(new Color(245, 247, 250));

        // Header
        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 0, 4));
        headerPanel.setOpaque(false);
        JLabel lblTitle = new JLabel("GRIET BANK INDIA", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblTitle.setForeground(new Color(24, 43, 73));

        JLabel lblSub = new JLabel("Administrative Portal Authentication", SwingConstants.CENTER);
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblSub.setForeground(new Color(100, 116, 139));
        headerPanel.add(lblTitle);
        headerPanel.add(lblSub);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Form fields
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 15));
        formPanel.setOpaque(false);

        JLabel lblUser = new JLabel("Username:");
        lblUser.setFont(new Font("SansSerif", Font.BOLD, 13));
        txtUsername = new JTextField("admin");
        txtUsername.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JLabel lblPass = new JLabel("Password:");
        lblPass.setFont(new Font("SansSerif", Font.BOLD, 13));
        txtPassword = new JPasswordField("admin");
        txtPassword.setFont(new Font("SansSerif", Font.PLAIN, 13));

        formPanel.add(lblUser);
        formPanel.add(txtUsername);
        formPanel.add(lblPass);
        formPanel.add(txtPassword);
        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Button actions
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        btnPanel.setOpaque(false);

        JButton btnLogin = new JButton("Login");
        btnLogin.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnLogin.setBackground(new Color(30, 64, 175));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setPreferredSize(new Dimension(110, 34));

        JButton btnClear = new JButton("Clear");
        btnClear.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btnClear.setPreferredSize(new Dimension(90, 34));

        btnLogin.addActionListener(this::handleLogin);
        txtPassword.addActionListener(this::handleLogin);
        btnClear.addActionListener(e -> {
            txtUsername.setText("");
            txtPassword.setText("");
            txtUsername.requestFocus();
        });

        btnPanel.add(btnLogin);
        btnPanel.add(btnClear);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void handleLogin(ActionEvent e) {
        String user = txtUsername.getText().trim();
        String pass = new String(txtPassword.getPassword()).trim();

        if ("admin".equals(user) && "admin".equals(pass)) {
            JOptionPane.showMessageDialog(this, "Login successful. Welcome to Bank Manager!",
                    "Authentication Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            SwingUtilities.invokeLater(() -> new MainDashboard(bank).setVisible(true));
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials. Default is admin / admin",
                    "Access Denied", JOptionPane.ERROR_MESSAGE);
            txtPassword.setText("");
            txtPassword.requestFocus();
        }
    }
}
