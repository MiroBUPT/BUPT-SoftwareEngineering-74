import main.java.boundary.GUIView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoginView extends JFrame {
    private JTextField userNameField;
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

        JLabel userLabel = new JLabel("User Name:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(userLabel, gbc);

        userNameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(userNameField, gbc);

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
                String userName = userNameField.getText();
                String password = new String(passwordField.getPassword());
                if (validateLogin(userName, password)) {
                    control.UserManager.getInstance().setCurrentUser(userName);
                    dispose(); // Close the login window
                    new GUIView(); // Open the GUI view
                } else {
                    messageLabel.setText("Invalid user name or password.");
                }
            }
        });
    }

    private boolean validateLogin(String userName, String password) {
        List<User> users = loadUsers();
        for (User user : users) {
            if (user.getUserName().equals(userName) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    private List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        String filePath = "src/main/resources/user.csv"; // 使用相对路径读取 CSV 文件
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length == 3) {
                    String userId = values[0];
                    String userName = values[1];
                    String pwd = values[2];
                    users.add(new User(userId, userName, pwd));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView();
            loginView.setVisible(true);
        });
    }

    static class User {
        private String userId;
        private String userName;
        private String password;

        public User(String userId, String userName, String password) {
            this.userId = userId;
            this.userName = userName;
            this.password = password;
        }

        public String getUserId() {
            return userId;
        }

        public String getUserName() {
            return userName;
        }

        public String getPassword() {
            return password;
        }
    }
}