package boundary;

import control.SavingManager;
import control.UserManager;
import entity.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProfilePanel extends JPanel {
    private JTextField usernameField;
    private JTextField userIdField;
    private JPasswordField oldpasswordField;
    private JPasswordField newpasswordField;
    private JPasswordField confirmpasswordField;
    private JButton resetButton;
    private JButton saveButton;
    private JButton editUsernameButton;
    private JButton editUserIdButton;
    private JButton editPasswordButton;
    private UserManager userManager; // 添加 UserManager 引用
    private String currentUserId;    // 添加 currentUserId 变量

    public ProfilePanel(java.awt.Color borderColor, java.awt.Color fillColor) {
        // 设置边框颜色和填充颜色
        this.setBorder(BorderFactory.createLineBorder(borderColor));
        this.setBackground(fillColor);
        this.setLayout(new GridBagLayout()); // 使用 GridBagLayout 管理布局

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8); // 添加间距
        gbc.anchor = GridBagConstraints.WEST;

        // 初始化 UserManager 和获取当前用户信息
        userManager = UserManager.getInstance();
        currentUserId = userManager.getCurrentUserId();
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
        add(new JLabel("Old Password:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        oldpasswordField = new JPasswordField(20);
        oldpasswordField.setEditable(false);
        add(oldpasswordField, gbc);
        gbc.gridx = 2;
        gbc.gridy = 2;
        editPasswordButton = new JButton("Edit");
        add(editPasswordButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("New Password:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 3;
        newpasswordField = new JPasswordField(20);
        newpasswordField.setEditable(false);
        add(newpasswordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        add(new JLabel("Confirm New Password:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 4;
        confirmpasswordField = new JPasswordField(20);
        confirmpasswordField.setEditable(false);
        add(confirmpasswordField, gbc);

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
                if (usernameField.isEditable()) {
                    // 保存更改
                    String newUsername = usernameField.getText();
                    if (newUsername.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Username cannot be empty.");
                        return;
                    }
                    if (userManager.getUserIdByName(newUsername) != null && !newUsername.equals(currentUser.name)) {
                        JOptionPane.showMessageDialog(null, "Username already exists.");
                        return;
                    }
                    // 更新用户名
                    if (userManager.editUserName(currentUserId, newUsername)) {
                        SavingManager savingManager = SavingManager.getInstance();
                        savingManager.saveUsersToCSV();
                        JOptionPane.showMessageDialog(null, "Save Successfully!");
                        System.out.println("Username changed to: " + newUsername);
                    } else {
                        System.out.println("Wrong");
                    }
                    usernameField.setEditable(false);
                    editUsernameButton.setText("Edit");
                } else {
                    // 开始编辑
                    usernameField.setEditable(true);
                    usernameField.requestFocus();
                    editUsernameButton.setText("Save");
                }
            }
        });

        editUserIdButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (userIdField.isEditable()) {
                    // 保存更改
                    String newUserId = userIdField.getText();
                    if (newUserId.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "User ID cannot be empty.");
                        return;
                    }
                    if (userManager.getUserById(newUserId) != null && !newUserId.equals(currentUserId)) {
                        JOptionPane.showMessageDialog(null, "User ID already exists.");
                        return;
                    }
                    // 更新用户ID
                    if (userManager.editUserId(currentUserId, newUserId)) {
                        currentUserId = newUserId; // 更新 currentUserId
                        SavingManager savingManager = SavingManager.getInstance();
                        savingManager.saveUsersToCSV();
                        JOptionPane.showMessageDialog(null, "Save Successfully!");
                        System.out.println("User ID changed to: " + newUserId);
                        refreshUserInformation(); // 刷新用户信息
                    } else {
                        System.out.println("Wrong");
                    }
                    userIdField.setEditable(false);
                    editUserIdButton.setText("Edit");
                } else {
                    // 开始编辑
                    userIdField.setEditable(true);
                    userIdField.requestFocus();
                    editUserIdButton.setText("Save");
                }
            }
        });

        editPasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (oldpasswordField.isEditable()) {
                    // 保存更改
                    String oldPassword = new String(oldpasswordField.getPassword());
                    String newPassword = new String(newpasswordField.getPassword());
                    String confirmPassword = new String(confirmpasswordField.getPassword());

                    if (!newPassword.equals(confirmPassword)) {
                        JOptionPane.showMessageDialog(null, "Please confirm that the new password you entered twice is the same");
                        return;
                    }
                    if (!userManager.getPassword(currentUserId).equals(oldPassword)) {
                        JOptionPane.showMessageDialog(null, "Old password is incorrect.");
                        return;
                    }
                    // 更新密码
                    if (userManager.editPassword(currentUserId, newPassword)) {
                        SavingManager savingManager = SavingManager.getInstance();
                        savingManager.saveUsersToCSV();
                        JOptionPane.showMessageDialog(null, "Save Successfully!");
                        System.out.println("Password changed.");
                    } else {
                        System.out.println("Wrong");
                    }
                    oldpasswordField.setEditable(false);
                    newpasswordField.setEditable(false);
                    confirmpasswordField.setEditable(false);
                    editPasswordButton.setText("Edit");
                } else {
                    // 开始编辑
                    oldpasswordField.setEditable(true);
                    newpasswordField.setEditable(true);
                    confirmpasswordField.setEditable(true);
                    oldpasswordField.requestFocus();
                    editPasswordButton.setText("Save");
                }
            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshUserInformation(); // 刷新用户信息
                oldpasswordField.setText("");
                newpasswordField.setText("");
                confirmpasswordField.setText("");
                usernameField.setEditable(false);
                userIdField.setEditable(false);
                oldpasswordField.setEditable(false);
                newpasswordField.setEditable(false);
                confirmpasswordField.setEditable(false);
                editUsernameButton.setText("Edit");
                editUserIdButton.setText("Edit");
                editPasswordButton.setText("Edit");
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String userId = userIdField.getText();
                String oldPassword = new String(oldpasswordField.getPassword());
                String newPassword = new String(newpasswordField.getPassword());
                String confirmPassword = new String(confirmpasswordField.getPassword());

                if (!newPassword.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(null, "New password and confirm password do not match.");
                    return;
                }

                // 更新用户信息（根据需要实现）
                System.out.println("Saved Profile Information:");
                System.out.println("Username: " + username);
                System.out.println("User ID: " + userId);
                System.out.println("Old Password: " + oldPassword);
                System.out.println("New Password: " + newPassword);

                // 使信息框不可编辑
                usernameField.setEditable(false);
                userIdField.setEditable(false);
                oldpasswordField.setEditable(false);
                newpasswordField.setEditable(false);
                confirmpasswordField.setEditable(false);
                editUsernameButton.setText("Edit");
                editUserIdButton.setText("Edit");
                editPasswordButton.setText("Edit");
            }
        });
    }

    /**
     * 刷新当前用户信息
     */
    private void refreshUserInformation() {
        User currentUser = userManager.getUserById(currentUserId);
        if (currentUser != null) {
            usernameField.setText(currentUser.name);
        }
        userIdField.setText(currentUserId);
    }
}