package control;

import java.util.ArrayList;
import java.util.List;

import entity.Transaction;

/**
 * Central manager class that coordinates all other managers in the application.
 * Implements the Singleton pattern to ensure only one instance exists.
 */
public class ApplicationManager {
    /** Singleton instance of ApplicationManager */
    private static ApplicationManager instance = new ApplicationManager();

    /**
     * Gets the singleton instance of ApplicationManager.
     * @return The singleton instance
     */
    public static ApplicationManager getInstance() {
        return instance;
    }

    /** List of all registered managers in the application */
    private List<control.Manager> managerList = new ArrayList<>();

    /**
     * Initializes and starts all managers in the application.
     * Creates instances of all required managers and initializes the saving manager.
     */
    public void startApp() {
        AIManager.getInstance();
        control.BudgetManager.getInstance();
        control.SavingManager.getInstance();
        control.TransactionManager.getInstance();
        control.UserManager.getInstance();
        control.SavingManager.getInstance().Init();
    }

    /**
     * Registers a manager with the application.
     * @param manager The manager to register
     */
    public void registerManager(control.Manager manager) {
        managerList.add(manager);
    }
}
