package boundary;

import java.awt.*;
import java.awt.Color;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class ManualDataEntryPanel extends JPanel {
    private JTextField amountField;
    private JComboBox<String> typeComboBox;
    private JTextField timeField;
    private JTextField productNameField;
    private JTextArea descriptionArea;
    private JButton saveButton;

    public ManualDataEntryPanel(java.awt.Color borderColor, java.awt.Color fillColor) {
        setBorder(new CompoundBorder(new TitledBorder("Manual Data Entry"), new EmptyBorder(10, 10, 10, 10)));
        setBackground(fillColor);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // 创建组件
        JLabel amountLabel = new JLabel("Amount:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(amountLabel, gbc);

        amountField = new JTextField(10);
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(amountField, gbc);

        JLabel typeLabel = new JLabel("Type:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(typeLabel, gbc);

        typeComboBox = new JComboBox<>(new String[]{"Type1", "Type2", "Type3"});
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(typeComboBox, gbc);

        JLabel timeLabel = new JLabel("Time:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(timeLabel, gbc);

        timeField = new JTextField(10);
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(timeField, gbc);

        JLabel productNameLabel = new JLabel("Product Name:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(productNameLabel, gbc);

        productNameField = new JTextField(10);
        gbc.gridx = 1;
        gbc.gridy = 3;
        add(productNameField, gbc);

        JLabel descriptionLabel = new JLabel("Description:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(descriptionLabel, gbc);

        descriptionArea = new JTextArea(3, 20);
        descriptionArea.setLineWrap(true);
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        add(new JScrollPane(descriptionArea), gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(new JLabel(), gbc);

        saveButton = new JButton("Save");
        gbc.gridx = 1;
        gbc.gridy = 6;
        add(saveButton, gbc);

        // 添加按钮事件监听器
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveData();
            }
        });
    }

    private void saveData() {
        String amount = amountField.getText();
        String type = (String) typeComboBox.getSelectedItem();
        String time = timeField.getText();
        String productName = productNameField.getText();
        String description = descriptionArea.getText();

        // 输出到控制台
        System.out.println("Amount: " + amount);
        System.out.println("Type: " + type);
        System.out.println("Time: " + time);
        System.out.println("Product Name: " + productName);
        System.out.println("Description: " + description);
        System.out.println("Data saved successfully.");
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Manual Data Entry");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new ManualDataEntryPanel(java.awt.Color.MAGENTA, Color.LIGHT_GRAY));
        frame.pack();
        frame.setSize(400, 300);
        frame.setVisible(true);
    }
}