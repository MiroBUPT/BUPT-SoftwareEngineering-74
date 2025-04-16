package com.example.personalfinance;

import java.util.ArrayList;
import java.util.List;

public class BudgetManagerImpl implements BudgetManager {
    private static final BudgetManager INSTANCE = new BudgetManagerImpl();
    private List<Budget> budgetList;

    private BudgetManagerImpl() {
        budgetList = new ArrayList<>();
    }

    public static BudgetManager getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Budget> queryByOwner(String owner) {
        List<Budget> result = new ArrayList<>();
        for (Budget budget : budgetList) {
            if (budget.getOwner().getUserId().equals(owner)) {
                result.add(budget);
            }
        }
        return result;
    }

    @Override
    public List<Budget> queryByType(TransactionType type) {
        List<Budget> result = new ArrayList<>();
        for (Budget budget : budgetList) {
            if (budget.getType() == type) {
                result.add(budget);
            }
        }
        return result;
    }

    @Override
    public List<Budget> queryByTime(String time) {
        List<Budget> result = new ArrayList<>();
        for (Budget budget : budgetList) {
            if (budget.getDate().equals(time)) {
                result.add(budget);
            }
        }
        return result;
    }

    @Override
    public void addBudget(Budget budget) {
        budgetList.add(budget);
    }

    @Override
    public void editBudget(Budget budget, String newData) {
        // 这里简单示例修改金额，可按需扩展
        for (Budget b : budgetList) {
            if (b.getBudgetId().equals(budget.getBudgetId())) {
                b = new Budget(b.getBudgetId(), newData, b.getType(), b.getOwner(), b.getDate());
                break;
            }
        }
    }

    @Override
    public void initData() {
        // 初始化预算数据
    }
}
