package control;

public class SavingManager extends Manager {
    private static SavingManager instance;

    public static SavingManager getInstance() {
        if (instance == null)
            instance = new SavingManager();
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
        System.out.println("SavingManager initialized.");
    }

    public void saveData() {
        
    }

    public void loadData() {
        budgetManager.loadData(null);
        transactionManager.loadData(null);
        userManager.loadData(null);
    }
}
