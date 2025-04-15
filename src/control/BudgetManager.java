package control;

import java.util.ArrayList;
import java.util.List;

import entity.Budget;
import entity.TransactionType;

public class BudgetManager extends Manager {
    private static BudgetManager instance;
    private List<Budget> budgetList = new ArrayList<>();

    public static BudgetManager getInstance() {
        if (instance == null)
            instance = new BudgetManager();
        return instance;
    }

    @Override
    public void Init() {
        System.out.println("BudgetManager initialized.");
    }

    public List<Budget> queryByOwner(String owner) {
        return budgetList;
    }

    public List<Budget> queryByType(TransactionType type) {
        return budgetList;
    }

    public List<Budget> queryByTime(String date) {
        return budgetList;
    }

    public void addBudget(Budget budget) {
        budgetList.add(budget);
    }
    
    public void editBudget(Budget budget, String budgetId) {

    }

    public void loadData(List<Budget> budgets) {
        budgetList.clear();
        budgetList.addAll(budgets);
    }
}
