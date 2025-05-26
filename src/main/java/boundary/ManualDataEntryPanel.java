package boundary;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import control.SavingManager;
import control.TransactionManager;
import control.UserManager;
import entity.Transaction;
import entity.TransactionType;
import entity.User;
import java.util.UUID;

/**
 * Panel for manual entry of transaction data.
 * Provides a form interface for users to input transaction details.
 */
public class ManualDataEntryPanel extends JPanel {
    /** Text field for entering transaction amount */
    private JTextField amountField;
    /** Combo box for selecting transaction type */
    private JComboBox<String> typeComboBox;
    /** Text field for entering transaction time */
    private JTextField timeField;
    /** Text field for entering product name */
    private JTextField productNameField;
    /** Text area for entering transaction description */
    private JTextArea descriptionArea;
    /** Button for saving the transaction */
    private JButton saveButton;
    /** Text field for entering transaction location */
    private JTextField locationField;
    /** Flag indicating if the panel has been initialized */
    private static boolean isInitialized = false;

    /**
     * Constructs a new ManualDataEntryPanel with specified colors.
     * @param borderColor The color for the panel's border
     * @param fillColor The background color for the panel
     */
    public ManualDataEntryPanel(java.awt.Color borderColor, java.awt.Color fillColor) {
        setBorder(new CompoundBorder(new TitledBorder("Manual Data Entry"), new EmptyBorder(10, 10, 10, 10)));
        setBackground(fillColor);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // 创建组件
        JLabel amountLabel = new JLabel("Amount:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(amountLabel, gbc);

        amountField = new JTextField(10);
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(amountField, gbc);

        JLabel typeLabel = new JLabel("Type:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(typeLabel, gbc);

        typeComboBox = new JComboBox<>(new String[]{"income", "health", "food", "rent", "groceries","transportation","entertainment","cosmetics","education","game","digitalProduct","travel"});
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(typeComboBox, gbc);

        JLabel timeLabel = new JLabel("Time:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(timeLabel, gbc);

        timeField = new JTextField(10);
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(timeField, gbc);

        JLabel productNameLabel = new JLabel("Product Name:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(productNameLabel, gbc);

        productNameField = new JTextField(10);
        gbc.gridx = 1;
        gbc.gridy = 3;
        add(productNameField, gbc);

        JLabel locationLabel = new JLabel("Location:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(locationLabel, gbc);

        locationField = new JTextField(10);
        gbc.gridx = 1;
        gbc.gridy = 4;
        add(locationField, gbc);

        JLabel descriptionLabel = new JLabel("Description:");
        gbc.gridx = 0;
        gbc.gridy = 5;
        add(descriptionLabel, gbc);

        descriptionArea = new JTextArea(3, 20);
        descriptionArea.setLineWrap(true);
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        add(new JScrollPane(descriptionArea), gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(new JLabel(), gbc);

        saveButton = new JButton("Save");
        gbc.gridx = 1;
        gbc.gridy = 7;
        add(saveButton, gbc);

        // 添加按钮事件监听器
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveData();
            }
        });
        
        // 初始化时加载历史交易数据，确保只初始化一次
        if (!isInitialized) {
            // 确保 SavingManager 已加载数据
            SavingManager.getInstance().Init(); // 初始化并加载数据
            isInitialized = true;
            System.out.println("ManualDataEntryPanel 初始化完成，已加载历史交易记录");
        }
    }

    /**
     * Saves the entered transaction data.
     * Validates input and stores the transaction in the system.
     */
    private void saveData() {
        String amount = amountField.getText();
        String type = (String) typeComboBox.getSelectedItem();
        String time = timeField.getText();
        String productName = productNameField.getText();
        String description = descriptionArea.getText();
        String location = locationField.getText();
        
        if (amount.isEmpty() || time.isEmpty()) {
            JOptionPane.showMessageDialog(this, "金额和时间不能为空", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // 获取当前登录用户
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            JOptionPane.showMessageDialog(this, "请先登录后再保存交易记录", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // 先确保 TransactionManager 已加载数据
        TransactionManager transactionManager = TransactionManager.getInstance();
        
        // 创建 Transaction 对象
        Transaction transaction = new Transaction();
        // 使用自动递增的ID
        transaction.transactionId = transactionManager.getMaxTransactionId();
        transaction.amount = amount;
        transaction.type = TransactionType.valueOf(type); // 转换为枚举类型
        transaction.date = time;
        transaction.description = description;
        
        // 设置 owner 为当前用户对象
        transaction.owner = currentUser; // 这里确保使用User对象而不是String
        transaction.isIncome = type.equals("income"); // 如果类型是 income，则为收入
        transaction.location = location.isEmpty() ? "默认位置" : location; // 使用用户输入的位置或默认位置
        
        // 打印调试信息
        System.out.println("DEBUG - Transaction Owner ID: " + (transaction.owner != null ? transaction.owner.userId : "null"));
        System.out.println("DEBUG - Transaction Owner Name: " + (transaction.owner != null ? transaction.owner.name : "null"));
        
        // 添加到 TransactionManager
        transactionManager.importData(transaction);
        
        try {
            // 保存到CSV文件
            boolean success = SavingManager.getInstance().saveTransactionsToCSV();
            
            if (success) {
                // 清空输入字段
                amountField.setText("");
                timeField.setText("");
                productNameField.setText("");
                descriptionArea.setText("");
                locationField.setText("");
                
                JOptionPane.showMessageDialog(this, "交易记录保存成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "保存失败，请检查日志", "错误", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "保存失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }

        // 输出到控制台
        System.out.println("Amount: " + amount);
        System.out.println("Type: " + type);
        System.out.println("Time: " + time);
        System.out.println("Product Name: " + productName);
        System.out.println("Description: " + description);
        System.out.println("Location: " + location);
        System.out.println("Data saved successfully.");
    }

    /**
     * Gets the currently logged-in user.
     * @return The current user object, or null if no user is logged in
     */
    private User getCurrentUser() {
        UserManager userManager = UserManager.getInstance();
        String currentUserId = userManager.getCurrentUserId();
        
        if (currentUserId == null || currentUserId.isEmpty()) {
            // 如果没有登录用户，提示用户登录
            JOptionPane.showMessageDialog(this, "请先登录后再进行操作", "提示", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        
        User user = userManager.getUserById(currentUserId);
        System.out.println("当前登录用户: " + (user != null ? user.name : "未找到用户"));
        return user;
    }

    /**
     * Main method for testing the ManualDataEntryPanel.
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame("Manual Data Entry");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new ManualDataEntryPanel(java.awt.Color.MAGENTA, Color.LIGHT_GRAY));
        frame.pack();
        frame.setSize(400, 300);
        frame.setVisible(true);
    }
}