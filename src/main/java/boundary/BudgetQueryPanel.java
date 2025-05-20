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

import control.BudgetManager;
import control.UserManager;
import entity.Budget;
import entity.TransactionType;

public class BudgetQueryPanel extends JPanel {
    private JTextField queryField;
    private JTextField dateField;
    private JTextField ownerField;
    private JComboBox<String> typeComboBox;
    private JButton queryButton;
    private JButton resetButton;
    private JTable resultsTable;
    private DefaultTableModel tableModel;
    private BudgetManager budgetManager;
    private UserManager userManager;

    public BudgetQueryPanel(Color borderColor, Color fillColor) {
        setBorder(BorderFactory.createLineBorder(borderColor));
        setBackground(fillColor);
        setLayout(new BorderLayout());
        
        // 初始化管理器
        budgetManager = BudgetManager.getInstance();
        userManager = UserManager.getInstance();
        
        init();
    }

    private void init() {
        System.out.println("预算查询面板初始化");

        // 创建查询输入字段和按钮
        JPanel queryPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        queryField = new JTextField(10);
        dateField = new JTextField(10);
        ownerField = new JTextField(10);
        typeComboBox = new JComboBox<>(new String[]{"All", "income", "health", "food", "rent", "groceries", "transportation", "entertainment", "cosmetics", "education", "game", "digitalProduct", "travel"});
        queryButton = new JButton("Query");
        resetButton = new JButton("Reset");

        queryPanel.add(new JLabel("BudgetID:"));
        queryPanel.add(queryField);
        queryPanel.add(new JLabel("Date:"));
        queryPanel.add(dateField);
        queryPanel.add(new JLabel("Owner:"));
        queryPanel.add(ownerField);
        queryPanel.add(new JLabel("Type:"));
        queryPanel.add(typeComboBox);
        queryPanel.add(queryButton);
        queryPanel.add(resetButton);

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
        String owner = ownerField.getText().toLowerCase();
        String type = (String) typeComboBox.getSelectedItem();
        
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

        // 获取所有预算记录
        List<Budget> budgets = budgetManager.getBudgetList();
        for (Budget budget : budgets) {
            boolean matches = true;
            
            // 检查ID或描述是否匹配
            if (!query.isEmpty() && 
                !budget.budgetId.equals(query) && 
                !budget.type.name().toLowerCase().contains(query.toLowerCase())) {
                matches = false;
            }
            
            // 检查日期是否匹配
            if (queryDate != null) {
                try {
                    Date budgetDate = dateFormat.parse(budget.date);
                    if (!budgetDate.equals(queryDate)) {
                        matches = false;
                    }
                } catch (ParseException e) {
                    matches = false;
                }
            }

            // 检查所有者是否匹配
            if (!owner.isEmpty() && !budget.owner.name.toLowerCase().contains(owner)) {
                matches = false;
            }

            // 检查类型是否匹配
            if (!type.equals("All") && !budget.type.name().equals(type)) {
                matches = false;
            }

            if (matches) {
                Object[] row = new Object[]{
                    budget.budgetId,
                    budget.amount,
                    budget.type.name(),
                    budget.owner.name,
                    budget.date
                };
                tableModel.addRow(row);
            }
        }
    }

    private void resetFields() {
        queryField.setText("");
        dateField.setText("");
        ownerField.setText("");
        typeComboBox.setSelectedItem("All");
        tableModel.setRowCount(0);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Budget Query Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new BudgetQueryPanel(Color.BLACK, Color.WHITE));
        frame.pack();
        frame.setSize(800, 600);
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