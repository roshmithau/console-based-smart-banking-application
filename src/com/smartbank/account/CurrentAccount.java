package com.smartbank.account;

public class CurrentAccount extends Account {
    public CurrentAccount(String accountNumber, String holderName, double balance) {
        super(accountNumber, holderName, balance);
    }

    @Override
    public String getType() {
        return "Current";
    }
}
