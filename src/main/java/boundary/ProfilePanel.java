package boundary;

import javax.swing.*;

import control.UserManager;
import entity.User;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProfilePanel extends JPanel {
    private JTextField usernameField;
    private JTextField accountIdField;
    private JTextField registrationDateField;
    private JTextField realNameAuthField;
    private JTextField phoneNumberField;
    private JButton resetButton;
    private JButton saveButton;

    public ProfilePanel(java.awt.Color borderColor, java.awt.Color fillColor) {
        // 设置边框颜色和填充颜色
        this.setBorder(BorderFactory.createLineBorder(borderColor));
        this.setBackground(fillColor);
        this.setLayout(new GridBagLayout()); // 使用 GridBagLayout 管理布局

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8); // 添加间距
        gbc.anchor = GridBagConstraints.WEST;


        // 获取当前用户信息
        UserManager userManager = UserManager.getInstance();
        String currentUserId = userManager.getCurrentUserId();
        User currentUser = userManager.getUserById(currentUserId);

        // 添加用户信息
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        usernameField = new JTextField(currentUser != null ? currentUser.name : "", 20);
        usernameField.setEditable(false);
        add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("User ID:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        accountIdField = new JTextField(currentUserId, 20);
        accountIdField.setEditable(false);
        add(accountIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Please enter your old password:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        registrationDateField = new JTextField("2021-06-30", 20);
        add(registrationDateField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Please enter new password:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 3;
        realNameAuthField = new JTextField("authenticated", 20);
        add(realNameAuthField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        add(new JLabel("Please confirm your new password:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 4;
        phoneNumberField = new JTextField("2489372598", 20);
        add(phoneNumberField, gbc);

        // 添加操作按钮
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        resetButton = new JButton("Reset");
        add(resetButton, gbc);

        gbc.gridy = 6;
        saveButton = new JButton("Save");
        add(saveButton, gbc);

        // 添加按钮事件监听器
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 清空用户信息并使信息框可以输入
                usernameField.setText("");
                accountIdField.setText("");
                registrationDateField.setText("");
                realNameAuthField.setText("");
                phoneNumberField.setText("");
                usernameField.setEditable(true);
                accountIdField.setEditable(true);
                registrationDateField.setEditable(true);
                realNameAuthField.setEditable(true);
                phoneNumberField.setEditable(true);
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 保存并显示修改后的信息
                String username = usernameField.getText();
                String accountId = accountIdField.getText();
                String registrationDate = registrationDateField.getText();
                String realNameAuth = realNameAuthField.getText();
                String phoneNumber = phoneNumberField.getText();

                // 打印保存的信息（可以根据需要进行其他操作，如更新数据库）
                System.out.println("Saved Profile Information:");
                System.out.println("Username: " + username);
                System.out.println("Account ID: " + accountId);
                System.out.println("Registration Date: " + registrationDate);
                System.out.println("Real-name Authentication: " + realNameAuth);
                System.out.println("Phone number: " + phoneNumber);

                // 使信息框不可编辑
                usernameField.setEditable(false);
                accountIdField.setEditable(false);
                registrationDateField.setEditable(false);
                realNameAuthField.setEditable(false);
                phoneNumberField.setEditable(false);
            }
        });
    }
}