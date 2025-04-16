package com.example.personalfinance;

public class Budget {
    private String budgetId;
    private String amount;
    private TransactionType type;
    private User owner;
    private String date;

    public Budget(String budgetId, String amount, TransactionType type, User owner, String date) {
        this.budgetId = budgetId;
        this.amount = amount;
        this.type = type;
        this.owner = owner;
        this.date = date;
    }

    public String getBudgetId() {
        return budgetId;
    }

    public String getAmount() {
        return amount;
    }

    public TransactionType getType() {
        return type;
    }

    public User getOwner() {
        return owner;
    }

    public String getDate() {
        return date;
    }
}