package exception;

// Unit III: custom exception.
// Thrown when a withdrawal would take the balance below the minimum balance.
public class InsufficientFundsException extends Exception {

    // Unit V: version number used during serialization (Exception is Serializable)
    private static final long serialVersionUID = 1L;

    // Constructor that passes the error message to the parent Exception class using super
    public InsufficientFundsException(String message) {
        super(message);
    }
}
