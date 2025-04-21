package boundary;

import java.awt.*;
import java.awt.Color;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class AutomatedDataEntryPanel extends JPanel {
    private JButton uploadButton;

    public AutomatedDataEntryPanel(java.awt.Color borderColor, java.awt.Color fillColor) {
        setBorder(BorderFactory.createLineBorder(borderColor));
        setBackground(fillColor);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH; // 填充单元格
        gbc.insets = new Insets(10, 10, 10, 10); // 设置组件的边距
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1; // 水平方向上的权重
        gbc.weighty = 1; // 垂直方向上的权重

        // 创建组件
        JLabel titleLabel = new JLabel("Automated Data Entry", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0; // 不扩展
        gbc.weighty = 0; // 不扩展
        add(titleLabel, gbc);

        // 创建上传按钮并居中，设置较短的宽度
        uploadButton = new JButton("Upload");
        uploadButton.setPreferredSize(new Dimension(100, 30)); // 设置按钮的宽度和高度
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0; // 不扩展
        gbc.weighty = 0; // 不扩展
        add(uploadButton, gbc);

        // 添加按钮事件监听器
        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                uploadFile();
            }
        });
    }

    private void uploadFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select CSV File");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if (selectedFile.length() <= 1024 * 1024 * 1024) { // 检查文件大小是否不超过1GB
                System.out.println("File uploaded successfully: " + selectedFile.getAbsolutePath());
                // 在这里添加处理文件的代码
            } else {
                JOptionPane.showMessageDialog(this, "File size exceeds 1GB limit.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Automated Data Entry");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new AutomatedDataEntryPanel(java.awt.Color.ORANGE, Color.LIGHT_GRAY));
        frame.setSize(400, 300);
        frame.setVisible(true);
        frame.setResizable(true); // 允许窗口大小改变
    }
}