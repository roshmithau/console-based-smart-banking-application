package com.smartbank.account;

public class SavingsAccount extends Account {
    private double interestRate = 0.02; // simple placeholder

    public SavingsAccount(String accountNumber, String holderName, double balance) {
        super(accountNumber, holderName, balance);
    }

    @Override
    public String getType() {
        return "Savings";
    }

    // Simple method to apply interest (not used by menu, but demonstrates extension)
    public void applyInterest() {
        double interest = getBalance() * interestRate;
        deposit(interest);
    }
}
