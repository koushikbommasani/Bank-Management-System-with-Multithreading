package service;

// Unit III: multithreading demo.
// Four threads deposit and withdraw on the SAME shared account at the same time.
// Part 1 runs WITHOUT synchronization (the final balance comes out wrong).
// Part 2 runs WITH synchronization (the final balance is always correct).
// The only difference between the two parts is the "synchronized" keyword.
public class ThreadDemoService {

    // Unit I: static final constants used by the demo
    private static final double START_BALANCE = 10000;
    private static final double DEPOSIT_AMOUNT = 100;
    private static final double WITHDRAW_AMOUNT = 50;
    private static final int TIMES = 50; // how many times each thread repeats its work

    // Runs both parts of the demo and prints a summary
    public void runDemo() {
        // 2 depositors and 2 withdrawers, each repeating TIMES times
        double expected = START_BALANCE
                + (2 * TIMES * DEPOSIT_AMOUNT)
                - (2 * TIMES * WITHDRAW_AMOUNT);

        System.out.println("\nStarting balance : Rs. " + String.format("%.2f", START_BALANCE));
        System.out.println("2 threads each deposit Rs. " + DEPOSIT_AMOUNT + ", " + TIMES + " times");
        System.out.println("2 threads each withdraw Rs. " + WITHDRAW_AMOUNT + ", " + TIMES + " times");
        System.out.println("Expected final balance: Rs. " + String.format("%.2f", expected));

        System.out.println("\n--- PART 1: WITHOUT synchronization ---");
        double unsafeResult = runThreads(false);
        System.out.println("Final balance: Rs. " + String.format("%.2f", unsafeResult));

        System.out.println("\n--- PART 2: WITH synchronization ---");
        double safeResult = runThreads(true);
        System.out.println("Final balance: Rs. " + String.format("%.2f", safeResult));

        System.out.println("\n--- RESULT ---");
        System.out.println("Expected          : Rs. " + String.format("%.2f", expected));
        System.out.println("Without sync      : Rs. " + String.format("%.2f", unsafeResult)
                + getVerdict(unsafeResult, expected));
        System.out.println("With sync         : Rs. " + String.format("%.2f", safeResult)
                + getVerdict(safeResult, expected));
        System.out.println("Without synchronized, two threads can read the same old balance and");
        System.out.println("one thread's update overwrites the other's. This is a race condition.");
    }

    // Returns a short message saying if the result matches the expected balance
    private String getVerdict(double result, double expected) {
        if (result == expected) {
            return "  (correct)";
        } else {
            return "  (WRONG - some updates were lost)";
        }
    }

    // Creates the four threads, starts them, waits for them and returns the final balance
    private double runThreads(boolean useSync) {
        SharedAccount account = new SharedAccount(START_BALANCE);

        // Unit III: Runnable - the task is given to a Thread object
        Thread depositor1 = new Thread(new DepositTask(account, useSync), "Depositor-1");
        Thread depositor2 = new Thread(new DepositTask(account, useSync), "Depositor-2");

        // Unit III: a class that extends Thread directly
        WithdrawThread withdrawer1 = new WithdrawThread("Withdrawer-1", account, useSync);
        WithdrawThread withdrawer2 = new WithdrawThread("Withdrawer-2", account, useSync);

        // Unit III: thread priorities (1 = MIN, 5 = NORM, 10 = MAX).
        // Priority is only a hint to the scheduler; it does not fix race conditions.
        depositor1.setPriority(Thread.MAX_PRIORITY);
        withdrawer1.setPriority(Thread.MIN_PRIORITY);
        System.out.println("Priorities -> " + depositor1.getName() + ": " + depositor1.getPriority()
                + ", " + depositor2.getName() + ": " + depositor2.getPriority()
                + ", " + withdrawer1.getName() + ": " + withdrawer1.getPriority()
                + ", " + withdrawer2.getName() + ": " + withdrawer2.getPriority());

        // Unit III: thread life cycle - a thread is NEW before start() is called
        System.out.println("State of Depositor-1 before start(): " + depositor1.getState());

        depositor1.start();
        depositor2.start();
        withdrawer1.start();
        withdrawer2.start();

        // After start() the thread is RUNNABLE (or TIMED_WAITING while it sleeps)
        System.out.println("State of Depositor-1 after start() : " + depositor1.getState());

        // join() makes the main thread wait until each thread has finished
        try {
            depositor1.join();
            depositor2.join();
            withdrawer1.join();
            withdrawer2.join();
        } catch (InterruptedException e) {
            System.out.println("Main thread was interrupted: " + e.getMessage());
        }

        // When run() has finished, the thread is TERMINATED
        System.out.println("State of Depositor-1 after join()  : " + depositor1.getState());

        return account.getBalance();
    }

    // Unit II: inner class - a very small account used only for this demo.
    // It has an unsafe and a safe version of deposit and withdraw. The code inside is
    // the same; only the "synchronized" keyword is different.
    private class SharedAccount {
        private double balance;

        // Constructor of the inner class
        SharedAccount(double startBalance) {
            balance = startBalance;
        }

        // NOT synchronized - several threads can be inside this method at the same time
        public void unsafeDeposit(double amount) {
            double current = balance;       // step 1: read the balance
            pause();                        // small delay so the problem is easy to see
            balance = current + amount;     // step 2: write the new balance
        }

        // NOT synchronized
        public void unsafeWithdraw(double amount) {
            double current = balance;
            pause();
            balance = current - amount;
        }

        // Unit III: synchronized - only one thread at a time can be inside this method
        public synchronized void safeDeposit(double amount) {
            double current = balance;
            pause();
            balance = current + amount;
        }

        // Unit III: synchronized
        public synchronized void safeWithdraw(double amount) {
            double current = balance;
            pause();
            balance = current - amount;
        }

        // Unit III: synchronized read of the balance
        public synchronized double getBalance() {
            return balance;
        }

        // Puts the current thread to sleep for 1 millisecond (thread goes to TIMED_WAITING)
        private void pause() {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + " was interrupted");
            }
        }
    }

    // Unit III: inner class that implements Runnable - the job of a depositor thread
    private class DepositTask implements Runnable {
        private SharedAccount account;
        private boolean useSync;

        // Constructor of the task
        DepositTask(SharedAccount account, boolean useSync) {
            this.account = account;
            this.useSync = useSync;
        }

        // run() contains the work the thread does after start() is called
        @Override
        public void run() {
            for (int i = 0; i < TIMES; i++) {
                if (useSync) {
                    account.safeDeposit(DEPOSIT_AMOUNT);
                } else {
                    account.unsafeDeposit(DEPOSIT_AMOUNT);
                }
            }
        }
    }

    // Unit III: inner class that extends Thread - the withdrawer thread
    private class WithdrawThread extends Thread {
        private SharedAccount account;
        private boolean useSync;

        // Unit II: super(name) calls the Thread constructor to set the thread's name
        WithdrawThread(String name, SharedAccount account, boolean useSync) {
            super(name);
            this.account = account;
            this.useSync = useSync;
        }

        // Unit II: overrides run() of the Thread class
        @Override
        public void run() {
            for (int i = 0; i < TIMES; i++) {
                if (useSync) {
                    account.safeWithdraw(WITHDRAW_AMOUNT);
                } else {
                    account.unsafeWithdraw(WITHDRAW_AMOUNT);
                }
            }
        }
    }
}
