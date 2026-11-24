package bank.exception;

/**
 * Thrown when a withdrawal request would breach the minimum balance threshold.
 */
public class InsufficientBalanceException extends BankException {
    private static final long serialVersionUID = 1L;

    public InsufficientBalanceException(String message) {
        super(message);
    }
}
