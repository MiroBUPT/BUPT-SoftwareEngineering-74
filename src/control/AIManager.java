package control;

import entity.Budget;
import entity.Transaction;
import entity.TransactionType;

public class AIManager extends Manager {
    private static AIManager instance;

    public static AIManager getInstance() {
        if (instance == null)
            instance = new AIManager();
        return instance;
    }

    private BudgetManager budgetManager;
    private TransactionManager transactionManager;
    private UserManager userManager;

    @Override
    public void Init() {
        budgetManager = BudgetManager.getInstance();
        transactionManager = TransactionManager.getInstance();
        userManager = UserManager.getInstance();
        System.out.println("AIManager initialized.");
    }

    public String generateAdvice() {
        return "";
    }

    public TransactionType predictType(Transaction transaction) {
        return TransactionType.food;
    }
    
    public Budget predictBudget() {
        return new Budget();
    }
}
