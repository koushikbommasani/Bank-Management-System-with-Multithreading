package exception;

// Unit III: custom exception.
// Thrown when an amount is zero, negative, not a number, or the input format is wrong.
public class InvalidAmountException extends Exception {

    // Unit V: version number used during serialization (Exception is Serializable)
    private static final long serialVersionUID = 1L;

    // Constructor that passes the error message to the parent Exception class using super
    public InvalidAmountException(String message) {
        super(message);
    }
}
