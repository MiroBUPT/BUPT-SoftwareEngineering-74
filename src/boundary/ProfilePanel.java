package boundary;

class ProfilePanel extends Panel {
    public ProfilePanel(Color border, Color fill) {
        super(border, fill);
    }

    @Override
    public void init() {
        System.out.println("用户资料面板初始化");
    }

    @Override
    public void updateData() {
        System.out.println("用户资料面板数据更新");
    }
}
