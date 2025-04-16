package boundary;

class AIAdvicePanel extends Panel {
    public AIAdvicePanel(Color border, Color fill) {
        super(border, fill);
    }

    @Override
    public void init() {
        System.out.println("AI建议面板初始化");
    }

    @Override
    public void updateData() {
        System.out.println("AI建议面板数据更新");
    }
}
