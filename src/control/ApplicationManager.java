package control;

public class ApplicationManager {
    private static ApplicationManager instance = new ApplicationManager();

    public static ApplicationManager getInstance(){
        return instance;
    }
}
