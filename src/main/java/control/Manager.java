package control;

public abstract class Manager {
    public Manager() {
        var applicationManager = control.ApplicationManager.getInstance();
        applicationManager.registerManager(this);
        Init();
    }

    public abstract void Init();
}
