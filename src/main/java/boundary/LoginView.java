package boundary;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import control.UserManager;

public class LoginView extends JFrame {
    private JTextField userIdField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel messageLabel;

    public LoginView() {
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel userIdLabel = new JLabel("User ID:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(userIdLabel, gbc);

        userIdField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(userIdField, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(passwordLabel, gbc);

        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(passwordField, gbc);

        loginButton = new JButton("Login");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        add(loginButton, gbc);

        messageLabel = new JLabel("");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(messageLabel, gbc);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userId = userIdField.getText();
                String password = new String(passwordField.getPassword());
                if (validateLogin(userId, password)) {
                    UserManager.getInstance().setCurrentUser(userId);
                    dispose();
                    GUIView.main(null);
                } else {
                    messageLabel.setText("Invalid user ID or password.");
                }
            }
        });
    }

    private boolean validateLogin(String userId, String password) {
        UserManager userManager = UserManager.getInstance();
        var user = userManager.getUserById(userId);
        return user != null && user.password.equals(password);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView();
            loginView.setVisible(true);
        });
    }
}