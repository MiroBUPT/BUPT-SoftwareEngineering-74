package control;

public class UserManager {
    private static UserManager instance = new UserManager();
    
    public static UserManager getInstance() {
        return instance;
    }
}
