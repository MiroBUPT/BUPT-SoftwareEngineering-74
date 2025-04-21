package boundary;

/* BudgetQueryPanel extends Panel {
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
}*/
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;

public class BudgetQueryPanel extends JPanel {
    private JTextField queryField;
    private JButton queryButton;
    private JTable resultsTable;
    private DefaultTableModel tableModel;

    public BudgetQueryPanel(Color borderColor, Color fillColor) {
        setBorder(BorderFactory.createLineBorder(borderColor));
        setBackground(fillColor);
        setLayout(new BorderLayout());
        init();
    }

    private void init() {
        System.out.println("预算查询面板初始化");

        // 创建查询输入字段和按钮
        JPanel queryPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        queryField = new JTextField(20);
        queryButton = new JButton("查询");
        queryPanel.add(queryField);
        queryPanel.add(queryButton);

        add(queryPanel, BorderLayout.NORTH);

        // 创建表格模型和表格
        String[] columnNames = {"Date", "Description", "Amount", "Owner"};
        tableModel = new DefaultTableModel(columnNames, 0);
        resultsTable = new JTable(tableModel);

        // 添加表格到面板
        add(new JScrollPane(resultsTable), BorderLayout.CENTER);

        // 添加按钮事件监听器
        queryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                queryData();
            }
        });
    }

    private void queryData() {
        // 这里应该是查询数据库或文件的代码
        // 为了演示，我们只是添加一些示例数据
        String query = queryField.getText();
        System.out.println("查询条件: " + query);

        // 清空表格数据
        tableModel.setRowCount(0);

        Object[][] data = {
            {"2024-05-20", "办公用品", 150.0, "张三"},
            {"2024-05-19", "交通费", 200.0, "李四"},
            {"2024-05-18", "餐费", 120.0, "王五"},
            // 可以添加更多数据
        };

        for (Object[] row : data) {
            tableModel.addRow(row);
        }
    }



    public static void main(String[] args) {
        JFrame frame = new JFrame("Budget Query Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new BudgetQueryPanel(Color.BLACK, Color.WHITE));
        frame.pack();
        frame.setSize(400, 300);
        frame.setVisible(true);
    }
}