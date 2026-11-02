package util;

import exception.InvalidAmountException;
import java.util.StringTokenizer;

// Unit I: StringTokenizer - splits an input line like "1001,500" into separate parts.
// Unit III: throws a custom exception when the input is not valid.
public class InputParser {

    // Unit II: static nested (inner) class - holds the two values read from one input line
    public static class ParsedInput {
        private String accountNo;
        private double amount;

        // Constructor of the inner class
        public ParsedInput(String accountNo, double amount) {
            this.accountNo = accountNo;
            this.amount = amount;
        }

        // Returns the account number part
        public String getAccountNo() {
            return accountNo;
        }

        // Returns the amount part
        public double getAmount() {
            return amount;
        }
    }

    // Splits "accountNo,amount" (for example "1001,500") into an account number and an amount
    public static ParsedInput parseAccountAndAmount(String line) throws InvalidAmountException {
        // The comma "," is the separator between the tokens
        StringTokenizer tokenizer = new StringTokenizer(line, ",");

        if (tokenizer.countTokens() != 2) {
            throw new InvalidAmountException("Please type it as accountNo,amount (example: 1001,500)");
        }

        String accountNo = tokenizer.nextToken().trim();
        String amountText = tokenizer.nextToken().trim();
        double amount = parseAmount(amountText);
        return new ParsedInput(accountNo, amount);
    }

    // Converts text such as "500" or "250.75" into a double.
    // Unit III: catches the built-in NumberFormatException and throws our own exception instead.
    public static double parseAmount(String text) throws InvalidAmountException {
        try {
            return Double.parseDouble(text.trim());
        } catch (NumberFormatException e) {
            throw new InvalidAmountException("'" + text + "' is not a valid amount");
        }
    }
}
