package boundary;

/**
 * View class for managing panel transitions and initialization.
 * Handles the current active panel and its lifecycle.
 */
class PanelView {
    /** The currently active panel */
    private Panel currentPanel;

    /**
     * Sets the current panel and initializes it.
     * @param panel The panel to set as current
     */
    public void setCurrentPanel(Panel panel) {
        this.currentPanel = panel;
        if (currentPanel != null) {
            currentPanel.init(); // 初始化面板
        }
    }
}
