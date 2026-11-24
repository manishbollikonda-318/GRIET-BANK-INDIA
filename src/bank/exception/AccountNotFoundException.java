package bank.exception;

/**
 * Thrown when an account lookup by account number yields no match.
 */
public class AccountNotFoundException extends BankException {
    private static final long serialVersionUID = 1L;

    public AccountNotFoundException(String message) {
        super(message);
    }
}
