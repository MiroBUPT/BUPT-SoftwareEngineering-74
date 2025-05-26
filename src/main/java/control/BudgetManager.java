package control;

import entity.Budget;
import entity.TransactionType;
import java.util.ArrayList;
import java.util.List;

/**
 * Manager class for handling budget-related operations.
 * Implements the Singleton pattern and manages budget data including creation, modification, and queries.
 */
public class BudgetManager extends control.Manager {
    /** Singleton instance of BudgetManager */
    private static BudgetManager instance;
    /** List of all budgets in the system */
    private List<Budget> budgetList = new ArrayList<>();

    /**
     * Get the singleton instance of BudgetManager.
     * @return The singleton instance of BudgetManager
     */
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

    /**
     * Gets the list of all budgets.
     * @return List of all budgets in the system
     */
    public List<Budget> getBudgetList() {
        return budgetList;
    }

    /**
     * Query budgets by the budget's owner.
     * @param owner The owner of the budget
     * @return A list of budgets owned by the specified owner
     */
    public List<Budget> queryByOwner(String owner) {
        List<Budget> result = new ArrayList<>();
        for (Budget budget : budgetList) {
            if (budget.owner.name.equals(owner)) {
                result.add(budget);
            }
        }
        return budgetList;
    }

    /**
     * Query budgets by the budget's type.
     * @param type The type of the budget
     * @return A list of budgets of the specified type
     */
    public List<Budget> queryByType(TransactionType type) {
        List<Budget> result = new ArrayList<>();
        for (Budget budget : budgetList) {
            if (budget.type == type) {
                result.add(budget);
            }
        }
        return budgetList;
    }

    /**
     * Query budgets by the budget's time.
     * @param date The date of the budget
     * @return A list of budgets for the specified date
     */
    public List<Budget> queryByTime(String date) {
        List<Budget> result = new ArrayList<>();
        for (Budget budget : budgetList) {
            if (budget.date.equals(date)) {
                result.add(budget);
            }
        }
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
