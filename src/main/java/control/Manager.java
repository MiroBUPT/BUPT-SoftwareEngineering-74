package control;

public abstract class Manager {
    public Manager() {
        var applicationManager = ApplicationManager.getInstance();
        applicationManager.registerManager(this);
        Init();
    }

    public abstract void Init();
}
