package main.java.boundary;

/*class TransactionQueryPanel extends Panel {
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
}*/
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.awt.Color;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class TransactionQueryPanel extends JPanel {
    private JTextField dateField; // 日期查询字段
    private JTextField ownerField; // 所有者查询字段
    private JTextField locationField; // 地点查询字段
    private JComboBox<String> typeComboBox; // 类型下拉菜单
    private JButton queryButton;
    private JButton resetButton; // 添加 Reset 按钮
    private JTable resultsTable;
    private DefaultTableModel tableModel;

    public TransactionQueryPanel(Color borderColor, Color fillColor) {
        setBorder(BorderFactory.createLineBorder(borderColor));
        setBackground(fillColor);
        setLayout(new BorderLayout());
        init();
    }

    private void init() {
        System.out.println("交易查询面板初始化");

        // 创建查询输入字段和按钮
        JPanel queryPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        dateField = new JTextField(10);
        ownerField = new JTextField(10);
        locationField = new JTextField(10);
        typeComboBox = new JComboBox<>(new String[]{"All", "income", "health", "food", "rent", "groceries"}); // 添加默认选项 "All"
        queryButton = new JButton("Query");
        resetButton = new JButton("Reset"); // 添加 Reset 按钮

        queryPanel.add(new JLabel("Date:"));
        queryPanel.add(dateField);
        queryPanel.add(new JLabel("Owner:"));
        queryPanel.add(ownerField);
        queryPanel.add(new JLabel("Location:"));
        queryPanel.add(locationField);
        queryPanel.add(new JLabel("Type:"));
        queryPanel.add(typeComboBox);
        queryPanel.add(queryButton);
        queryPanel.add(resetButton); // 将 Reset 按钮添加到面板

        add(queryPanel, BorderLayout.NORTH);

        // 创建表格模型和表格
        String[] columnNames = {"Date", "Description", "Amount", "Account", "Type", "Income/Outcome", "Location"};
        tableModel = new DefaultTableModel(columnNames, 0);
        resultsTable = new JTable(tableModel);

        // 添加表格到面板
        add(new JScrollPane(resultsTable), BorderLayout.CENTER);

        // 添加 Query 按钮事件监听器
        queryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                queryTransactions();
            }
        });

        // 添加 Reset 按钮事件监听器
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetFields();
            }
        });
    }

    private void queryTransactions() {
        String date = dateField.getText().trim(); // 日期查询条件
        String owner = ownerField.getText().toLowerCase(); // 所有者查询条件
        String location = locationField.getText().toLowerCase(); // 地点查询条件
        String type = (String) typeComboBox.getSelectedItem(); // 类型查询条件

        System.out.println("Query Conditions: Date: " + date + ", Owner: " + owner + ", Location: " + location + ", Type: " + type);

        // 清空表格数据
        tableModel.setRowCount(0);

        // 读取 CSV 文件并过滤数据
        List<Object[]> filteredData = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/transaction.csv"))) { // 修改文件路径
            String line;
            boolean isFirstLine = true; // 跳过标题行
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false; // 跳过标题行
                    continue;
                }
                String[] fields = line.split(",");
                String transactionId = fields[0];
                String transactionDate = fields[1];
                double amount = Double.parseDouble(fields[2]);
                String description = fields[3];
                String transactionType = fields[4];
                String transactionOwner = fields[5];
                boolean isIncome = fields[6].equalsIgnoreCase("true");
                String transactionLocation = fields[7];

                // 检查是否匹配查询条件
                boolean matches = true;
                if (!date.isEmpty() && !transactionDate.equals(date)) {
                    matches = false;
                }
                if (!owner.isEmpty() && !transactionOwner.toLowerCase().contains(owner)) {
                    matches = false;
                }
                if (!location.isEmpty() && !transactionLocation.toLowerCase().contains(location)) {
                    matches = false;
                }
                if (!type.equals("All") && !transactionType.equals(type)) { // 处理 "All" 选项
                    matches = false;
                }

                if (matches) {
                    filteredData.add(new Object[]{
                            transactionDate, description, amount, transactionOwner,
                            transactionType, isIncome ? "Income" : "Outcome", transactionLocation
                    });
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Cannot read CSV : " + e.getMessage());
            return;
        }

        // 将过滤后的数据显示在表格中
        for (Object[] row : filteredData) {
            tableModel.addRow(row);
        }
    }

    private void resetFields() {
        // 清空所有查询输入字段
        dateField.setText("");
        ownerField.setText("");
        locationField.setText("");
        typeComboBox.setSelectedIndex(0); // 重置下拉菜单到第一个选项（"All"）
        tableModel.setRowCount(0); // 清空表格数据
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Transactions Query Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new TransactionQueryPanel(Color.BLACK, Color.WHITE));
        frame.pack();
        frame.setSize(600, 400);
        frame.setVisible(true);
    }
}