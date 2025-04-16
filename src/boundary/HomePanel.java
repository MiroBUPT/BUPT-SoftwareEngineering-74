package boundary;

class HomePanel extends Panel {
    public HomePanel(Color border, Color fill) {
        super(border, fill);
    }

    @Override
    public void init() {
        System.out.println("主页面板初始化");
    }

    @Override
    public void updateData() {
        System.out.println("主页面板数据更新");
    }
}
