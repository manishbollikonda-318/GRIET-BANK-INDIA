package bank.exception;

/**
 * Base checked exception for all banking operations.
 */
public class BankException extends Exception {
    private static final long serialVersionUID = 1L;

    public BankException(String message) {
        super(message);
    }
}
