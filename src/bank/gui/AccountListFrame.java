package bank.gui;

import bank.model.Bank;
import bank.model.BankAccount;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;

/**
 * Modern directory window presenting all accounts in a sortable, filterable table.
 */
public class AccountListFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    private final Bank bank;
    private final DefaultTableModel tableModel;
    private final TableRowSorter<DefaultTableModel> sorter;
    private final JTextField txtFilter;

    public AccountListFrame(Bank bank) {
        this.bank = bank;

        setTitle("GRIET Bank India - Account Directory");
        setSize(780, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel content = new JPanel(new BorderLayout(15, 15));
        content.setBorder(new EmptyBorder(20, 20, 20, 20));
        content.setBackground(new Color(248, 250, 252));

        // Header and Search Filter
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setOpaque(false);

        JLabel lblTitle = new JLabel("GRIET BANK - REGISTERED ACCOUNT DIRECTORY", SwingConstants.LEFT);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitle.setForeground(new Color(30, 41, 59));
        topPanel.add(lblTitle, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchPanel.setOpaque(false);
        searchPanel.add(new JLabel("Quick Search:"));
        txtFilter = new JTextField(25);
        searchPanel.add(txtFilter);
        topPanel.add(searchPanel, BorderLayout.SOUTH);

        content.add(topPanel, BorderLayout.NORTH);

        // Table setup
        String[] columns = {"Account #", "Holder Name", "Account Type", "Balance (₹)", "Additional Details"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(tableModel);
        table.setRowHeight(26);
        table.setFont(new Font("SansSerif", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(226, 232, 240));

        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        txtFilter.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            private void filter() {
                String text = txtFilter.getText().trim();
                if (text.isEmpty()) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
        });

        populateTable();

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(203, 213, 225)));
        content.add(scrollPane, BorderLayout.CENTER);

        // Bottom panel
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        bottomPanel.setOpaque(false);

        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(e -> populateTable());

        JButton btnClose = new JButton("Close");
        btnClose.addActionListener(e -> dispose());

        bottomPanel.add(btnRefresh);
        bottomPanel.add(btnClose);
        content.add(bottomPanel, BorderLayout.SOUTH);

        add(content);
    }

    private void populateTable() {
        tableModel.setRowCount(0);
        List<BankAccount> list = bank.getAccounts();
        for (BankAccount acc : list) {
            tableModel.addRow(new Object[]{
                    acc.getAccountNumber(),
                    acc.getAccountHolderName(),
                    acc.getAccountType(),
                    String.format("%.2f", acc.getBalance()),
                    acc.getSpecificDetail()
            });
        }
    }
}
