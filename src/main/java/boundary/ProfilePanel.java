package boundary;

import control.UserManager;
import entity.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProfilePanel extends JPanel {
    private JTextField usernameField;
    private JTextField userIdField;
    private JTextField oldpasswordField;
    private JTextField newpasswordField;
    private JTextField confirmpasswordField;
    private JButton resetButton;
    private JButton saveButton;
    private JButton editUsernameButton;
    private JButton editUserIdButton;
    private JButton editPasswordButton;


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
        gbc.gridx = 2;
        gbc.gridy = 0;
        editUsernameButton = new JButton("Edit");
        add(editUsernameButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("User ID:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        userIdField = new JTextField(currentUserId, 20);
        userIdField.setEditable(false);
        add(userIdField, gbc);
        gbc.gridx = 2;
        gbc.gridy = 1;
        editUserIdButton = new JButton("Edit");
        add(editUserIdButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Please enter your old password:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        oldpasswordField = new JTextField("2021-06-30", 20);
        oldpasswordField.setEditable(false);
        add(oldpasswordField, gbc);
        gbc.gridx = 2;
        gbc.gridy = 2;
        editPasswordButton = new JButton("Edit");
        add(editPasswordButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Please enter your new password:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 3;
        newpasswordField = new JTextField("authenticated", 20);
        newpasswordField.setEditable(false);
        add(newpasswordField, gbc);
        gbc.gridx = 2;
        gbc.gridy = 3;


        gbc.gridx = 0;
        gbc.gridy = 4;
        add(new JLabel("Please confirm your new password:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 4;
        confirmpasswordField = new JTextField("2489372598", 20);
        confirmpasswordField.setEditable(false);
        add(confirmpasswordField, gbc);
        gbc.gridx = 2;
        gbc.gridy = 4;


        // 添加操作按钮
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        resetButton = new JButton("Reset");
        add(resetButton, gbc);

        gbc.gridy = 6;
        saveButton = new JButton("Save");
        add(saveButton, gbc);

        // 添加按钮事件监听器
        editUsernameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                usernameField.setEditable(true);
                usernameField.requestFocus();
            }
        });

        editUserIdButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userIdField.setEditable(true);
                userIdField.requestFocus();
            }
        });

        editPasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newpasswordField.setEditable(true);
                newpasswordField.requestFocus();
                confirmpasswordField.setEditable(true);
                confirmpasswordField.requestFocus();
                oldpasswordField.setEditable(true);
                oldpasswordField.requestFocus();
            }
        });


        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 清空用户信息并使信息框可以输入
                usernameField.setText("");
                userIdField.setText("");
                oldpasswordField.setText("");
                newpasswordField.setText("");
                confirmpasswordField.setText("");
                usernameField.setEditable(true);
                userIdField.setEditable(true);
                oldpasswordField.setEditable(true);
                newpasswordField.setEditable(true);
                confirmpasswordField.setEditable(true);
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 保存并显示修改后的信息
                String username = usernameField.getText();
                String accountId = userIdField.getText();
                String registrationDate = oldpasswordField.getText();
                String realNameAuth = newpasswordField.getText();
                String phoneNumber = confirmpasswordField.getText();

                // 打印保存的信息（可以根据需要进行其他操作，如更新数据库）
                System.out.println("Saved Profile Information:");
                System.out.println("Username: " + username);
                System.out.println("Account ID: " + accountId);
                System.out.println("Registration Date: " + registrationDate);
                System.out.println("Real-name Authentication: " + realNameAuth);
                System.out.println("Phone number: " + phoneNumber);

                // 使信息框不可编辑
                usernameField.setEditable(false);
                userIdField.setEditable(false);
                oldpasswordField.setEditable(false);
                newpasswordField.setEditable(false);
                confirmpasswordField.setEditable(false);
            }
        });
    }
}