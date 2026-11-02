package model;

import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import java.io.Serializable;
import java.util.ArrayList;

// Unit II: abstract class - the common parent of SavingsAccount and CurrentAccount.
// We can never create a plain "Account" object, only one of its child classes.
// Unit IV: implements Comparable so accounts can be sorted by account number (used by TreeSet).
// Unit V: implements Serializable so accounts can be saved to a file and loaded back.
public abstract class Account implements Serializable, Comparable<Account> {

    // Unit V: version number used during serialization
    private static final long serialVersionUID = 1L;

    // Unit I: private fields - only this class can change them directly (encapsulation)
    private String accountNo;
    private String holderName;
    private double balance;

    // Unit IV: the transaction history of this account is stored in an ArrayList
    private ArrayList<Transaction> history;

    // Unit I: constructor for a new account with zero balance
    public Account(String accountNo, String holderName) {
        this.accountNo = accountNo;
        this.holderName = holderName;
        this.balance = 0;
        this.history = new ArrayList<Transaction>();
    }

    // Unit I: overloaded constructor that also takes an opening balance.
    // It first calls the constructor above using this(...)
    public Account(String accountNo, String holderName, double openingBalance) {
        this(accountNo, holderName);
        if (openingBalance > 0) {
            this.balance = openingBalance;
            history.add(new DepositTransaction(openingBalance, "Opening balance"));
        }
    }

    // Unit II: abstract method - every child class must give its own minimum balance
    public abstract double getMinimumBalance();

    // Unit II: abstract method - every child class must give its own type name
    public abstract String getAccountType();

    // Unit II: normal method that a child class may override.
    // It is the extra charge taken on every withdrawal (zero by default).
    public double getWithdrawCharge() {
        return 0;
    }

    // Unit II: extra details about the account, overridden by the child classes
    public String getExtraInfo() {
        return "";
    }

    // Unit III: synchronized - only one thread at a time can run this method on the same account.
    // Unit I: overloaded method - deposit with a default description
    public synchronized void deposit(double amount) throws InvalidAmountException {
        deposit(amount, "Cash deposit");
    }

    // Unit I: overloaded method - deposit with our own description (for example "Transfer from 1001")
    // Unit III: throws a custom exception if the amount is not valid
    public synchronized void deposit(double amount, String note) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Deposit amount must be greater than zero");
        }
        balance = balance + amount;
        history.add(new DepositTransaction(amount, note));
    }

    // Unit I: overloaded method - withdraw with a default description
    public synchronized void withdraw(double amount)
            throws InvalidAmountException, InsufficientFundsException {
        withdraw(amount, "Cash withdrawal");
    }

    // Unit III: synchronized withdraw that throws custom exceptions.
    // The balance must not go below the minimum balance of this type of account.
    public synchronized void withdraw(double amount, String note)
            throws InvalidAmountException, InsufficientFundsException {
        if (amount <= 0) {
            throw new InvalidAmountException("Withdraw amount must be greater than zero");
        }

        // getWithdrawCharge() and getMinimumBalance() run the child class version (overriding)
        double charge = getWithdrawCharge();
        double totalDeducted = amount + charge;
        if (balance - totalDeducted < getMinimumBalance()) {
            throw new InsufficientFundsException("Insufficient funds. Balance: Rs. "
                    + String.format("%.2f", balance) + ", minimum balance: Rs. "
                    + String.format("%.2f", getMinimumBalance())
                    + ", charge: Rs. " + String.format("%.2f", charge));
        }

        balance = balance - amount;
        history.add(new WithdrawTransaction(amount, note));

        // The charge is stored as a separate transaction so it is clearly visible
        if (charge > 0) {
            balance = balance - charge;
            history.add(new WithdrawTransaction(charge, "Withdrawal charge"));
        }
    }

    // Returns the account number
    public String getAccountNo() {
        return accountNo;
    }

    // Returns the name of the account holder
    public String getHolderName() {
        return holderName;
    }

    // Unit III: synchronized so a thread never reads the balance in the middle of an update
    public synchronized double getBalance() {
        return balance;
    }

    // Returns a COPY of the history, so code outside this class cannot change the original list
    public synchronized ArrayList<Transaction> getHistory() {
        return new ArrayList<Transaction>(history);
    }

    // Unit IV: Comparable - decides the "natural" order of accounts (by account number).
    // TreeSet uses this method to keep accounts sorted.
    @Override
    public int compareTo(Account other) {
        return this.accountNo.compareTo(other.accountNo);
    }

    // Unit II: overrides toString() from the Object class - one line used in the account list
    @Override
    public String toString() {
        return String.format("%-8s %-10s %-20s Rs. %10.2f",
                accountNo, getAccountType(), holderName, getBalance());
    }
}
