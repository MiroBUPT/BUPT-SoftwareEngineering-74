import control.ApplicationManager;

import javax.swing.SwingUtilities;

/**
 * Main application class that serves as the entry point for the financial management system.
 * Initializes the application and launches the login interface.
 */
public class Application {
    /**
     * Main method that starts the application.
     * Initializes the application manager and launches the login interface in the Event Dispatch Thread.
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        var appManager = control.ApplicationManager.getInstance();
        appManager.startApp();

        // 启动登录界面
        SwingUtilities.invokeLater(() -> {
            boundary.LoginView.main(args);
        });
    }
}
