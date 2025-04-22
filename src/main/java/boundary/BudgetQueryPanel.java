package main.java.boundary;

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

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BudgetQueryPanel extends JPanel {
    private JTextField queryField;
    private JTextField dateField;
    private JButton queryButton;
    private JButton resetButton; // 添加 Reset 按钮
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
        dateField = new JTextField(20);
        queryButton = new JButton("Query");
        resetButton = new JButton("Reset"); // 初始化 Reset 按钮
        queryPanel.add(new JLabel("ID/Description:"));
        queryPanel.add(queryField);
        queryPanel.add(new JLabel("Date:"));
        queryPanel.add(dateField);
        queryPanel.add(queryButton);
        queryPanel.add(resetButton); // 将 Reset 按钮添加到面板

        add(queryPanel, BorderLayout.NORTH);

        // 创建表格模型和表格
        String[] columnNames = {"Budget ID", "Amount", "Type", "Owner", "Date"};
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

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetFields();
            }
        });
    }

    private void queryData() {
        String query = queryField.getText();
        String dateString = dateField.getText();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date queryDate = null;
        try {
            queryDate = dateString.isEmpty() ? null : dateFormat.parse(dateString);
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Please use yyyy-MM-dd.");
            return;
        }

        System.out.println("查询条件: " + query);

        // 清空表格数据
        tableModel.setRowCount(0);

        // 读取 CSV 文件并查询数据
        List<BudgetItem> items = loadBudgetItems();
        for (BudgetItem item : items) {
            if ((item.getBudgetId().equals(query) || item.getDescription().contains(query)) && (queryDate == null || item.getDate().equals(queryDate))) {
                Object[] row = new Object[]{
                        item.getBudgetId(),
                        item.getAmount(),
                        item.getType(),
                        item.getOwner(),
                        item.getDate()
                };
                tableModel.addRow(row);
            }
        }
    }

    private List<BudgetItem> loadBudgetItems() {
        List<BudgetItem> items = new ArrayList<>();
        String filePath = "src/main/resources/budget.csv"; // 修改文件路径
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length == 5) {
                    String budgetId = values[0];
                    double amount = Double.parseDouble(values[1]);
                    String type = values[2];
                    String owner = values[3];
                    String date = values[4];
                    items.add(new BudgetItem(budgetId, amount, type, owner, date));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return items;
    }

    private void resetFields() {
        // 清空所有查询输入字段
        queryField.setText("");
        dateField.setText("");
        // 清空表格数据
        tableModel.setRowCount(0);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Budget Query Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new BudgetQueryPanel(Color.BLACK, Color.WHITE));
        frame.pack();
        frame.setSize(400, 300);
        frame.setVisible(true);
    }

    static class BudgetItem {
        private String budgetId;
        private double amount;
        private String type;
        private String owner;
        private Date date;

        public BudgetItem(String budgetId, double amount, String type, String owner, String dateString) {
            this.budgetId = budgetId;
            this.amount = amount;
            this.type = type;
            this.owner = owner;
            try {
                this.date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        public String getBudgetId() {
            return budgetId;
        }

        public double getAmount() {
            return amount;
        }

        public String getDescription() {
            return type;
        }

        public String getType() {
            return type;
        }

        public String getOwner() {
            return owner;
        }

        public Date getDate() {
            return date;
        }
    }
}