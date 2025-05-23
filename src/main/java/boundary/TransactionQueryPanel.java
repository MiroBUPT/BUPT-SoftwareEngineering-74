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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import control.TransactionManager;
import control.UserManager;
import entity.Transaction;
import entity.TransactionType;
import control.SavingManager;

public class TransactionQueryPanel extends JPanel {
    private JTextField dateField; // 日期查询字段
    private JTextField ownerField; // 所有者查询字段
    private JTextField locationField; // 地点查询字段
    private JComboBox<String> typeComboBox; // 类型下拉菜单 (查询用)
    private JButton queryButton;
    private JButton resetButton; // 添加 Reset 按钮
    private JTable resultsTable;
    private DefaultTableModel tableModel;
    private TransactionManager transactionManager;
    private UserManager userManager;
    private JComboBox<TransactionType> modifyTypeComboBox; // 修改类型下拉菜单
    private JButton modifyTypeButton; // 修改类型按钮

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
        String[] columnNames = {"ID", "Date", "Description", "Amount", "Account", "Type", "Income/Outcome", "Location"};
        tableModel = new DefaultTableModel(columnNames, 0);
        resultsTable = new JTable(tableModel);
        // Hide the ID column, but keep it in the model to retrieve the transaction ID
        resultsTable.getColumnModel().getColumn(0).setMinWidth(0);
        resultsTable.getColumnModel().getColumn(0).setMaxWidth(0);
        resultsTable.getColumnModel().getColumn(0).setWidth(0);

        // 添加表格到面板
        add(new JScrollPane(resultsTable), BorderLayout.CENTER);
        
        // 创建修改类型面板
        JPanel modifyPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        // 修改类型下拉框
        modifyTypeComboBox = new JComboBox<>(TransactionType.values());
        modifyPanel.add(new JLabel("Change Type to:"));
        modifyPanel.add(modifyTypeComboBox);
        
        // 修改类型按钮
        modifyTypeButton = new JButton("Modify Type");
        modifyPanel.add(modifyTypeButton);
        modifyTypeButton.setEnabled(false); // Initially disabled

        add(modifyPanel, BorderLayout.SOUTH);

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
        
        // 添加表格行选择监听器
        resultsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && resultsTable.getSelectedRow() != -1) {
                    // A row is selected, enable modify button and show current type
                    modifyTypeButton.setEnabled(true);
                    int selectedRow = resultsTable.getSelectedRow();
                    String currentTypeString = (String) resultsTable.getValueAt(selectedRow, 5); // Type is at column index 5
                    try {
                         TransactionType currentType = TransactionType.valueOf(currentTypeString);
                         modifyTypeComboBox.setSelectedItem(currentType);
                    } catch (IllegalArgumentException ex) {
                         // Handle cases where the type in the table doesn't match enum names
                         modifyTypeComboBox.setSelectedItem(TransactionType.none); // Or another default
                    }
                } else {
                    // No row is selected, disable modify button
                    modifyTypeButton.setEnabled(false);
                }
            }
        });
        
        // 添加修改类型按钮事件监听器
        modifyTypeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = resultsTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(TransactionQueryPanel.this, 
                            "Please select a transaction to modify.", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                String transactionId = (String) resultsTable.getValueAt(selectedRow, 0); // Get ID from hidden column
                TransactionType newType = (TransactionType) modifyTypeComboBox.getSelectedItem();
                
                // Get the Transaction object from the manager based on ID to ensure we have the full object
                Transaction transactionToModify = null;
                for (Transaction transaction : transactionManager.getTransactionList()) {
                    if (transaction.transactionId.equals(transactionId)) {
                        transactionToModify = transaction;
                        break;
                    }
                }

                if (transactionToModify != null) {
                    // Create a copy or modify directly - let's modify directly for simplicity if editData handles replacement
                    // Assuming editData replaces the object based on ID
                    // Update the type on the found object and call editData
                    transactionToModify.type = newType;
                    transactionManager.editData(transactionToModify, transactionId);
                    
                    // Save changes to file
                    SavingManager.getInstance().saveTransactionsToCSV();
                    
                    // Refresh table display
                    queryData(); // Re-run the query to update the table

                    JOptionPane.showMessageDialog(TransactionQueryPanel.this, 
                            "Transaction type modified successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                     JOptionPane.showMessageDialog(TransactionQueryPanel.this, 
                            "Could not find the transaction in data manager.", "Error", JOptionPane.ERROR_MESSAGE);
                }
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
            // Handle the case where type is null in Transaction object
            String transactionTypeName = (transaction.type != null) ? transaction.type.name() : "none";
            if (!type.equals("All") && !transactionTypeName.equals(type)) {
                matches = false;
            }

            if (matches) {
                tableModel.addRow(new Object[]{
                    transaction.transactionId, // Add ID to the model
                    transaction.date,
                    transaction.description,
                    transaction.amount,
                    transaction.owner.name,
                    transactionTypeName, // Use the potentially null-safe type name
                    transaction.isIncome ? "Income" : "Outcome",
                    transaction.location
                });
            }
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
        frame.setSize(900, 600); // Increased size to accommodate new controls
        frame.setVisible(true);
    }
}