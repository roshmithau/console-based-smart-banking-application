package com.smartbank;

import com.smartbank.account.*;
import java.util.*;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        String dataPath = "accounts.txt";
        Bank bank = new Bank(dataPath);
        bank.load();

        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    createAccount(bank);
                    break;
                case "2":
                    displayAccount(bank);
                    break;
                case "3":
                    depositMoney(bank);
                    break;
                case "4":
                    withdrawMoney(bank);
                    break;
                case "5":
                    closeAccount(bank);
                    break;
                case "6":
                    running = false;
                    bank.save();
                    System.out.println("Exiting. Data saved.");
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n--- SmartBank Menu ---");
        System.out.println("1. Create Account");
        System.out.println("2. Display Account");
        System.out.println("3. Deposit Money");
        System.out.println("4. Withdraw Money");
        System.out.println("5. Close Account");
        System.out.println("6. Exit");
        System.out.print("Choose an option: ");
    }

    private static void createAccount(Bank bank) {
        System.out.print("Enter account type (Savings/Current): ");
        String type = scanner.nextLine().trim();
        if (!type.equalsIgnoreCase("Savings") && !type.equalsIgnoreCase("Current")) {
            System.out.println("Unknown type. Defaulting to Current.");
            type = "Current";
        }
        System.out.print("Enter holder name: ");
        String name = scanner.nextLine().trim();
        double initial = readDouble("Enter initial deposit: ");
        Account a = bank.createAccount(type, name, initial);
        System.out.println("Account created: " + a.getAccountNumber());
    }

    private static void displayAccount(Bank bank) {
        System.out.print("Enter account number: ");
        String acc = scanner.nextLine().trim();
        Account a = bank.findAccount(acc);
        if (a == null) {
            System.out.println("Account not found");
        } else {
            System.out.println(a);
        }
    }

    private static void depositMoney(Bank bank) {
        System.out.print("Enter account number: ");
        String acc = scanner.nextLine().trim();
        Account a = bank.findAccount(acc);
        if (a == null) {
            System.out.println("Account not found");
            return;
        }
        System.out.print("Deposit type (1=int, 2=double): ");
        String t = scanner.nextLine().trim();
        try {
            if (t.equals("1")) {
                int amt = (int) readDouble("Enter amount (int): ");
                a.deposit(amt);
            } else {
                double amt = readDouble("Enter amount (double): ");
                a.deposit(amt);
            }
            bank.save();
            System.out.println("Deposit successful. New balance: " + a.getBalance());
        } catch (IllegalArgumentException ex) {
            System.out.println("Invalid amount: " + ex.getMessage());
        }
    }

    private static void withdrawMoney(Bank bank) {
        System.out.print("Enter account number: ");
        String acc = scanner.nextLine().trim();
        Account a = bank.findAccount(acc);
        if (a == null) {
            System.out.println("Account not found");
            return;
        }
        double amt = readDouble("Enter amount to withdraw: ");
        try {
            a.withdraw(amt);
            bank.save();
            System.out.println("Withdrawal successful. New balance: " + a.getBalance());
        } catch (InsufficientFundsException ex) {
            System.out.println("Withdrawal failed: " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            System.out.println("Invalid amount: " + ex.getMessage());
        }
    }

    private static void closeAccount(Bank bank) {
        System.out.print("Enter account number to close: ");
        String acc = scanner.nextLine().trim();
        boolean ok = bank.closeAccount(acc);
        if (ok) System.out.println("Account closed."); else System.out.println("Account not found.");
    }

    private static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                return Double.parseDouble(line);
            } catch (NumberFormatException ex) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}
