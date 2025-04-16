package boundary;

class BudgetQueryPanel extends Panel {
    public BudgetQueryPanel(Color border, Color fill) {
        super(border, fill);
    }

    @Override
    public void init() {
        System.out.println("预算查询面板初始化");
    }

    @Override
    public void updateData() {
        System.out.println("预算查询面板数据更新");
    }
}
