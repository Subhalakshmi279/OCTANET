import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

class Transaction {
    private String type;
    private double amount;

    public Transaction(String type, double amount) {
        this.type = type;
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return type + " of $" + amount;
    }
}

class BankAccount {
    private double balance;
    private int pin;
    private ArrayList<Transaction> transactionHistory;

    public BankAccount(double initialBalance, int pin) {
        this.balance = initialBalance;
        this.pin = pin;
        this.transactionHistory = new ArrayList<>();
    }

    public double getBalance() {
        return balance;
    }

    public boolean verifyPin(int enteredPin) {
        return enteredPin == pin;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            Transaction transaction = new Transaction("Deposit", amount);
            transactionHistory.add(transaction);
            JOptionPane.showMessageDialog(null, "Deposit successful. New balance: $" + balance);
        } else {
            JOptionPane.showMessageDialog(null, "Invalid deposit amount.");
        }
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            Transaction transaction = new Transaction("Withdrawal", amount);
            transactionHistory.add(transaction);
            JOptionPane.showMessageDialog(null, "Withdrawal successful. New balance: $" + balance);
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Insufficient balance or invalid withdrawal amount.");
            return false;
        }
    }

    public void displayTransactionHistory() {
        StringBuilder history = new StringBuilder("Transaction History:\n");
        for (Transaction transaction : transactionHistory) {
            history.append(transaction).append("\n");
        }
        JOptionPane.showMessageDialog(null, history.toString());
    }
}

class ATM extends JFrame {
    private BankAccount userAccount;

    private JTextField pinField;
    private JButton enterButton;

    private JButton checkBalanceButton;
    private JButton depositButton;
    private JButton withdrawButton;
    private JButton historyButton;
    private JButton exitButton;

    public ATM(BankAccount account) {
        userAccount = account;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("ATM Interface");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1));

        // PIN entry panel
        JPanel pinPanel = new JPanel();
        pinField = new JTextField(4);
        enterButton = new JButton("Enter");
        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                authenticatePin();
            }
        });
        pinPanel.add(new JLabel("Enter your PIN:"));
        pinPanel.add(pinField);
        pinPanel.add(enterButton);

        // Transaction buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 4));

        checkBalanceButton = new JButton("Check Balance");
        checkBalanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkBalance();
            }
        });

        depositButton = new JButton("Deposit Funds");
        depositButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                depositFunds();
            }
        });

        withdrawButton = new JButton("Withdraw Funds");
        withdrawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                withdrawFunds();
            }
        });

        historyButton = new JButton("View Transaction History");
        historyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewTransactionHistory();
            }
        });

        buttonPanel.add(checkBalanceButton);
        buttonPanel.add(depositButton);
        buttonPanel.add(withdrawButton);
        buttonPanel.add(historyButton);

        // Exit button panel
        JPanel exitPanel = new JPanel();
        exitButton = new JButton("Exit");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exitATM();
            }
        });
        exitPanel.add(exitButton);

        panel.add(pinPanel);
        panel.add(buttonPanel);
        panel.add(exitPanel);

        add(panel);
    }

    private void authenticatePin() {
        try {
            int pin = Integer.parseInt(pinField.getText());
            if (userAccount.verifyPin(pin)) {
                enableTransactionButtons();
            } else {
                JOptionPane.showMessageDialog(null, "Incorrect PIN. Please try again.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid PIN format. Please enter numeric digits.");
        }
    }

    private void enableTransactionButtons() {
        checkBalanceButton.setEnabled(true);
        depositButton.setEnabled(true);
        withdrawButton.setEnabled(true);
        historyButton.setEnabled(true);
    }

    private void checkBalance() {
        double balance = userAccount.getBalance();
        JOptionPane.showMessageDialog(null, "Your account balance: $" + balance);
    }

    private void depositFunds() {
        try {
            double amount = Double.parseDouble(JOptionPane.showInputDialog("Enter the deposit amount:"));
            userAccount.deposit(amount);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid amount format. Please enter a numeric value.");
        }
    }

    private void withdrawFunds() {
        try {
            double amount = Double.parseDouble(JOptionPane.showInputDialog("Enter the withdrawal amount:"));
            userAccount.withdraw(amount);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid amount format. Please enter a numeric value.");
        }
    }

    private void viewTransactionHistory() {
        userAccount.displayTransactionHistory();
    }

    private void exitATM() {
        JOptionPane.showMessageDialog(null, "Thank you for using the ATM!");
        System.exit(0);
    }
}

public class ATM_Interface {
    public static void main(String[] args) {
        // Set a PIN for the bank account
        int userPin = 2728;
        // Set the initial balance to $2000
        double initialBalance = 2000;

        BankAccount userAccount = new BankAccount(initialBalance, userPin);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ATM(userAccount).setVisible(true);
            }
        });
    }
}
