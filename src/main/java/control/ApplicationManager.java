package control;

import java.util.ArrayList;
import java.util.List;

import entity.Transaction;

public class ApplicationManager {
    private static ApplicationManager instance = new ApplicationManager();

    public static ApplicationManager getInstance() {
        return instance;
    }

    private List<control.Manager> managerList = new ArrayList<>();

    public void startApp() {
        AIManager.getInstance();
        control.BudgetManager.getInstance();
        control.SavingManager.getInstance();
        control.TransactionManager.getInstance();
        control.UserManager.getInstance();
        control.SavingManager.getInstance().Init();
    }

    public void registerManager(control.Manager manager) {
        managerList.add(manager);
    }
}
