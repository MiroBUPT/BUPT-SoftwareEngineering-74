package control;

public class TransactionManager {
    private static TransactionManager instance = new TransactionManager();

    public static TransactionManager getInstance() {
        return instance;
    }
}
