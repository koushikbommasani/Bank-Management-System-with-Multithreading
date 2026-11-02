package model;

import java.time.LocalDateTime;

// Unit II: class that implements the Transaction interface.
// Stores one withdrawal (money taken out of an account).
public class WithdrawTransaction implements Transaction {

    // Unit V: version number used during serialization
    private static final long serialVersionUID = 1L;

    private double amount;
    private String note;
    private LocalDateTime dateTime;

    // Constructor - the date and time is taken automatically as "now"
    public WithdrawTransaction(double amount, String note) {
        this.amount = amount;
        this.note = note;
        this.dateTime = LocalDateTime.now();
    }

    // Unit II: implements the interface method - returns the amount
    @Override
    public double getAmount() {
        return amount;
    }

    // Unit II: implements the interface method - returns the date and time
    @Override
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    // Unit II: implements the interface method - date and time as text using the interface constant
    @Override
    public String getFormattedDate() {
        return dateTime.format(DATE_FORMAT);
    }

    // Unit II: implements the interface method - the type of this transaction
    @Override
    public String getType() {
        return "WITHDRAW";
    }

    // Unit II: implements the interface method - the description
    @Override
    public String getNote() {
        return note;
    }
}
