package model;

// Unit II: inheritance - SavingsAccount is a child class of Account.
// Rules: minimum balance of Rs. 1000, no withdrawal charge, earns 4% interest per year.
public class SavingsAccount extends Account {

    // Unit V: version number used during serialization
    private static final long serialVersionUID = 1L;

    // Unit I: static final constants - the same value for every savings account and never changes
    public static final double MINIMUM_BALANCE = 1000;
    public static final double INTEREST_RATE = 4.0;

    // Unit II: constructor chain - calls the parent constructor using super
    public SavingsAccount(String accountNo, String holderName) {
        super(accountNo, holderName);
    }

    // Unit I: overloaded constructor - also takes an opening balance, passed to the parent with super
    public SavingsAccount(String accountNo, String holderName, double openingBalance) {
        super(accountNo, holderName, openingBalance);
    }

    // Unit II: method overriding - savings accounts need Rs. 1000 minimum balance
    @Override
    public double getMinimumBalance() {
        return MINIMUM_BALANCE;
    }

    // Unit II: method overriding - returns the type name of this account
    @Override
    public String getAccountType() {
        return "Savings";
    }

    // Calculates the interest for one year on the current balance
    public double calculateInterest() {
        return getBalance() * INTEREST_RATE / 100;
    }

    // Unit II: method overriding - shows the yearly interest for this account
    @Override
    public String getExtraInfo() {
        return "Yearly interest at " + INTEREST_RATE + "%: Rs. "
                + String.format("%.2f", calculateInterest());
    }
}
