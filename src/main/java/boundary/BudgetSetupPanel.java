package boundary;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import control.BudgetManager;
import control.UserManager;
import control.SavingManager;
import entity.Budget;
import entity.TransactionType;
import entity.User;
import java.util.*;

public class BudgetSetupPanel extends JPanel {
    private static final String[] TRANSACTION_TYPES = {"income", "health", "food", "rent", "groceries",
            "transportation", "entertainment", "cosmetics",
            "education", "game", "digitalProduct", "travel"};

    private BudgetManager budgetManager;
    private UserManager userManager;
    private String currentUsername;
    private User currentUser;

    private Map<String, JTextField> amountFields;
    private Map<String, JButton> editButtons;
    private Map<String, String> originalAmounts;

    public BudgetSetupPanel(Color borderColor, Color fillColor) {
        this.setBorder(BorderFactory.createLineBorder(borderColor));
        this.setBackground(fillColor);
        this.setLayout(new BorderLayout());

        budgetManager = BudgetManager.getInstance();
        userManager = UserManager.getInstance();
        String currentUserId = userManager.getCurrentUserId();
        this.currentUsername = userManager.getUserName(currentUserId);
        this.currentUser = userManager.getUserById(currentUserId);

        amountFields = new HashMap<>();
        editButtons = new HashMap<>();
        originalAmounts = new HashMap<>();

        // 创建一个滚动面板来容纳所有预算框
        JScrollPane scrollPane = new JScrollPane(createBudgetGrid(fillColor));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        // 设置滚动面板的首选大小
        scrollPane.setPreferredSize(new Dimension(800, 600));
        
        this.add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createBudgetGrid(Color fillColor) {
        // 创建一个面板来容纳所有预算框
        JPanel gridPanel = new JPanel(new GridLayout(0, 3, 10, 10)); // 3列，行数自动调整
        gridPanel.setBackground(fillColor);
        gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 获取当前月份
        Calendar cal = Calendar.getInstance();
        String currentMonth = String.format("%04d-%02d", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1);
        java.util.List<Budget> budgets = budgetManager.getBudgetList();
        Map<String, Budget> currentMonthBudgets = new HashMap<>();
        for (Budget budget : budgets) {
            if (budget.owner.name.equals(currentUsername) && budget.date.startsWith(currentMonth)) {
                currentMonthBudgets.put(budget.type.name(), budget);
            }
        }

        // 为每个预算类型创建一个框
        for (String type : TRANSACTION_TYPES) {
            JPanel budgetBox = createBudgetBox(type, currentMonthBudgets.get(type), fillColor);
            gridPanel.add(budgetBox);
        }

        return gridPanel;
    }

    private JPanel createBudgetBox(String type, Budget budget, Color fillColor) {
        // 创建预算框
        JPanel box = new JPanel();
        box.setLayout(new BorderLayout(5, 5));
        box.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        box.setBackground(fillColor);

        // 标题
        JLabel titleLabel = new JLabel(type.toUpperCase());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        box.add(titleLabel, BorderLayout.NORTH);

        // 金额输入区域
        JPanel amountPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        amountPanel.setBackground(fillColor);
        String amount = budget != null ? budget.amount : "0.00";
        JTextField amountField = new JTextField(amount, 10);
        amountField.setEditable(false);
        JButton editButton = new JButton("Edit");
        
        originalAmounts.put(type, amount);
        amountFields.put(type, amountField);
        editButtons.put(type, editButton);

        amountPanel.add(new JLabel("Amount: $"));
        amountPanel.add(amountField);
        amountPanel.add(editButton);
        box.add(amountPanel, BorderLayout.CENTER);

        // 添加编辑按钮的事件监听器
        editButton.addActionListener(new ActionListener() {
            private boolean editing = false;
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!editing) {
                    amountField.setEditable(true);
                    editButton.setText("Save");
                    editing = true;
                } else {
                    String newAmount = amountField.getText();
                    try {
                        double value = Double.parseDouble(newAmount);
                        if (value < 0) throw new NumberFormatException();
                        
                        // 保存到 budget.csv
                        Calendar cal = Calendar.getInstance();
                        String currentMonth = String.format("%04d-%02d", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1);
                        Budget newBudget = new Budget();
                        newBudget.budgetId = String.format("%04d", budgetManager.getBudgetList().size() + 1);
                        newBudget.amount = String.format("%.2f", value);
                        try {
                            newBudget.type = TransactionType.valueOf(type);
                        } catch (IllegalArgumentException ex) {
                            try {
                                int idx = Integer.parseInt(type);
                                newBudget.type = TransactionType.values()[idx];
                            } catch (Exception e2) {
                                JOptionPane.showMessageDialog(BudgetSetupPanel.this, "类型字段无效！", "错误", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                        }
                        newBudget.owner = currentUser;
                        newBudget.date = currentMonth + "-01";
                        budgetManager.addBudget(newBudget);
                        SavingManager.getInstance().saveBudgetsToCSV();
                        
                        amountField.setEditable(false);
                        editButton.setText("Edit");
                        originalAmounts.put(type, newAmount);
                        editing = false;
                        
                        JOptionPane.showMessageDialog(BudgetSetupPanel.this, 
                            "Budget updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(BudgetSetupPanel.this, 
                            "请输入有效的非负数字！", "输入错误", JOptionPane.ERROR_MESSAGE);
                        amountField.setText(originalAmounts.get(type));
                    }
                }
            }
        });

        return box;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Budget Setup Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new BudgetSetupPanel(Color.BLACK, Color.WHITE));
        frame.pack();
        frame.setSize(900, 700);
        frame.setVisible(true);
    }
}