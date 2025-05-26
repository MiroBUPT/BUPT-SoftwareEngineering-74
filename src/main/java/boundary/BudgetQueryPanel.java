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

/**
 * Panel for querying and displaying budget information.
 * Provides search functionality and tabular display of budget records.
 */
public class BudgetQueryPanel extends JPanel {
    /** Text field for entering budget ID */
    private JTextField queryField;
    /** Text field for entering date */
    private JTextField dateField;
    /** Text field for entering owner name */
    private JTextField ownerField;
    /** Combo box for selecting budget type */
    private JComboBox<String> typeComboBox;
    /** Button to execute the search query */
    private JButton queryButton;
    /** Button to reset all search fields */
    private JButton resetButton;
    /** Table displaying search results */
    private JTable resultsTable;
    /** Model for the results table */
    private DefaultTableModel tableModel;
    /** Manager for budget-related operations */
    private BudgetManager budgetManager;
    /** Manager for user-related operations */
    private UserManager userManager;

    /**
     * Constructs a new BudgetQueryPanel with specified colors.
     * @param borderColor The color for the panel's border
     * @param fillColor The background color for the panel
     */
    public BudgetQueryPanel(Color borderColor, Color fillColor) {
        setBorder(BorderFactory.createLineBorder(borderColor));
        setBackground(fillColor);
        setLayout(new BorderLayout());
        
        // 初始化管理器
        budgetManager = BudgetManager.getInstance();
        userManager = UserManager.getInstance();
        
        init();
    }

    /**
     * Initializes the panel components and sets up event listeners.
     * Creates search fields, buttons, and results table.
     */
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

    /**
     * Executes the search query based on current filter values.
     * Updates the results table with matching budget records.
     */
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

    /**
     * Resets all search fields to their default values.
     */
    private void resetFields() {
        queryField.setText("");
        dateField.setText("");
        ownerField.setText("");
        typeComboBox.setSelectedItem("All");
        tableModel.setRowCount(0);
    }

    /**
     * Main method for testing the BudgetQueryPanel.
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame("Budget Query Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new BudgetQueryPanel(Color.BLACK, Color.WHITE));
        frame.pack();
        frame.setSize(800, 600);
        frame.setVisible(true);
    }

    /**
     * Inner class representing a budget item with its properties.
     */
    static class BudgetItem {
        /** Unique identifier for the budget */
        private String budgetId;
        /** Amount allocated for the budget */
        private double amount;
        /** Type of the budget */
        private String type;
        /** Owner of the budget */
        private String owner;
        /** Date of the budget */
        private Date date;

        /**
         * Constructs a new BudgetItem with specified properties.
         * @param budgetId The budget identifier
         * @param amount The budget amount
         * @param type The budget type
         * @param owner The budget owner
         * @param dateString The budget date in yyyy-MM-dd format
         */
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

        /**
         * Gets the budget identifier.
         * @return The budget ID
         */
        public String getBudgetId() {
            return budgetId;
        }

        /**
         * Gets the budget amount.
         * @return The budget amount
         */
        public double getAmount() {
            return amount;
        }

        /**
         * Gets the budget description.
         * @return The budget type as description
         */
        public String getDescription() {
            return type;
        }

        /**
         * Gets the budget type.
         * @return The budget type
         */
        public String getType() {
            return type;
        }

        /**
         * Gets the budget owner.
         * @return The budget owner
         */
        public String getOwner() {
            return owner;
        }

        /**
         * Gets the budget date.
         * @return The budget date
         */
        public Date getDate() {
            return date;
        }
    }
}