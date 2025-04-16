package com.example.personalfinance;

import java.util.List;

public interface BudgetManager {
    static BudgetManager getinstance() {
        return BudgetManagerImpl.getInstance();
    }

    List<Budget> queryByOwner(String owner);
    List<Budget> queryByType(TransactionType type);
    List<Budget> queryByTime(String time);
    void addBudget(Budget budget);
    void editBudget(Budget budget, String newData);
    void initData();
}
