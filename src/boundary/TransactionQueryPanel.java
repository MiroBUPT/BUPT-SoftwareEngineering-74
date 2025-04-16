package boundary;

class TransactionQueryPanel extends Panel {
    public TransactionQueryPanel(Color border, Color fill) {
        super(border, fill);
    }

    @Override
    public void init() {
        System.out.println("交易查询面板初始化");
    }

    @Override
    public void updateData() {
        System.out.println("交易查询面板数据更新");
    }
}
