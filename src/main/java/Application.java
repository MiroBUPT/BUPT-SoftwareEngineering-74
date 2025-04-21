import control.ApplicationManager;
import javax.swing.SwingUtilities;

public class Application {
    public static void main(String[] args) {
        var appManager = ApplicationManager.getInstance();
        appManager.startApp();

        // 启动 GUI 界面
        SwingUtilities.invokeLater(() -> {
            boundary.GUIView.main(args);
        });
    }
}