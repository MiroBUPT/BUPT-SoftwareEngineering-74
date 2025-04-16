package boundary;

class DataAnalysisPanel extends Panel {
    public DataAnalysisPanel(Color border, Color fill) {
        super(border, fill);
    }

    @Override
    public void init() {
        System.out.println("数据分析面板初始化");
    }

    @Override
    public void updateData() {
        System.out.println("数据分析面板数据更新");
    }
}
