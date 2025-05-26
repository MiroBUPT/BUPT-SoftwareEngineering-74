package boundary;

import control.UserManager;
import control.SavingManager;
import entity.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Registration interface for new user account creation.
 * Provides fields for username and password input with validation.
 */
public class RegisterView extends JFrame {
    /** Text field for entering username */
    private JTextField userNameField;
    /** Password field for entering password */
    private JPasswordField passwordField;
    /** Button for submitting registration */
    private JButton registerButton;
    /** Label for displaying registration status messages */
    private JLabel messageLabel;

    /**
     * Constructs a new RegisterView.
     * Initializes the registration interface and sets up event handlers.
     */
    public RegisterView() {
        setTitle("Register");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);
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

        // 创建注册按钮
        registerButton = new JButton("Register");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        add(registerButton, gbc);

        // 创建消息标签
        messageLabel = new JLabel("");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(messageLabel, gbc);

        // 添加注册按钮的事件监听器
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userName = userNameField.getText().trim();
                String password = new String(passwordField.getPassword());
                UserManager userManager = UserManager.getInstance();

                // 检查用户名是否已存在
                if (userManager.getUserIdByName(userName) != null) {
                    messageLabel.setText("User name already exists.");
                } else {
                    // 创建新用户
                    String userId = String.valueOf(userManager.getUserList().size() + 1); // 用户ID生成逻辑
                    User newUser = new User();
                    newUser.userId = userId;
                    newUser.name = userName;
                    newUser.password = password;

                    // 添加新用户到用户列表
                    if (userManager.addUser(newUser)) {
                        SavingManager savingManager = SavingManager.getInstance();
                        savingManager.saveUsersToCSV();
                        messageLabel.setText("Registration successful! Go to login page in 3 seconds.");

                        // 延迟 3 秒后关闭注册页面并打开登录页面
                        Timer timer = new Timer(3000, new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                dispose(); // 关闭注册页面
                                new LoginView().setVisible(true); // 打开登录页面
                            }
                        });
                        timer.setRepeats(false); // 设置 Timer 只触发一次
                        timer.start();
                    } else {
                        messageLabel.setText("新用户添加失败，用户 ID 已存在");
                    }
                }
            }
        });

        // 确保注册窗口可见
        setVisible(true);
    }
}