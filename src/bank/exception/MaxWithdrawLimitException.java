package bank.exception;

/**
 * Thrown when an account withdrawal exceeds the maximum allowed per-transaction limit.
 */
public class MaxWithdrawLimitException extends BankException {
    private static final long serialVersionUID = 1L;

    public MaxWithdrawLimitException(String message) {
        super(message);
    }
}
