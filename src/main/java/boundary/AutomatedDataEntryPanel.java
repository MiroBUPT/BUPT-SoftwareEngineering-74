package boundary;

import java.awt.*;
import java.awt.Color;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import control.TransactionManager;
import control.SavingManager; // Import SavingManager if needed for saving after import

/**
 * Panel for automated import of transaction data from CSV files.
 * Provides file selection and import functionality with progress feedback.
 */
public class AutomatedDataEntryPanel extends JPanel {
    /** Button for initiating file upload */
    private JButton uploadButton;
    /** Label for displaying import status */
    private JLabel statusLabel;

    /**
     * Constructs a new AutomatedDataEntryPanel with specified colors.
     * @param borderColor The color for the panel's border
     * @param fillColor The background color for the panel
     */
    public AutomatedDataEntryPanel(java.awt.Color borderColor, java.awt.Color fillColor) {
        setBorder(BorderFactory.createLineBorder(borderColor));
        setBackground(fillColor);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH; // 填充单元格
        gbc.insets = new Insets(10, 10, 10, 10); // 设置组件的边距

        // 创建组件
        JLabel titleLabel = new JLabel("Automated Data Entry", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0; // 不扩展
        gbc.weighty = 0; // 不扩展
        gbc.anchor = GridBagConstraints.CENTER; // 居中
        add(titleLabel, gbc);

        // 创建上传按钮并居中
        uploadButton = new JButton("Upload CSV");
        uploadButton.setPreferredSize(new Dimension(150, 30)); // 设置按钮的宽度和高度
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0; // 不扩展
        gbc.weighty = 0; // 不扩展
        gbc.anchor = GridBagConstraints.CENTER; // 居中
        add(uploadButton, gbc);

        // 创建状态标签
        statusLabel = new JLabel("Select a CSV file to import.", SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0; // 不扩展
        gbc.weighty = 0; // 不扩展
        gbc.anchor = GridBagConstraints.CENTER; // 居中
        add(statusLabel, gbc);

        // 添加按钮事件监听器
        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                uploadFile();
            }
        });
    }

    /**
     * Handles the file upload process.
     * Opens a file chooser and processes the selected CSV file.
     */
    private void uploadFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select CSV File");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if (selectedFile.length() <= 1024 * 1024 * 1024) { // 检查文件大小是否不超过1GB
                System.out.println("File selected: " + selectedFile.getAbsolutePath());
                statusLabel.setText("Importing '" + selectedFile.getName() + "'...");
                uploadButton.setEnabled(false); // Disable button during import

                // Use SwingWorker for background import
                SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        TransactionManager.getInstance().importFromCSV(selectedFile.getAbsolutePath());
                        return null;
                    }

                    @Override
                    protected void done() {
                        uploadButton.setEnabled(true); // Re-enable button
                        try {
                            get(); // Check for exceptions
                            statusLabel.setText("Import successful!");
                            JOptionPane.showMessageDialog(AutomatedDataEntryPanel.this, 
                                    "Data imported successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        } catch (Exception ex) {
                            statusLabel.setText("Import failed.");
                            JOptionPane.showMessageDialog(AutomatedDataEntryPanel.this, 
                                    "Error importing data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                            ex.printStackTrace();
                        }
                    }
                };

                worker.execute(); // Start the background worker

            } else {
                JOptionPane.showMessageDialog(this, "File size exceeds 1GB limit.", "Error", JOptionPane.ERROR_MESSAGE);
                statusLabel.setText("File too large.");
            }
        }
    }

    /**
     * Main method for testing the AutomatedDataEntryPanel.
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame("Automated Data Entry");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new AutomatedDataEntryPanel(java.awt.Color.ORANGE, Color.LIGHT_GRAY));
        frame.setSize(400, 300);
        frame.setVisible(true);
        frame.setResizable(true); // 允许窗口大小改变
    }
}