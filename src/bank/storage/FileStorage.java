package bank.storage;

import bank.model.Bank;
import bank.model.SavingsAccount;
import bank.model.CurrentAccount;
import bank.model.StudentAccount;

import java.io.*;

/**
 * Handles object serialization and deserialization for persistent bank data.
 */
public class FileStorage {
    public static final String DEFAULT_FILE = "data.bin";

    /**
     * Loads the Bank instance from disk. If not found or corrupt, returns a initialized Bank with sample accounts.
     */
    public static Bank load(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            Bank initialBank = new Bank();
            initialBank.addAccount(new SavingsAccount("Alex Morgan", 12500.0, 4000.0));
            initialBank.addAccount(new CurrentAccount("Global Trading LLC", 45000.0, "TL-78921-X"));
            initialBank.addAccount(new StudentAccount("Emily Watson", 1500.0, "University of Engineering"));
            save(initialBank, filePath);
            return initialBank;
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            return (Bank) in.readObject();
        } catch (Exception e) {
            System.err.println("Warning: Could not read persistence file (" + e.getMessage() + "). Generating new database.");
            return new Bank();
        }
    }

    /**
     * Serializes the Bank instance to disk.
     */
    public static boolean save(Bank bank, String filePath) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath))) {
            out.writeObject(bank);
            out.flush();
            return true;
        } catch (IOException e) {
            System.err.println("Error saving bank data to file: " + e.getMessage());
            return false;
        }
    }
}
