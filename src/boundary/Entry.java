package boundary;

class Entry {
    private String name;
    private Panel targetPanel;

    public Entry(String name, Panel targetPanel) {
        this.name = name;
        this.targetPanel = targetPanel;
    }

    public void onClick() {
        System.out.println("点击条目:" + name + "切换到面板" + targetPanel.getClass().getName());
    }
}
