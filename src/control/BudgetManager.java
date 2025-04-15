package control;

public class BudgetManager {
    private static BudgetManager instance = new BudgetManager();

    public static BudgetManager getInstance() {
        return instance;
    }
}
