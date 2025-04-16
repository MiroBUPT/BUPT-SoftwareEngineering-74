package com.example.personalfinance;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PersonalFinanceAppGUI extends JFrame {
    private JPanel sidebarPanel;
    private JPanel mainPanel;

    public PersonalFinanceAppGUI() {
        setTitle("个人财务管理软件");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 创建侧边栏面板
        sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        addSidebarButtons();

        // 创建主面板
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // 将侧边栏和主面板添加到主窗口
        add(sidebarPanel, BorderLayout.WEST);
        add(mainPanel, BorderLayout.CENTER);
    }

    private void addSidebarButtons() {
        // 用户管理相关按钮
        addSidebarButton("用户登录", e -> showLoginPanel());
        addSidebarButton("用户注册", e -> showRegistrationPanel());
        addSidebarButton("用户信息修改", e -> showUserInfoEditPanel());

        // 交易管理相关按钮
        addSidebarButton("交易数据导入", e -> showTransactionImportPanel());
        addSidebarButton("交易数据编辑", e -> showTransactionEditPanel());
        addSidebarButton("交易数据查询", e -> showTransactionQueryPanel());

        // 预算管理相关按钮
        addSidebarButton("预算添加", e -> showBudgetAddPanel());
        addSidebarButton("预算编辑", e -> showBudgetEditPanel());
        addSidebarButton("预算查询", e -> showBudgetQueryPanel());

        // 存储管理相关按钮
        addSidebarButton("数据保存", e -> showDataSavePanel());
        addSidebarButton("数据初始化", e -> showDataInitPanel());

        // AI 管理相关按钮
        addSidebarButton("生成建议", e -> showGenerateAdvicePanel());
        addSidebarButton("预测交易类型", e -> showPredictTransactionTypePanel());
        addSidebarButton("预测预算", e -> showPredictBudgetPanel());
    }

    private void addSidebarButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.addActionListener(listener);
        sidebarPanel.add(button);
    }

    // 以下是各个面板显示方法的具体实现
    private void showLoginPanel() {
        mainPanel.removeAll();
        JPanel loginPanel = new JPanel();
        JLabel usernameLabel = new JLabel("用户名:");
        JTextField usernameField = new JTextField(20);
        JLabel passwordLabel = new JLabel("密码:");
        JPasswordField passwordField = new JPasswordField(20);
        JButton loginButton = new JButton("登录");
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            // 这里进行登录验证逻辑
            JOptionPane.showMessageDialog(this, "登录成功", "提示", JOptionPane.INFORMATION_MESSAGE);
        });
        loginPanel.add(usernameLabel);
        loginPanel.add(usernameField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);
        loginPanel.add(loginButton);
        mainPanel.add(loginPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void showRegistrationPanel() {
        mainPanel.removeAll();
        JPanel registrationPanel = new JPanel();
        JLabel usernameLabel = new JLabel("用户名:");
        JTextField usernameField = new JTextField(20);
        JLabel passwordLabel = new JLabel("密码:");
        JPasswordField passwordField = new JPasswordField(20);
        JButton registerButton = new JButton("注册");
        registerButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            JOptionPane.showMessageDialog(this, "注册成功：用户名 " + username, "提示", JOptionPane.INFORMATION_MESSAGE);
        });
        registrationPanel.add(usernameLabel);
        registrationPanel.add(usernameField);
        registrationPanel.add(passwordLabel);
        registrationPanel.add(passwordField);
        registrationPanel.add(registerButton);
        mainPanel.add(registrationPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void showUserInfoEditPanel() {
        mainPanel.removeAll();
        JPanel userInfoEditPanel = new JPanel();
        JLabel usernameLabel = new JLabel("新用户名:");
        JTextField usernameField = new JTextField(20);
        JLabel passwordLabel = new JLabel("新密码:");
        JPasswordField passwordField = new JPasswordField(20);
        JButton saveButton = new JButton("保存");
        saveButton.addActionListener(e -> {
            String newUsername = usernameField.getText();
            String newPassword = new String(passwordField.getPassword());
            JOptionPane.showMessageDialog(this, "信息修改成功：新用户名 " + newUsername, "提示", JOptionPane.INFORMATION_MESSAGE);
        });
        userInfoEditPanel.add(usernameLabel);
        userInfoEditPanel.add(usernameField);
        userInfoEditPanel.add(passwordLabel);
        userInfoEditPanel.add(passwordField);
        userInfoEditPanel.add(saveButton);
        mainPanel.add(userInfoEditPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void showTransactionImportPanel() {
        mainPanel.removeAll();
        JPanel transactionImportPanel = new JPanel();
        JLabel fileLabel = new JLabel("选择文件:");
        JTextField fileField = new JTextField(20);
        JButton importButton = new JButton("导入");
        importButton.addActionListener(e -> {
            String filePath = fileField.getText();
            JOptionPane.showMessageDialog(this, "开始导入文件：" + filePath, "提示", JOptionPane.INFORMATION_MESSAGE);
        });
        transactionImportPanel.add(fileLabel);
        transactionImportPanel.add(fileField);
        transactionImportPanel.add(importButton);
        mainPanel.add(transactionImportPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void showTransactionEditPanel() {
        mainPanel.removeAll();
        JPanel transactionEditPanel = new JPanel();
        JLabel transactionIdLabel = new JLabel("交易 ID:");
        JTextField transactionIdField = new JTextField(20);
        JLabel newDataLabel = new JLabel("新数据:");
        JTextField newDataField = new JTextField(20);
        JButton editButton = new JButton("编辑");
        editButton.addActionListener(e -> {
            String transactionId = transactionIdField.getText();
            String newData = newDataField.getText();
            JOptionPane.showMessageDialog(this, "开始编辑交易：ID " + transactionId + "，新数据 " + newData, "提示", JOptionPane.INFORMATION_MESSAGE);
        });
        transactionEditPanel.add(transactionIdLabel);
        transactionEditPanel.add(transactionIdField);
        transactionEditPanel.add(newDataLabel);
        transactionEditPanel.add(newDataField);
        transactionEditPanel.add(editButton);
        mainPanel.add(transactionEditPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void showTransactionQueryPanel() {
        mainPanel.removeAll();
        JPanel transactionQueryPanel = new JPanel();
        JLabel startTimeLabel = new JLabel("开始时间:");
        JTextField startTimeField = new JTextField(20);
        JLabel endTimeLabel = new JLabel("结束时间:");
        JTextField endTimeField = new JTextField(20);
        JButton queryButton = new JButton("查询");
        queryButton.addActionListener(e -> {
            String startTime = startTimeField.getText();
            String endTime = endTimeField.getText();
            JOptionPane.showMessageDialog(this, "开始查询交易：时间范围 " + startTime + " - " + endTime, "提示", JOptionPane.INFORMATION_MESSAGE);
        });
        transactionQueryPanel.add(startTimeLabel);
        transactionQueryPanel.add(startTimeField);
        transactionQueryPanel.add(endTimeLabel);
        transactionQueryPanel.add(endTimeField);
        transactionQueryPanel.add(queryButton);
        mainPanel.add(transactionQueryPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void showBudgetAddPanel() {
        mainPanel.removeAll();
        JPanel budgetAddPanel = new JPanel();
        JLabel amountLabel = new JLabel("金额:");
        JTextField amountField = new JTextField(20);
        JButton addButton = new JButton("添加");
        addButton.addActionListener(e -> {
            String amount = amountField.getText();
            JOptionPane.showMessageDialog(this, "添加预算：金额 " + amount, "提示", JOptionPane.INFORMATION_MESSAGE);
        });
        budgetAddPanel.add(amountLabel);
        budgetAddPanel.add(amountField);
        budgetAddPanel.add(addButton);
        mainPanel.add(budgetAddPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void showBudgetEditPanel() {
        mainPanel.removeAll();
        JPanel budgetEditPanel = new JPanel();
        JLabel budgetIdLabel = new JLabel("预算 ID:");
        JTextField budgetIdField = new JTextField(20);
        JLabel newAmountLabel = new JLabel("新金额:");
        JTextField newAmountField = new JTextField(20);
        JButton editButton = new JButton("编辑");
        editButton.addActionListener(e -> {
            String budgetId = budgetIdField.getText();
            String newAmount = newAmountField.getText();
            JOptionPane.showMessageDialog(this, "编辑预算：ID " + budgetId + "，新金额 " + newAmount, "提示", JOptionPane.INFORMATION_MESSAGE);
        });
        budgetEditPanel.add(budgetIdLabel);
        budgetEditPanel.add(budgetIdField);
        budgetEditPanel.add(newAmountLabel);
        budgetEditPanel.add(newAmountField);
        budgetEditPanel.add(editButton);
        mainPanel.add(budgetEditPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void showBudgetQueryPanel() {
        mainPanel.removeAll();
        JPanel budgetQueryPanel = new JPanel();
        JLabel timeLabel = new JLabel("时间:");
        JTextField timeField = new JTextField(20);
        JButton queryButton = new JButton("查询");
        queryButton.addActionListener(e -> {
            String time = timeField.getText();
            JOptionPane.showMessageDialog(this, "查询预算：时间 " + time, "提示", JOptionPane.INFORMATION_MESSAGE);
        });
        budgetQueryPanel.add(timeLabel);
        budgetQueryPanel.add(timeField);
        budgetQueryPanel.add(queryButton);
        mainPanel.add(budgetQueryPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void showDataSavePanel() {
        mainPanel.removeAll();
        JPanel dataSavePanel = new JPanel();
        JButton saveButton = new JButton("保存数据");
        saveButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "数据保存成功", "提示", JOptionPane.INFORMATION_MESSAGE);
        });
        dataSavePanel.add(saveButton);
        mainPanel.add(dataSavePanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void showDataInitPanel() {
        mainPanel.removeAll();
        JPanel dataInitPanel = new JPanel();
        JButton initButton = new JButton("初始化数据");
        initButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "数据初始化成功", "提示", JOptionPane.INFORMATION_MESSAGE);
        });
        dataInitPanel.add(initButton);
        mainPanel.add(dataInitPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void showGenerateAdvicePanel() {
        mainPanel.removeAll();
        JPanel generateAdvicePanel = new JPanel();
        JButton generateButton = new JButton("生成建议");
        generateButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "生成建议：这是 AI 生成的建议", "提示", JOptionPane.INFORMATION_MESSAGE);
        });
        generateAdvicePanel.add(generateButton);
        mainPanel.add(generateAdvicePanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void showPredictTransactionTypePanel() {
        mainPanel.removeAll();
        JPanel predictTransactionTypePanel = new JPanel();
        JButton predictButton = new JButton("预测交易类型");
        predictButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "预测交易类型：收入", "提示", JOptionPane.INFORMATION_MESSAGE);
        });
        predictTransactionTypePanel.add(predictButton);
        mainPanel.add(predictTransactionTypePanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void showPredictBudgetPanel() {
        mainPanel.removeAll();
        JPanel predictBudgetPanel = new JPanel();
        JButton predictButton = new JButton("预测预算");
        predictButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "预测预算：暂无具体预算", "提示", JOptionPane.INFORMATION_MESSAGE);
        });
        predictBudgetPanel.add(predictButton);
        mainPanel.add(predictBudgetPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public static void main(String[] args) {
        // 在事件调度线程中创建并显示 GUI
        SwingUtilities.invokeLater(() -> {
            PersonalFinanceAppGUI app = new PersonalFinanceAppGUI();
            app.setVisible(true);
        });
    }
}