package service;

import exception.AccountNotFoundException;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.TreeSet;
import model.Account;
import model.CurrentAccount;
import model.SavingsAccount;
import model.Transaction;
import util.FileStorage;
import util.Repository;

// Service class - contains all the banking work: create, deposit, withdraw, transfer,
// history and the sorted account list. Main only reads input and calls these methods.
// Unit III: throws custom exceptions. Unit IV: uses collections, Comparator, Iterator.
public class BankService {

    // Unit I: static final constant - account numbers start from 1001
    private static final int FIRST_ACCOUNT_NUMBER = 1001;

    // Unit IV: Comparator written as a lambda expression - sorts transactions by amount (smallest first)
    private static final Comparator<Transaction> BY_AMOUNT =
            (first, second) -> Double.compare(first.getAmount(), second.getAmount());

    // Unit IV: Comparator written as a lambda expression - sorts transactions by date (oldest first)
    private static final Comparator<Transaction> BY_DATE =
            (first, second) -> first.getDateTime().compareTo(second.getDateTime());

    // Unit III: the generic Repository is used here with the type Account
    private Repository<Account> repository;

    // Constructor - starts with an empty repository
    public BankService() {
        repository = new Repository<Account>();
    }

    // Loads saved accounts from the file into the repository (deserialization)
    public void loadAccounts() {
        Account[] savedAccounts = FileStorage.loadAccounts();
        // Unit I: for-each loop over an array
        for (Account account : savedAccounts) {
            repository.add(account.getAccountNo(), account);
        }
        System.out.println("Loaded " + savedAccounts.length + " account(s) from file.");
    }

    // Saves all accounts to the file (serialization)
    public void saveAccounts() {
        ArrayList<Account> list = repository.getAll();
        // Unit I: copy the ArrayList into an array, because the file stores an Account array
        Account[] accountArray = new Account[list.size()];
        for (int i = 0; i < list.size(); i++) {
            accountArray[i] = list.get(i);
        }
        if (FileStorage.saveAccounts(accountArray)) {
            System.out.println("Saved " + accountArray.length + " account(s) to file.");
        }
    }

    // Makes the next account number, for example 1001, 1002, 1003 ...
    private String generateAccountNo() {
        return String.valueOf(FIRST_ACCOUNT_NUMBER + repository.size());
    }

    // Creates a savings account (type 1) or a current account (type 2)
    public Account createAccount(int type, String holderName, double openingBalance)
            throws InvalidAmountException {
        if (openingBalance < 0) {
            throw new InvalidAmountException("Opening balance cannot be negative");
        }

        String accountNo = generateAccountNo();
        // Unit II: the variable type is the parent class Account, the object is a child class
        Account account;
        if (type == 1) {
            account = new SavingsAccount(accountNo, holderName, openingBalance);
        } else {
            account = new CurrentAccount(accountNo, holderName, openingBalance);
        }

        if (openingBalance < account.getMinimumBalance()) {
            throw new InvalidAmountException("Opening balance must be at least Rs. "
                    + String.format("%.2f", account.getMinimumBalance()));
        }

        repository.add(accountNo, account);
        return account;
    }

    // Finds an account by number. Unit III: throws a custom exception if it does not exist.
    public Account findAccount(String accountNo) throws AccountNotFoundException {
        Account account = repository.findById(accountNo);
        if (account == null) {
            throw new AccountNotFoundException("Account " + accountNo + " does not exist");
        }
        return account;
    }

    // Deposits money into an account
    public void deposit(String accountNo, double amount)
            throws AccountNotFoundException, InvalidAmountException {
        Account account = findAccount(accountNo);
        account.deposit(amount);
    }

    // Withdraws money from an account
    public void withdraw(String accountNo, double amount)
            throws AccountNotFoundException, InvalidAmountException, InsufficientFundsException {
        Account account = findAccount(accountNo);
        account.withdraw(amount);
    }

    // Moves money from one account to another.
    // The withdrawal is done first, so if it fails the deposit never happens.
    public void transfer(String fromAccountNo, String toAccountNo, double amount)
            throws AccountNotFoundException, InvalidAmountException, InsufficientFundsException {
        if (fromAccountNo.equals(toAccountNo)) {
            throw new InvalidAmountException("Cannot transfer to the same account");
        }
        Account fromAccount = findAccount(fromAccountNo);
        Account toAccount = findAccount(toAccountNo);

        // Uses the overloaded versions of withdraw and deposit that take a description
        fromAccount.withdraw(amount, "Transfer to " + toAccountNo);
        toAccount.deposit(amount, "Transfer from " + fromAccountNo);
    }

    // Unit IV: TreeSet keeps accounts sorted automatically using Account.compareTo()
    public TreeSet<Account> getSortedAccounts() {
        return new TreeSet<Account>(repository.getAll());
    }

    // Prints all accounts sorted by account number
    public void printSortedAccounts() {
        if (repository.size() == 0) {
            System.out.println("No accounts yet.");
            return;
        }
        System.out.println(String.format("%-8s %-10s %-20s %14s", "Acc No", "Type", "Holder", "Balance"));
        // Unit III: calling the generic method of Repository with a TreeSet<Account>
        Repository.printAll(getSortedAccounts());
    }

    // Returns how many accounts exist
    public int getAccountCount() {
        return repository.size();
    }

    // Prints the history of one account.
    // sortChoice: 1 = by date (oldest first), 2 = by amount, 3 = newest first
    public void printHistory(String accountNo, int sortChoice) throws AccountNotFoundException {
        Account account = findAccount(accountNo);
        ArrayList<Transaction> list = account.getHistory();

        if (list.isEmpty()) {
            System.out.println("No transactions for this account yet.");
            return;
        }

        System.out.println(String.format("%-20s %-10s %12s   %s", "Date", "Type", "Amount", "Description"));

        if (sortChoice == 2) {
            // Unit IV: sort using the Comparator lambda for amount, then go forward with an Iterator
            Collections.sort(list, BY_AMOUNT);
            printWithIterator(list);
        } else if (sortChoice == 3) {
            // Unit IV: sort by date, then go BACKWARDS with a ListIterator (newest first)
            Collections.sort(list, BY_DATE);
            printWithListIteratorReversed(list);
        } else {
            // Unit IV: sort using the Comparator lambda for date, then go forward with an Iterator
            Collections.sort(list, BY_DATE);
            printWithIterator(list);
        }
    }

    // Unit IV: Iterator - goes through the list from the first item to the last
    private void printWithIterator(ArrayList<Transaction> list) {
        Iterator<Transaction> iterator = list.iterator();
        while (iterator.hasNext()) {
            Transaction transaction = iterator.next();
            printTransaction(transaction);
        }
    }

    // Unit IV: ListIterator - can move backwards, so we start at the end and go to the front
    private void printWithListIteratorReversed(ArrayList<Transaction> list) {
        ListIterator<Transaction> listIterator = list.listIterator(list.size());
        while (listIterator.hasPrevious()) {
            Transaction transaction = listIterator.previous();
            printTransaction(transaction);
        }
    }

    // Prints one transaction as one line
    private void printTransaction(Transaction transaction) {
        System.out.println(String.format("%-20s %-10s %12.2f   %s",
                transaction.getFormattedDate(), transaction.getType(),
                transaction.getAmount(), transaction.getNote()));
    }
}
