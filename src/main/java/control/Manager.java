package control;

/**
 * Abstract base class for all manager classes in the application.
 * Provides common functionality for manager initialization and registration.
 */
public abstract class Manager {
    /**
     * Constructs a new Manager instance.
     * Registers this manager with the ApplicationManager and calls Init().
     */
    public Manager() {
        var applicationManager = control.ApplicationManager.getInstance();
        applicationManager.registerManager(this);
        Init();
    }

    /**
     * Abstract method to be implemented by subclasses for initialization.
     * This method is called after the manager is registered with ApplicationManager.
     */
    public abstract void Init();
}
