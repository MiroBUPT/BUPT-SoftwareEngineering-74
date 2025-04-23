package control;

import entity.Budget;
import entity.TransactionType;
import java.util.ArrayList;
import java.util.List;

public class BudgetManager extends Manager {
    private static BudgetManager instance;
    private List<Budget> budgetList = new ArrayList<>();

    public static BudgetManager getInstance() {
        if (instance == null) {
            instance = new BudgetManager();
        }
        return instance;
    }

    @Override
    public void Init() {
        System.out.println("BudgetManager initialized.");
    }

    // 添加预算
    public void addBudget(Budget budget) {
        budgetList.add(budget);
    }

    // 编辑预算
    public void editBudget(Budget budget, String budgetId) {
        for (int i = 0; i < budgetList.size(); i++) {
            if (budgetList.get(i).budgetId.equals(budgetId)) {
                budgetList.set(i, budget);
                return;
            }
        }
    }

    // 根据所有者查询预算记录
    public List<Budget> queryByOwner(String owner) {
        List<Budget> result = new ArrayList<>();
        for (Budget budget : budgetList) {
            if (budget.owner.name.equals(owner)) {
                result.add(budget);
            }
        }
        return result;
    }

    // 根据预算类型查询预算记录
    public List<Budget> queryByType(TransactionType type) {
        List<Budget> result = new ArrayList<>();
        for (Budget budget : budgetList) {
            if (budget.type == type) {
                result.add(budget);
            }
        }
        return result;
    }

    // 根据时间查询预算记录
    public List<Budget> queryByTime(String date) {
        List<Budget> result = new ArrayList<>();
        for (Budget budget : budgetList) {
            if (budget.date.equals(date)) {
                result.add(budget);
            }
        }
        return result;
    }

    // 加载预算数据
    public void loadData(List<Budget> budgets) {
        budgetList.clear();
        if (budgets != null) {
            budgetList.addAll(budgets);
        }
    }

    // 获取所有预算记录
    public List<Budget> getBudgetList() {
        return budgetList;
    }

    // 获取指定用户的预算记录
    public List<Budget> getBudgetsByUserName(String userName) {
        List<Budget> userBudgets = new ArrayList<>();
        for (Budget budget : budgetList) {
            if (budget.owner.name.equals(userName)) {
                userBudgets.add(budget);
            }
        }
        return userBudgets;
    }
}







// public class BudgetManager extends control.Manager {
//     private static BudgetManager instance; // Singleton instance
//     private List<Budget> budgetList = new ArrayList<>(); // List of budgets

//     /**
//      * Get the singleton instance of BudgetManager.
//      * 
//      * @return The singleton instance of BudgetManager
//      */
//     public static BudgetManager getInstance() {
//         if (instance == null)
//             instance = new BudgetManager();
//         return instance;
//     }

//     /**
//      * Initialize the BudgetManager instance.
//      */
//     @Override
//     public void Init() {
//         System.out.println("BudgetManager initialized.");
//     }

//     public List<Budget> getBudgetList() {
//         return budgetList;
//     }

//     /**
//      * Query budgets by the budget's owner.
//      * 
//      * @param owner The owner of the budget
//      * @return A list of budgets owned by the specified owner
//      */
//     public List<Budget> queryByOwner(String owner) {
//         List<Budget> result = new ArrayList<>();
//         for (Budget budget : budgetList) {
//             if (budget.owner.name.equals(owner)) {
//                 result.add(budget);
//             }
//         }
//         return budgetList;
//     }

//     /**
//      * Query budgets by the budget's type.
//      * 
//      * @param type The type of the budget
//      * @return A list of budgets of the specified type
//      */
//     public List<Budget> queryByType(TransactionType type) {
//         List<Budget> result = new ArrayList<>();
//         for (Budget budget : budgetList) {
//             if (budget.type == type) {
//                 result.add(budget);
//             }
//         }
//         return budgetList;
//     }

//     /**
//      * Query budgets by the budget's time.
//      * 
//      * @param date The date of the budget
//      * @return A list of budgets for the specified date
//      */
//     public List<Budget> queryByTime(String date) {
//         List<Budget> result = new ArrayList<>();
//         for (Budget budget : budgetList) {
//             if (budget.date.equals(date)) {
//                 result.add(budget);
//             }
//         }
//         return budgetList;
//     }

//     /**
//      * Add a new budget to the list.
//      * 
//      * @param budget The budget to be added
//      */
//     public void addBudget(Budget budget) {
//         budgetList.add(budget);
//     }

//     /**
//      * Edit a budget by its ID.
//      * 
//      * @param budget   The new budget to be set
//      * @param budgetId The ID of the budget to be edited
//      */
//     public void editBudget(Budget budget, String budgetId) {
//         for (int i = 0; i < budgetList.size(); i++) {
//             if (budgetList.get(i).budgetId.equals(budgetId)) {
//                 budgetList.set(i, budget);
//                 return;
//             }
//         }
//     }

//     /**
//      * Clear all the budgets and load all the budgets from the given list.
//      * 
//      * @param budgets The list of budgets to be loaded
//      */
//     public void loadData(List<Budget> budgets) {
//         budgetList.clear();
//         budgetList.addAll(budgets);
//     }

// }
