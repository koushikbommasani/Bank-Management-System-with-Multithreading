# Syllabus Map

Where each syllabus topic is used in the project. All paths are inside `src/`.

## Unit I: Java Basics

| Topic | File | Where |
|---|---|---|
| Classes and objects | all files | for example `new SavingsAccount(...)` in `BankService.createAccount()` |
| Constructors | `model/Account.java` | `Account(String, String)` |
| Constructor overloading | `model/Account.java`, `SavingsAccount.java`, `CurrentAccount.java` | two constructors each; `this(...)` in `Account` |
| Method overloading | `model/Account.java` | `deposit(double)` / `deposit(double, String)`, `withdraw(double)` / `withdraw(double, String)` |
| Access modifiers | `model/Account.java` | `private` fields, `public` methods; `private` helper methods in `Main` |
| static and final | `model/SavingsAccount.java`, `CurrentAccount.java`, `service/BankService.java` | `MINIMUM_BALANCE`, `INTEREST_RATE`, `WITHDRAW_CHARGE`, `FIRST_ACCOUNT_NUMBER` |
| Arrays | `util/FileStorage.java`, `service/BankService.java` | `Account[]` saved and loaded; filled in `saveAccounts()` |
| String | `Main.java`, `util/InputParser.java` | `trim()`, `equals()`, `String.format()` |
| StringTokenizer | `util/InputParser.java` | `parseAccountAndAmount()` splits `"1001,500"` |
| Scanner | `Main.java` | `readLine()` |
| Primitive types and operators | `model/Account.java` | `double balance`, arithmetic and comparisons in `withdraw()` |
| Flow control | `Main.java` | `while` menu loop, `switch`, `if/else`, `for` |

## Unit II: OOP

| Topic | File | Where |
|---|---|---|
| Inheritance | `model/SavingsAccount.java`, `CurrentAccount.java` | `extends Account` |
| super keyword | `model/SavingsAccount.java`, `CurrentAccount.java`, `exception/*.java`, `service/ThreadDemoService.java` | `super(accountNo, holderName, ...)`, `super(message)`, `super(name)` |
| Method overriding | `model/SavingsAccount.java`, `CurrentAccount.java` | `getMinimumBalance()`, `getAccountType()`, `getWithdrawCharge()`, `getExtraInfo()` |
| Abstract class | `model/Account.java` | `abstract class Account` with abstract methods |
| Interface | `model/Transaction.java` | implemented by `DepositTransaction`, `WithdrawTransaction` |
| Interface constant | `model/Transaction.java` | `DATE_FORMAT` |
| Inner classes | `service/ThreadDemoService.java`, `util/InputParser.java` | `SharedAccount`, `DepositTask`, `WithdrawThread`; static nested `ParsedInput` |
| Packages | whole project | `model`, `service`, `exception`, `util` |
| Polymorphism | `Main.java` | `account.getExtraInfo()` in `viewBalance()` |

## Unit III: Exceptions, Threads, Generics

| Topic | File | Where |
|---|---|---|
| try / catch | `Main.java` | around the `switch` in `main()` |
| throw / throws | `model/Account.java`, `service/BankService.java` | `throw new InvalidAmountException(...)`, `throws` on methods |
| finally | `util/FileStorage.java` | closes the stream in `saveAccounts()` and `loadAccounts()` |
| Custom exceptions | `exception/` | `InsufficientFundsException`, `AccountNotFoundException`, `InvalidAmountException` |
| Runnable | `service/ThreadDemoService.java` | `DepositTask implements Runnable` |
| Thread class | `service/ThreadDemoService.java` | `WithdrawThread extends Thread` |
| Thread life cycle | `service/ThreadDemoService.java` | `getState()` before `start()`, after `start()`, after `join()`; `Thread.sleep()` |
| Thread priorities | `service/ThreadDemoService.java` | `setPriority(Thread.MAX_PRIORITY)` / `MIN_PRIORITY` in `runThreads()` |
| synchronized | `service/ThreadDemoService.java`, `model/Account.java` | `safeDeposit()` / `safeWithdraw()` vs `unsafe...()`; `Account.deposit()` / `withdraw()` |
| Generic class | `util/Repository.java` | `Repository<T>`, used as `Repository<Account>` in `BankService` |
| Generic method | `util/Repository.java` | `<E> void printAll(Collection<E>)` |

## Unit IV: Collections

| Topic | File | Where |
|---|---|---|
| ArrayList | `model/Account.java`, `util/HtmlReportGenerator.java` | transaction history; list of HTML lines |
| HashMap | `util/Repository.java` | stores items by id |
| TreeSet | `service/BankService.java` | `getSortedAccounts()` |
| Iterator | `service/BankService.java` | `printWithIterator()` |
| ListIterator | `service/BankService.java` | `printWithListIteratorReversed()` (newest first) |
| Comparable | `model/Account.java` | `compareTo()` by account number |
| Comparator | `service/BankService.java` | `BY_AMOUNT`, `BY_DATE` |
| Lambda expressions | `service/BankService.java` | `(first, second) -> Double.compare(...)` |
| Functional interfaces | `service/BankService.java`, `service/ThreadDemoService.java` | `Comparator` (lambda), `Runnable` |

## Unit V: I/O and NIO

| Topic | File | Where |
|---|---|---|
| InputStream / OutputStream | `util/FileStorage.java` | `FileInputStream`, `FileOutputStream` |
| Serialization | `util/FileStorage.java` | `ObjectOutputStream.writeObject()` |
| Deserialization | `util/FileStorage.java` | `ObjectInputStream.readObject()` |
| Serializable | `model/Account.java`, `model/Transaction.java` | `implements Serializable`, `serialVersionUID` |
| NIO Path and Files | `util/HtmlReportGenerator.java`, `util/FileStorage.java` | `Paths.get()`, `Files.write()`, `Files.exists()` |
