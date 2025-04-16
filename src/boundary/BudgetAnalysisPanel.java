package boundary;

class BudgetAnalysisPanel extends Panel {
    public BudgetAnalysisPanel(Color border, Color fill) {
        super(border, fill);
    }

    @Override
    public void init() {
        System.out.println("预算分析面板初始化");
    }

    @Override
    public void updateData() {
        System.out.println("预算分析面板数据更新");
    }
}
