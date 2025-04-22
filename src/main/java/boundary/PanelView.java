package main.java.boundary;

class PanelView {
    private Panel currentPanel;

    public void setCurrentPanel(Panel panel) {
        this.currentPanel = panel;
        if (currentPanel != null) {
            currentPanel.init(); // 初始化面板
        }
    }
}
