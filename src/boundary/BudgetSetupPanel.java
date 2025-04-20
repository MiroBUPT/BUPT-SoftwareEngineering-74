package boundary;
import javax.swing.*;
import java.awt.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BudgetSetupPanel extends JPanel {
    private JComboBox<String> categoryComboBox;
    private JTextField budgetField;
    private JButton saveButton;

    public BudgetSetupPanel(java.awt.Color borderColor, Color fillColor) {
        // 设置边框颜色和填充颜色
        this.setBorder(BorderFactory.createLineBorder(borderColor));
        this.setBackground(fillColor);
        this.setLayout(new GridBagLayout()); // 使用 GridBagLayout 管理布局

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20); // 增加间距，单位是像素
        gbc.anchor = GridBagConstraints.WEST;

        // 添加分类选择下拉菜单
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(new JLabel("Category:", SwingConstants.RIGHT), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        categoryComboBox = new JComboBox<>(new String[]{"Food", "Entertainment", "Shopping", "Utilities"});
        add(categoryComboBox, gbc);

        // 添加预算输入字段
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(new JLabel("Budget:", SwingConstants.RIGHT), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        budgetField = new JTextField(20);
        add(budgetField, gbc);

        // 添加保存按钮
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        saveButton = new JButton("Save");
        add(saveButton, gbc);

        // 添加按钮事件监听器
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 获取选择的分类和输入的预算
                String category = (String) categoryComboBox.getSelectedItem();
                String budget = budgetField.getText();

                // 输出到控制台
                System.out.println("Saved Budget Information:");
                System.out.println("Category: " + category);
                System.out.println("Budget: " + budget);

                // 清空输入字段
                budgetField.setText("");
            }
        });
    }

    public void init() {
        System.out.println("预算设置面板初始化");
    }

    public void updateData() {
        System.out.println("预算设置面板数据更新");
    }
}
