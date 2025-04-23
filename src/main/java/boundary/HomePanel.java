package boundary;




import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import control.UserManager;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomePanel extends JPanel {
    private static final String TRANSACTIONS_FILE_PATH = "src/main/resources/transactions.csv";
    private static final String BUDGET_FILE_PATH = "src/main/resources/budget.csv";

    private String username;

    public HomePanel(Color borderColor, Color fillColor) {
        UserManager userManager = UserManager.getInstance(); // 获取 UserManager 实例
        String currentUserId = userManager.getCurrentUserId(); // 获取当前用户 ID
        this.username = userManager.getUserName(currentUserId); // 获取当前用户名

        setBorder(BorderFactory.createLineBorder(borderColor));
        setBackground(fillColor);
        setLayout(new BorderLayout());
        init();
    }

    private void init() {
        System.out.println("主页面板初始化");

        // 左上角欢迎信息面板
        JPanel welcomePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        welcomePanel.setBackground(Color.WHITE);
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel welcomeLabel = new JLabel("Welcome, " + username);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomePanel.add(welcomeLabel);
        add(welcomePanel, BorderLayout.NORTH);

        // 折线图
        JFreeChart lineChart = createLineChart();
        ChartPanel lineChartPanel = new ChartPanel(lineChart);
        lineChartPanel.setPreferredSize(new Dimension(800, 300)); // 设置折线图高度为 300

        // 饼图
        JFreeChart pieChart = createPieChart();
        ChartPanel pieChartPanel = new ChartPanel(pieChart);
        pieChartPanel.setPreferredSize(new Dimension(400, 300)); // 设置饼图高度为 300

        // 表格
        JScrollPane tablePanel = createTablePanel();
        tablePanel.setPreferredSize(new Dimension(400, 300)); // 设置表格高度为 300

        // 使用 JSplitPane 来分割饼图和表格
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pieChartPanel, tablePanel);
        splitPane.setDividerLocation(400); // 设置分隔线位置

        // 使用 JSplitPane 来分割折线图和饼图/表格
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, lineChartPanel, splitPane);
        mainSplitPane.setDividerLocation(300); // 设置分隔线位置

        add(mainSplitPane, BorderLayout.CENTER);
    }

    private JFreeChart createLineChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        List<Transaction> transactions = readTransactionsFromCSV();
        Map<String, Double> monthlyExpenses = new HashMap<>();

        for (Transaction transaction : transactions) {
            String month = transaction.getDate().substring(0, 7); // 提取年份和月份
            double amount = transaction.getAmount();
            monthlyExpenses.put(month, monthlyExpenses.getOrDefault(month, 0.0) + amount);
        }

        for (Map.Entry<String, Double> entry : monthlyExpenses.entrySet()) {
            dataset.addValue(entry.getValue(), "Consumption", entry.getKey());
        }

        JFreeChart lineChart = ChartFactory.createLineChart(
                "Consumer Trend (Last 7 days)", "Date", "Amount", dataset,
                PlotOrientation.VERTICAL, true, true, false);

        return lineChart;
    }

    private JFreeChart createPieChart() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        List<BudgetItem> budgetItems = readBudgetItemsFromCSV();
        Map<String, Double> typeAmountMap = new HashMap<>();

        for (BudgetItem item : budgetItems) {
            String type = item.getType();
            double amount = item.getAmount();
            typeAmountMap.put(type, typeAmountMap.getOrDefault(type, 0.0) + amount);
        }

        for (Map.Entry<String, Double> entry : typeAmountMap.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }

        JFreeChart pieChart = ChartFactory.createPieChart(
                "Consumer Structure (Last 7 days)", dataset, true, true, false);

        return pieChart;
    }

    private JScrollPane createTablePanel() {
        String[] columnNames = {"Date", "Description", "Amount", "Owner"};
        List<Transaction> transactions = readTransactionsFromCSV();
        Object[][] data = new Object[transactions.size()][4];

        int index = 0;
        for (Transaction transaction : transactions) {
            data[index][0] = transaction.getDate().substring(5, 10); // 显示日期的月-日部分
            data[index][1] = transaction.getDescription();
            data[index][2] = String.format("%.2f", transaction.getAmount()) + " €";
            data[index][3] = transaction.getOwner();
            index++;
        }

        JTable table = new JTable(data, columnNames);
        return new JScrollPane(table);
    }

    private List<Transaction> readTransactionsFromCSV() {
        List<Transaction> transactions = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(TRANSACTIONS_FILE_PATH))) {
            String line;
            boolean isFirstLine = true; // 用于跳过表头
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false; // 跳过表头
                    continue;
                }
                String[] values = line.split(",");
                if (values.length == 8) {
                    transactions.add(new Transaction(
                            values[0], values[1], Double.parseDouble(values[2]),
                            values[3], values[4], values[5],
                            Boolean.parseBoolean(values[6]), values[7]
                    ));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    private List<BudgetItem> readBudgetItemsFromCSV() {
        List<BudgetItem> budgetItems = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(BUDGET_FILE_PATH))) {
            String line;
            boolean isFirstLine = true; // 用于跳过表头
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false; // 跳过表头
                    continue;
                }
                String[] values = line.split(",");
                if (values.length == 5) {
                    budgetItems.add(new BudgetItem(
                            values[0], Double.parseDouble(values[1]),
                            values[2], values[3], values[4]
                    ));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return budgetItems;
    }

    private static class Transaction {
        private String transactionId;
        private String date;
        private double amount;
        private String description;
        private String type;
        private String owner;
        private boolean isIncome;
        private String location;

        public Transaction(String transactionId, String date, double amount,
                           String description, String type, String owner, boolean isIncome, String location) {
            this.transactionId = transactionId;
            this.date = date;
            this.amount = amount;
            this.description = description;
            this.type = type;
            this.owner = owner;
            this.isIncome = isIncome;
            this.location = location;
        }

        public String getDate() {
            return date;
        }

        public String getDescription() {
            return description;
        }

        public double getAmount() {
            return amount;
        }

        public String getOwner() {
            return owner;
        }
    }

    private static class BudgetItem {
        private String budgetId;
        private double amount;
        private String type;
        private String owner;
        private String date;

        public BudgetItem(String budgetId, double amount, String type, String owner, String date) {
            this.budgetId = budgetId;
            this.amount = amount;
            this.type = type;
            this.owner = owner;
            this.date = date;
        }

        public double getAmount() {
            return amount;
        }

        public String getType() {
            return type;
        }

        public String getOwner() {
            return owner;
        }

        public String getDate() {
            return date;
        }
    }
}