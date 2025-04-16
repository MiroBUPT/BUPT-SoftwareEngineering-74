package boundary;

abstract class Panel {
    private Color border;
    private Color fill;

    public Panel(Color border, Color fill) {
        this.border = border;
        this.fill = fill;
    }

    public Color getBorder() {
        return border;
    }

    public Color getFill() {
        return fill;
    }

    public abstract void init(); // 初始化面板
    public abstract void updateData(); // 更新面板数据
}
