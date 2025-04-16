package boundary;

class ManualDataEntryPanel extends Panel {
    public ManualDataEntryPanel(Color border, Color fill) {
        super(border, fill);
    }

    @Override
    public void init() {
        System.out.println("手动数据录入面板初始化");
    }

    @Override
    public void updateData() {
        System.out.println("手动数据录入面板数据更新");
    }
}
