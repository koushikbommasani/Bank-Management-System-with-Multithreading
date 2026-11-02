package util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import model.Account;
import model.Transaction;

// Unit V: creates report.html using Java NIO (Path and Files).
// The page has two plain tables: all accounts, and the statement of one chosen account.
// Every HTML line is added to an ArrayList (Unit IV) and then written to the file in one go.
public class HtmlReportGenerator {

    // Unit I: static final constant - the name of the report file
    private static final String REPORT_FILE = "report.html";

    // Builds the whole page and writes it to report.html. Returns the path of the file.
    public static Path generateReport(Collection<Account> accounts, Account statementAccount)
            throws IOException {
        ArrayList<String> lines = new ArrayList<String>();
        addPageStart(lines);
        addSummary(lines, accounts);
        addAccountsTable(lines, accounts);
        addStatementTable(lines, statementAccount);
        addPageEnd(lines);

        // Unit V: Path points to the file, Files.write writes every line of the list into it
        Path path = Paths.get(REPORT_FILE);
        Files.write(path, lines);
        return path;
    }

    // Adds the top part of the page: head, title, style and main heading
    private static void addPageStart(ArrayList<String> lines) {
        // Tells the browser that this file is an HTML page
        lines.add("<!DOCTYPE html>");
        // Start of the whole page
        lines.add("<html>");
        // Start of the head part (page settings, not shown as content)
        lines.add("<head>");
        // Lets the browser show all characters correctly
        lines.add("<meta charset=\"UTF-8\">");
        // Text shown on the browser tab
        lines.add("<title>Bank Report</title>");
        addStyle(lines);
        // End of the head part
        lines.add("</head>");
        // Start of the visible part of the page
        lines.add("<body>");
        // Big heading at the top of the page
        lines.add("<h1>Bank Management System - Report</h1>");
        // A paragraph showing when this report was made
        lines.add("<p>Generated on: " + LocalDateTime.now().format(Transaction.DATE_FORMAT) + "</p>");
    }

    // Adds the small CSS block that controls how the page looks
    private static void addStyle(ArrayList<String> lines) {
        // Start of the CSS style block
        lines.add("<style>");
        // Use a simple font for the whole page and leave space around the edges
        lines.add("body { font-family: Arial, sans-serif; margin: 20px; }");
        // Dark blue colour for the headings
        lines.add("h1, h2 { color: navy; }");
        // Join cell borders into single lines and leave space below each table
        lines.add("table { border-collapse: collapse; margin-bottom: 30px; }");
        // Give every header cell and data cell a thin dark border and some inner space
        lines.add("th, td { border: 1px solid #444444; padding: 8px 12px; }");
        // Light blue background colour for the header cells
        lines.add("th { background-color: #d9e2f3; }");
        // Rows with class "deposit" get green text
        lines.add(".deposit { color: green; }");
        // Rows with class "withdraw" get red text
        lines.add(".withdraw { color: red; }");
        // End of the CSS style block
        lines.add("</style>");
    }

    // Adds a short summary as a bullet list: number of accounts and total money in the bank
    private static void addSummary(ArrayList<String> lines, Collection<Account> accounts) {
        int savingsCount = 0;
        int currentCount = 0;
        double totalMoney = 0;
        for (Account account : accounts) {
            if (account.getAccountType().equals("Savings")) {
                savingsCount++;
            } else {
                currentCount++;
            }
            totalMoney = totalMoney + account.getBalance();
        }

        // A horizontal line to separate this section from the heading above
        lines.add("<hr>");
        // Smaller heading for the summary section
        lines.add("<h2>Summary</h2>");
        // Start of a bullet list (ul = unordered list)
        lines.add("<ul>");
        // One bullet point (li = list item); <b> makes the number bold
        lines.add("<li>Total accounts: <b>" + accounts.size() + "</b></li>");
        // Bullet point with the number of savings accounts
        lines.add("<li>Savings accounts: <b>" + savingsCount + "</b></li>");
        // Bullet point with the number of current accounts
        lines.add("<li>Current accounts: <b>" + currentCount + "</b></li>");
        // Bullet point with the total money held in all accounts
        lines.add("<li>Total money in the bank: <b>Rs. " + String.format("%.2f", totalMoney) + "</b></li>");
        // End of the bullet list
        lines.add("</ul>");
    }

    // Adds the table that lists all accounts
    private static void addAccountsTable(ArrayList<String> lines, Collection<Account> accounts) {
        // A horizontal line to separate this section from the one above
        lines.add("<hr>");
        // Smaller heading above the table
        lines.add("<h2>All Accounts</h2>");
        // Start of the table
        lines.add("<table>");
        // Header row: tr = table row, th = header cell
        lines.add("<tr><th>Account No</th><th>Holder Name</th><th>Type</th><th>Balance (Rs.)</th></tr>");

        for (Account account : accounts) {
            // One row for this account: td = normal data cell
            lines.add("<tr><td>" + account.getAccountNo()
                    + "</td><td>" + escape(account.getHolderName())
                    + "</td><td>" + account.getAccountType()
                    + "</td><td>" + String.format("%.2f", account.getBalance())
                    + "</td></tr>");
        }

        // End of the table
        lines.add("</table>");
    }

    // Adds the statement (transaction list) of one account
    private static void addStatementTable(ArrayList<String> lines, Account account) {
        // A horizontal line to separate this section from the one above
        lines.add("<hr>");
        if (account == null) {
            // A paragraph saying no statement was chosen
            lines.add("<p>No account selected for the statement.</p>");
            return;
        }

        // Heading with the account number and holder name
        lines.add("<h2>Statement - Account " + account.getAccountNo()
                + " (" + escape(account.getHolderName()) + ")</h2>");
        // A paragraph showing the current balance
        lines.add("<p>Current balance: Rs. " + String.format("%.2f", account.getBalance()) + "</p>");
        // Start of the table
        lines.add("<table>");
        // Header row of the statement table
        lines.add("<tr><th>Date</th><th>Type</th><th>Description</th><th>Amount (Rs.)</th></tr>");

        ArrayList<Transaction> history = account.getHistory();
        if (history.isEmpty()) {
            // One row whose single cell spans all 4 columns
            lines.add("<tr><td colspan=\"4\">No transactions yet</td></tr>");
        }

        for (Transaction transaction : history) {
            // Deposits use the green "deposit" style, withdrawals use the red "withdraw" style
            String rowClass = "withdraw";
            if (transaction.getType().equals("DEPOSIT")) {
                rowClass = "deposit";
            }
            // One row for this transaction, coloured by its class
            lines.add("<tr class=\"" + rowClass + "\"><td>" + transaction.getFormattedDate()
                    + "</td><td>" + transaction.getType()
                    + "</td><td>" + transaction.getNote()
                    + "</td><td>" + String.format("%.2f", transaction.getAmount())
                    + "</td></tr>");
        }

        // End of the table
        lines.add("</table>");
    }

    // Adds the closing tags of the page
    private static void addPageEnd(ArrayList<String> lines) {
        // A horizontal line above the footer
        lines.add("<hr>");
        // Footer text in italics (<i>) at the bottom of the page
        lines.add("<p><i>Report created by the Bank Management System (Java console project).</i></p>");
        // End of the visible part of the page
        lines.add("</body>");
        // End of the whole page
        lines.add("</html>");
    }

    // Replaces special HTML characters so a name like "A & B" is shown correctly
    private static String escape(String text) {
        String safeText = text.replace("&", "&amp;");
        safeText = safeText.replace("<", "&lt;");
        safeText = safeText.replace(">", "&gt;");
        return safeText;
    }
}
