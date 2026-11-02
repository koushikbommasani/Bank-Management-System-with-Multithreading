package exception;

// Unit III: custom exception.
// Thrown when the user types an account number that does not exist.
public class AccountNotFoundException extends Exception {

    // Unit V: version number used during serialization (Exception is Serializable)
    private static final long serialVersionUID = 1L;

    // Constructor that passes the error message to the parent Exception class using super
    public AccountNotFoundException(String message) {
        super(message);
    }
}
