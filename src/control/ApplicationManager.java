package control;

import java.util.ArrayList;
import java.util.List;

public class ApplicationManager {
    private static ApplicationManager instance = new ApplicationManager();

    public static ApplicationManager getInstance() {
        return instance;
    }

    private List<Manager> managerList = new ArrayList<>();
    
    public void startApp() {
        
    }
    
    public void registerManager(Manager manager) {
        managerList.add(manager);
    }
}
