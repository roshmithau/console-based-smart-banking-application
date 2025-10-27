# SmartBank - Console Banking Application (Java)

Simple console-based banking app demonstrating OOP concepts, exception handling, collections and file I/O.

Files
- `src/com/smartbank/account/Account.java` - abstract base account
- `src/com/smartbank/account/SavingsAccount.java` - savings account
- `src/com/smartbank/account/CurrentAccount.java` - current account
- `src/com/smartbank/Bank.java` - manages accounts and persistence
- `src/com/smartbank/Main.java` - console UI
- `src/com/smartbank/InsufficientFundsException.java` - custom exception

How to compile (Windows cmd.exe)

javac -d out src/com/smartbank/*.java src/com/smartbank/account/*.java
java -cp out com.smartbank.Main

Data persistence
Accounts are saved to `accounts.txt` in the working directory in CSV format.
