import exception.AccountNotFoundException;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;
import model.Account;
import service.BankService;
import service.ThreadDemoService;
import util.HtmlReportGenerator;
import util.InputParser;

// Unit I: the starting point of the program (root package).
// Shows a console menu in a loop, reads the user's choice with Scanner,
// and calls BankService to do the actual work.
public class Main {

    // Unit I: static fields - shared by all the static methods of Main
    private static Scanner scanner = new Scanner(System.in);
    private static BankService bank = new BankService();
    private static ThreadDemoService threadDemo = new ThreadDemoService();

    // Unit I: main method - the program starts here
    public static void main(String[] args) {
        bank.loadAccounts();
        boolean running = true;

        // Unit I: while loop - keeps showing the menu until the user chooses 0
        while (running) {
            showMenu();
            int choice = readNumber("Enter your choice: ");

            // Unit III: try / catch - errors from any menu option are handled here in one place
            try {
                // Unit I: switch statement - runs the code for the chosen option
                switch (choice) {
                    case 1:
                        createAccount();
                        break;
                    case 2:
                        deposit();
                        break;
                    case 3:
                        withdraw();
                        break;
                    case 4:
                        transfer();
                        break;
                    case 5:
                        viewBalance();
                        break;
                    case 6:
                        viewHistory();
                        break;
                    case 7:
                        bank.printSortedAccounts();
                        break;
                    case 8:
                        threadDemo.runDemo();
                        break;
                    case 9:
                        generateReport();
                        break;
                    case 0:
                        bank.saveAccounts();
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (AccountNotFoundException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (InvalidAmountException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (InsufficientFundsException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (IOException e) {
                System.out.println("File error: " + e.getMessage());
            }
        }

        scanner.close();
        System.out.println("Thank you for using the Bank Management System.");
    }

    // Prints the menu options
    private static void showMenu() {
        System.out.println();
        System.out.println("========== BANK MANAGEMENT SYSTEM ==========");
        System.out.println("1. Create account");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. Transfer");
        System.out.println("5. View balance");
        System.out.println("6. View transaction history");
        System.out.println("7. List all accounts (sorted)");
        System.out.println("8. Run multithreading demo");
        System.out.println("9. Generate HTML report");
        System.out.println("0. Save and exit");
    }

    // Shows a message and returns the line the user types
    private static String readLine(String message) {
        System.out.print(message);
        return scanner.nextLine().trim();
    }

    // Reads a whole number. Returns -1 if the user did not type a valid number.
    private static int readNumber(String message) {
        String text = readLine(message);
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // Menu option 1: asks for the details and creates a new account
    private static void createAccount() throws InvalidAmountException {
        int type = readNumber("Account type (1 = Savings, 2 = Current): ");
        if (type != 1 && type != 2) {
            System.out.println("Please choose 1 or 2.");
            return;
        }

        String name = readLine("Holder name: ");
        if (name.length() == 0) {
            System.out.println("Name cannot be empty.");
            return;
        }

        double openingBalance = InputParser.parseAmount(readLine("Opening balance: "));
        Account account = bank.createAccount(type, name, openingBalance);
        System.out.println("Account created. Your account number is " + account.getAccountNo());
    }

    // Menu option 2: reads "accountNo,amount" in one line and deposits the money
    private static void deposit() throws AccountNotFoundException, InvalidAmountException {
        String line = readLine("Enter accountNo,amount (example 1001,500): ");
        InputParser.ParsedInput input = InputParser.parseAccountAndAmount(line);
        bank.deposit(input.getAccountNo(), input.getAmount());
        System.out.println("Deposit successful.");
    }

    // Menu option 3: reads "accountNo,amount" in one line and withdraws the money
    private static void withdraw()
            throws AccountNotFoundException, InvalidAmountException, InsufficientFundsException {
        String line = readLine("Enter accountNo,amount (example 1001,500): ");
        InputParser.ParsedInput input = InputParser.parseAccountAndAmount(line);
        bank.withdraw(input.getAccountNo(), input.getAmount());
        System.out.println("Withdrawal successful.");
    }

    // Menu option 4: moves money from one account to another
    private static void transfer()
            throws AccountNotFoundException, InvalidAmountException, InsufficientFundsException {
        String fromAccountNo = readLine("From account number: ");
        String line = readLine("Enter toAccountNo,amount (example 1002,500): ");
        InputParser.ParsedInput input = InputParser.parseAccountAndAmount(line);
        bank.transfer(fromAccountNo, input.getAccountNo(), input.getAmount());
        System.out.println("Transfer successful.");
    }

    // Menu option 5: shows the balance and details of one account
    private static void viewBalance() throws AccountNotFoundException {
        String accountNo = readLine("Account number: ");
        Account account = bank.findAccount(accountNo);
        System.out.println("Holder : " + account.getHolderName());
        System.out.println("Type   : " + account.getAccountType());
        System.out.println("Balance: Rs. " + String.format("%.2f", account.getBalance()));
        System.out.println("Minimum balance: Rs. " + String.format("%.2f", account.getMinimumBalance()));
        // Unit II: polymorphism - runs the getExtraInfo() of SavingsAccount or CurrentAccount
        System.out.println(account.getExtraInfo());
    }

    // Menu option 6: shows the transaction history, sorted the way the user chooses
    private static void viewHistory() throws AccountNotFoundException {
        String accountNo = readLine("Account number: ");
        int sortChoice = readNumber("Sort by (1 = date, 2 = amount, 3 = newest first): ");
        bank.printHistory(accountNo, sortChoice);
    }

    // Menu option 9: writes report.html with all accounts and one account's statement
    private static void generateReport() throws AccountNotFoundException, IOException {
        if (bank.getAccountCount() == 0) {
            System.out.println("Create an account first.");
            return;
        }
        String accountNo = readLine("Account number for the statement: ");
        Account account = bank.findAccount(accountNo);
        Path reportPath = HtmlReportGenerator.generateReport(bank.getSortedAccounts(), account);
        System.out.println("Report saved to " + reportPath.toAbsolutePath());
        System.out.println("Open this file in any web browser.");
    }
}
