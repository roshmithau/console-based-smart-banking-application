package com.smartbank.account;

import com.smartbank.InsufficientFundsException;

public abstract class Account {
    private final String accountNumber;
    private String holderName;
    protected double balance;

    public Account(String accountNumber, String holderName, double balance) {
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.balance = balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public double getBalance() {
        return balance;
    }

    // Method overloading: deposit int
    public void deposit(int amount) {
        deposit((double) amount);
    }

    // Method overloading: deposit double
    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        balance += amount;
    }

    public void withdraw(double amount) throws InsufficientFundsException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdraw amount must be positive");
        }
        if (amount > balance) {
            throw new InsufficientFundsException("Insufficient funds: attempted " + amount + " but balance is " + balance);
        }
        balance -= amount;
    }

    public String toCSV() {
        return String.format("%s,%s,%s,%.2f", getType(), accountNumber, holderName.replace(',', ' '), balance);
    }

    public String toString() {
        return String.format("[%s] Account No: %s\nHolder: %s\nBalance: %.2f", getType(), accountNumber, holderName, balance);
    }

    public abstract String getType();

    // Optional: finalize for demo-only logging (not recommended in production)
    @Override
    @SuppressWarnings("deprecation")
    protected void finalize() throws Throwable {
        try {
            System.out.println("Account object for " + accountNumber + " is being garbage collected");
        } finally {
            super.finalize();
        }
    }
}
