package boundary;

import control.UserManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginView extends JFrame {
    private JTextField userNameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel messageLabel;
    private JButton registerButton;

    public LoginView() {
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 250);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // 创建用户名标签和输入框
        JLabel userLabel = new JLabel("User Name:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(userLabel, gbc);

        userNameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(userNameField, gbc);

        // 创建密码标签和输入框
        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(passwordLabel, gbc);

        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(passwordField, gbc);

        // 创建登录按钮
        loginButton = new JButton("Login");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        add(loginButton, gbc);

        // 创建注册按钮
        registerButton = new JButton("Register");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(registerButton, gbc);

        // 创建消息标签
        messageLabel = new JLabel("");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(messageLabel, gbc);

        // 添加登录按钮的事件监听器
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userName = userNameField.getText();
                String inputPassword = new String(passwordField.getPassword());
                UserManager userManager = UserManager.getInstance();
                String storedPassword = userManager.getPasswordByName(userName);

                // // 调试信息：打印输入的用户名和密码
                // System.out.println("Input User Name: " + userName);
                // System.out.println("Input Password: " + inputPassword);

                // // 调试信息：打印存储的密码
                // System.out.println("Stored Password: " + storedPassword);

                if (storedPassword != null && storedPassword.equals(inputPassword)) {
                    String userId = userManager.getUserIdByName(userName);
                    if (userId != null) {
                        userManager.setCurrentUser(userId); // 设置当前用户ID
                        System.out.println("Login Successful. User ID: " + userId);
                        dispose(); // 关闭登录窗口
                        new GUIView(); // 打开主界面
                    } else {
                        messageLabel.setText("User ID not found."); // 显示错误信息
                        System.out.println("User ID not found for user: " + userName);
                    }
                } else {
                    messageLabel.setText("Invalid user name or password."); // 显示错误信息
                    System.out.println("Invalid user name or password.");
                }
            }
        });


        // 添加注册按钮的事件监听器
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 跳转到注册页面
                dispose(); // 关闭登录窗口
                new RegisterView(); // 打开注册页面
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView();
            loginView.setVisible(true);
        });
    }
}