package model;

// Unit II: inheritance - CurrentAccount is a child class of Account.
// Rules: no minimum balance, no interest, but Rs. 5 is charged on every withdrawal.
public class CurrentAccount extends Account {

    // Unit V: version number used during serialization
    private static final long serialVersionUID = 1L;

    // Unit I: static final constants
    public static final double MINIMUM_BALANCE = 0;
    public static final double WITHDRAW_CHARGE = 5;

    // Unit II: constructor chain - calls the parent constructor using super
    public CurrentAccount(String accountNo, String holderName) {
        super(accountNo, holderName);
    }

    // Unit I: overloaded constructor - also takes an opening balance, passed to the parent with super
    public CurrentAccount(String accountNo, String holderName, double openingBalance) {
        super(accountNo, holderName, openingBalance);
    }

    // Unit II: method overriding - current accounts have no minimum balance
    @Override
    public double getMinimumBalance() {
        return MINIMUM_BALANCE;
    }

    // Unit II: method overriding - returns the type name of this account
    @Override
    public String getAccountType() {
        return "Current";
    }

    // Unit II: method overriding - current accounts pay Rs. 5 on every withdrawal
    @Override
    public double getWithdrawCharge() {
        return WITHDRAW_CHARGE;
    }

    // Unit II: method overriding - shows the withdrawal charge for this account
    @Override
    public String getExtraInfo() {
        return "Charge per withdrawal: Rs. " + String.format("%.2f", WITHDRAW_CHARGE);
    }
}
