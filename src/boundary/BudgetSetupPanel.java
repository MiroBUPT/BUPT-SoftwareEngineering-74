package boundary;

class BudgetSetupPanel extends Panel {
    public BudgetSetupPanel(Color border, Color fill) {
        super(border, fill);
    }

    @Override
    public void init() {
        System.out.println("预算设置面板初始化");
    }

    @Override
    public void updateData() {
        System.out.println("预算设置面板数据更新");
    }
}
