package bank;

import bank.gui.LoginFrame;
import bank.model.Bank;
import bank.storage.FileStorage;

import javax.swing.*;

/**
 * Main application bootstrap for the Banking Management System.
 */
public class Application {
    public static void main(String[] args) {
        // Set cross-platform System Look and Feel for native OS appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // Fallback to default Swing Look & Feel
        }

        // Initialize state from persistence storage
        Bank bank = FileStorage.load(FileStorage.DEFAULT_FILE);

        // Launch UI on Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame(bank);
            loginFrame.setVisible(true);
        });
    }
}
