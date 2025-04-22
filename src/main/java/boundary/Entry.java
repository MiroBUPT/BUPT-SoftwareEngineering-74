package boundary;
import javax.swing.*;

class Entry {
    private String name;
    private JPanel targetPanel;

    public Entry(String name, JPanel targetPanel) {
        this.name = name;
        this.targetPanel = targetPanel;
    }

    public String getName() {
        return name; // 确保返回的名称与菜单树中的节点名称一致
    }

    public JPanel getTargetPanel() {
        return targetPanel;
    }

    public void init() {
        // 初始化面板（如果有需要）
    }
}




