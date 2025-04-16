package com.example.personalfinance;

public class AIManagerImpl implements AIManager {
    private static final AIManager INSTANCE = new AIManagerImpl();
    private BudgetManager budgetManager;
    private TransactionManager transactionManager;
    private UserManager userManager;

    private AIManagerImpl() {
        budgetManager = BudgetManager.getinstance();
        transactionManager = TransactionManager.getinstance();
        userManager = UserManager.getinstance();
    }

    public static AIManager getInstance() {
        return INSTANCE;
    }

    @Override
    public String generateAdvice() {
        return "这是AI生成的建议";
    }

    @Override
    public TransactionType predictType(Transaction transaction) {
        return TransactionType.INCOME;
    }

    @Override
    public Budget predictBudget() {
        return null;
    }
}