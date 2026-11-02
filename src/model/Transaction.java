package model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Unit II: interface - lists the methods every kind of transaction must have.
// It extends Serializable (Unit V) so transactions are saved to the file along with the account.
public interface Transaction extends Serializable {

    // Unit II: a field in an interface is automatically public static final (a constant).
    // It is the pattern used to show dates, for example 02-11-2026 14:05:30
    DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    // Returns the amount of money in this transaction
    double getAmount();

    // Returns the date and time when the transaction happened
    LocalDateTime getDateTime();

    // Returns the date and time as readable text
    String getFormattedDate();

    // Returns "DEPOSIT" or "WITHDRAW"
    String getType();

    // Returns a short description, for example "Cash deposit"
    String getNote();
}
