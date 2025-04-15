package control;

public class SavingManager {
    private static SavingManager instance = new SavingManager();

    public static SavingManager getInstance() {
        return instance;
    }
}
