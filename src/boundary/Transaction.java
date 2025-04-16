package com.example.personalfinance;

public class Transaction {
    private String transactionId;
    private String date;
    private String amount;
    private String description;
    private TransactionType type;
    private User owner;

    public Transaction(String transactionId, String date, String amount, String description, TransactionType type, User owner) {
        this.transactionId = transactionId;
        this.date = date;
        this.amount = amount;
        this.description = description;
        this.type = type;
        this.owner = owner;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getDate() {
        return date;
    }

    public String getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public TransactionType getType() {
        return type;
    }

    public User getOwner() {
        return owner;
    }
}