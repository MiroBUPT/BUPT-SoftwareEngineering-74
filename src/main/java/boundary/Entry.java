package boundary;
import javax.swing.*;

/**
 * Represents a navigation entry in the application's menu structure.
 * This class manages a single menu item and its associated panel.
 */
class Entry {
    /** The name of the entry */
    private String name;
    /** The panel associated with this entry */
    private JPanel targetPanel;

    /**
     * Constructs a new Entry with the specified name and panel.
     * @param name The name of the entry
     * @param targetPanel The panel to be displayed when this entry is selected
     */
    public Entry(String name, JPanel targetPanel) {
        this.name = name;
        this.targetPanel = targetPanel;
    }

    /**
     * Gets the name of this entry.
     * @return The entry name
     */
    public String getName() {
        return name; // 确保返回的名称与菜单树中的节点名称一致
    }

    /**
     * Gets the panel associated with this entry.
     * @return The target panel
     */
    public JPanel getTargetPanel() {
        return targetPanel;
    }

    /**
     * Initializes the entry and its associated panel.
     * This method is called when the entry is first created.
     */
    public void init() {
        // 初始化面板（如果有需要）
    }
}




