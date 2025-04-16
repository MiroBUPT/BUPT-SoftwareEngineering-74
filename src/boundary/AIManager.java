package com.example.personalfinance;

public interface AIManager {
    static AIManager getinstance() {
        return AIManagerImpl.getInstance();
    }

    String generateAdvice();
    TransactionType predictType(Transaction transaction);
    Budget predictBudget();
}