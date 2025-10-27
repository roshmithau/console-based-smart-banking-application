package com.smartbank;

import com.smartbank.account.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Bank {
    private List<Account> accounts = new ArrayList<>();
    private final Path dataFile;

    public Bank(String dataFilePath) {
        this.dataFile = Paths.get(dataFilePath);
    }

    public void load() {
        accounts.clear();
        if (Files.notExists(dataFile)) {
            return;
        }

        try (BufferedReader r = Files.newBufferedReader(dataFile)) {
            String line;
            while ((line = r.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                // expected: type,accountNumber,holderName,balance
                String[] parts = line.split(",", 4);
                if (parts.length < 4) continue;
                String type = parts[0];
                String accNo = parts[1];
                String name = parts[2];
                double bal = 0.0;
                try {
                    bal = Double.parseDouble(parts[3]);
                } catch (NumberFormatException ex) {
                    // skip bad record
                    continue;
                }
                Account a;
                if ("Savings".equalsIgnoreCase(type)) {
                    a = new SavingsAccount(accNo, name, bal);
                } else {
                    a = new CurrentAccount(accNo, name, bal);
                }
                accounts.add(a);
            }
        } catch (IOException e) {
            System.err.println("Failed to load accounts: " + e.getMessage());
        }
    }

    public void save() {
        try (BufferedWriter w = Files.newBufferedWriter(dataFile)) {
            for (Account a : accounts) {
                w.write(a.toCSV());
                w.newLine();
            }
        } catch (IOException e) {
            System.err.println("Failed to save accounts: " + e.getMessage());
        }
    }

    public Account createAccount(String type, String holderName, double initialDeposit) {
        int nextId = 1;
        for (Account a : accounts) {
            try {
                String num = a.getAccountNumber().replaceAll("[^0-9]", "");
                if (!num.isEmpty()) {
                    int val = Integer.parseInt(num);
                    if (val >= nextId) nextId = val + 1;
                }
            } catch (NumberFormatException ex) {
                // ignore
            }
        }
        String accountNumber = String.format("AC%05d", nextId);
        Account a;
        if ("Savings".equalsIgnoreCase(type)) {
            a = new SavingsAccount(accountNumber, holderName, initialDeposit);
        } else {
            a = new CurrentAccount(accountNumber, holderName, initialDeposit);
        }
        accounts.add(a);
        save();
        return a;
    }

    public Account findAccount(String accountNumber) {
        for (Account a : accounts) {
            if (a.getAccountNumber().equalsIgnoreCase(accountNumber)) return a;
        }
        return null;
    }

    public boolean closeAccount(String accountNumber) {
        Iterator<Account> it = accounts.iterator();
        while (it.hasNext()) {
            Account a = it.next();
            if (a.getAccountNumber().equalsIgnoreCase(accountNumber)) {
                it.remove();
                // small cleanup/log
                System.out.println("Closing account " + accountNumber + ". Final balance: " + a.getBalance());
                save();
                return true;
            }
        }
        return false;
    }

    public List<Account> getAccounts() {
        return Collections.unmodifiableList(accounts);
    }
}
