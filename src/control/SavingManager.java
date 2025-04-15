package control;

public class SavingManager {
    private static SavingManager instance;

    public static SavingManager getInstance() {
        if (instance == null)
            instance = new SavingManager();
        return instance;
    }
}
