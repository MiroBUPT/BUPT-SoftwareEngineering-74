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

    /**
     * Initialize the BudgetManager instance.
     */
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

    /**
     * Add a new budget to the list.
     * @param budget The budget to be added
     */
    public void addBudget(Budget budget) {
        budgetList.add(budget);
    }
    
    /**
     * Edit a budget by its ID.
     * @param budget The new budget to be set
     * @param budgetId The ID of the budget to be edited
     */
    public void editBudget(Budget budget, String budgetId) {
        for (int i = 0; i < budgetList.size(); i++) {
            if (budgetList.get(i).budgetId.equals(budgetId)) {
                budgetList.set(i, budget);
                return;
            }
        }
    }

    /**
     * Clear all the budgets and load all the budgets from the given list.
     * @param budgets The list of budgets to be loaded
     */
    public void loadData(List<Budget> budgets) {
        budgetList.clear();
        budgetList.addAll(budgets);
    }
}
