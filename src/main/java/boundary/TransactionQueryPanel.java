package boundary;

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

import control.TransactionManager;
import control.UserManager;
import entity.Transaction;
import entity.TransactionType;

public class TransactionQueryPanel extends JPanel {
    private JTextField dateField; // 日期查询字段
    private JTextField ownerField; // 所有者查询字段
    private JTextField locationField; // 地点查询字段
    private JComboBox<String> typeComboBox; // 类型下拉菜单
    private JButton queryButton;
    private JButton resetButton; // 添加 Reset 按钮
    private JTable resultsTable;
    private DefaultTableModel tableModel;
    private TransactionManager transactionManager;
    private UserManager userManager;

    public TransactionQueryPanel(Color borderColor, Color fillColor) {
        setBorder(BorderFactory.createLineBorder(borderColor));
        setBackground(fillColor);
        setLayout(new BorderLayout());
        
        // 初始化管理器
        transactionManager = TransactionManager.getInstance();
        userManager = UserManager.getInstance();
        
        init();
    }

    private void init() {
        System.out.println("交易查询面板初始化");

        // 创建查询输入字段和按钮
        JPanel queryPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        dateField = new JTextField(10);
        ownerField = new JTextField(10);
        locationField = new JTextField(10);
        typeComboBox = new JComboBox<>(new String[]{"All", "income", "health", "food", "rent", "groceries","transportation","entertainment","cosmetics","education","game","digitalProduct","travel"}); // 添加默认选项 "All"
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
                queryData();
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

    private void queryData() {
        String date = dateField.getText();
        String owner = ownerField.getText().toLowerCase();
        String location = locationField.getText().toLowerCase();
        String type = (String) typeComboBox.getSelectedItem();

        // 清空表格数据
        tableModel.setRowCount(0);

        // 获取所有交易记录
        List<Transaction> transactions = transactionManager.getTransactionList();
        List<Object[]> filteredData = new ArrayList<>();

        for (Transaction transaction : transactions) {
            // 检查是否匹配查询条件
            boolean matches = true;
            
            if (!date.isEmpty() && !transaction.date.equals(date)) {
                matches = false;
            }
            if (!owner.isEmpty() && !transaction.owner.name.toLowerCase().contains(owner)) {
                matches = false;
            }
            if (!location.isEmpty() && !transaction.location.toLowerCase().contains(location)) {
                matches = false;
            }
            if (!type.equals("All") && !transaction.type.name().equals(type)) {
                matches = false;
            }

            if (matches) {
                filteredData.add(new Object[]{
                    transaction.date,
                    transaction.description,
                    transaction.amount,
                    transaction.owner.name,
                    transaction.type.name(),
                    transaction.isIncome ? "Income" : "Outcome",
                    transaction.location
                });
            }
        }

        // 将过滤后的数据添加到表格
        for (Object[] row : filteredData) {
            tableModel.addRow(row);
        }
    }

    private void resetFields() {
        // 清空所有查询输入字段
        dateField.setText("");
        ownerField.setText("");
        locationField.setText("");
        typeComboBox.setSelectedItem("All");
        tableModel.setRowCount(0); // 清空表格数据
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Transaction Query Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new TransactionQueryPanel(Color.BLACK, Color.WHITE));
        frame.pack();
        frame.setSize(800, 600);
        frame.setVisible(true);
    }
}