# Bank Management System (Java, with Multithreading)

A console-based bank management system written in plain Java. It uses only the standard library: no frameworks, databases or external libraries. It saves accounts to a file using object serialization and creates a simple HTML report that can be opened in any web browser.

## Features

- Create **Savings** or **Current** accounts (account numbers start from 1001)
- Deposit, withdraw and transfer money
- View balance and account details (interest for savings, charges for current)
- View transaction history sorted by date, by amount, or newest first
- List all accounts sorted by account number
- **Multithreading demo**: runs the same deposits and withdrawals with and without `synchronized` to show a race condition
- Save and load all accounts using serialization (`accounts.dat`)
- Create `report.html` with an accounts table and an account statement

### Account rules

| Type    | Minimum balance | Withdrawal charge | Interest    |
|---------|-----------------|-------------------|-------------|
| Savings | Rs. 1000        | None              | 4% per year |
| Current | Rs. 0           | Rs. 5 each time   | None        |

## Folder structure

```
src/
├── Main.java                 menu loop (root package)
├── model/                    Account, SavingsAccount, CurrentAccount,
│                             Transaction, DepositTransaction, WithdrawTransaction
├── exception/                InsufficientFundsException, AccountNotFoundException,
│                             InvalidAmountException
├── service/                  BankService, ThreadDemoService
└── util/                     Repository<T>, FileStorage, InputParser, HtmlReportGenerator
```

## How to compile and run

You need JDK 8 or newer. Open a terminal in the project folder (the folder that contains `src`).

**Compile** (puts all `.class` files in the `out` folder):

```
javac -d out -sourcepath src src/Main.java
```

`-sourcepath src` tells javac where the other classes are, so it compiles every class that `Main` uses.

**Run:**

```
java -cp out Main
```

The program creates two files in the folder you run it from:
- `accounts.dat`: the saved accounts (created when you choose **0. Save and exit**)
- `report.html`: the report (created when you choose **9. Generate HTML report**); open it in a browser

## Example session

```
Enter your choice: 1
Account type (1 = Savings, 2 = Current): 1
Holder name: Ravi Kumar
Opening balance: 5000
Account created. Your account number is 1001

Enter your choice: 2
Enter accountNo,amount (example 1001,500): 1001,2500
Deposit successful.
```

Deposit, withdraw and transfer read the account number and amount on **one line separated by a comma**. The program splits that line using `StringTokenizer`.

## How to explain this project

1. **Start with the model (Unit II).** `Account` is an abstract class. `SavingsAccount` and `CurrentAccount` extend it, call the parent constructor with `super(...)` and override `getMinimumBalance()`, `getAccountType()`, `getWithdrawCharge()` and `getExtraInfo()`. When `withdraw()` calls `getMinimumBalance()`, Java runs the child class version. This is runtime polymorphism.
2. **Overloading (Unit I).** `Account` has two constructors and two versions of each of `deposit()` and `withdraw()`: one with a default description and one where you pass the description (used by transfer).
3. **Interface (Unit II).** `Transaction` is an interface. `DepositTransaction` and `WithdrawTransaction` implement it. Its `DATE_FORMAT` field is a constant (interface fields are automatically `public static final`).
4. **Exceptions (Unit III).** Three custom exceptions extend `Exception`. They are thrown in `Account` and `BankService` and all caught in one `try/catch` in `Main`. `FileStorage` shows `finally` closing the file.
5. **Generics (Unit III).** `Repository<T>` is a generic class that stores items in a `HashMap`. We use it as `Repository<Account>`. `printAll()` is a generic method.
6. **Multithreading (Unit III), the main demo.** Menu option 8 runs 2 depositor threads (`Runnable`) and 2 withdrawer threads (a class that `extends Thread`) on one shared account.
   - Without `synchronized`: each thread reads the balance, sleeps 1 ms, then writes. Two threads read the same old value and one update is lost, so the final balance is wrong.
   - With `synchronized`: only one thread can be inside the method at a time, so the balance is always correct.
   - It also shows thread priorities (`MAX_PRIORITY` and `MIN_PRIORITY`) and the life cycle states NEW → RUNNABLE → TERMINATED using `getState()` and `join()`.
   - The real `Account.deposit()` and `withdraw()` methods are `synchronized` for the same reason.
7. **Collections (Unit IV).** The history is an `ArrayList`. It is sorted with `Comparator` lambdas (by amount or date), then printed with an `Iterator` (forward) or a `ListIterator` (backward, newest first). The account list uses a `TreeSet`, which sorts using `Account.compareTo()` (`Comparable`).
8. **Files (Unit V).** `FileStorage` saves an `Account[]` with `ObjectOutputStream` and loads it with `ObjectInputStream`. Every model class implements `Serializable`. `HtmlReportGenerator` uses NIO `Path` and `Files.write()` to create `report.html`.

See `SYLLABUS_MAP.md` for the exact file where each syllabus topic is used.
