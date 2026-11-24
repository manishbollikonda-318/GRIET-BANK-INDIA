package bank.exception;

/**
 * Thrown when an input transaction amount is zero, negative, or invalid.
 */
public class InvalidAmountException extends BankException {
    private static final long serialVersionUID = 1L;

    public InvalidAmountException(String message) {
        super(message);
    }
}
