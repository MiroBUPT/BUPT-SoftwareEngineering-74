package boundary;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import control.UserManager;
import control.TransactionManager;
import entity.Transaction;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomePanel extends JPanel {
    private String username;
    private TransactionManager transactionManager;

    public HomePanel(Color borderColor, Color fillColor) {
        UserManager userManager = UserManager.getInstance();
        transactionManager = TransactionManager.getInstance();
        String currentUserId = userManager.getCurrentUserId();
        this.username = userManager.getUserName(currentUserId);

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
        lineChartPanel.setPreferredSize(new Dimension(800, 300));

        // 饼图
        JFreeChart pieChart = createPieChart();
        ChartPanel pieChartPanel = new ChartPanel(pieChart);
        pieChartPanel.setPreferredSize(new Dimension(400, 300));

        // 表格
        JScrollPane tablePanel = createTablePanel();
        tablePanel.setPreferredSize(new Dimension(400, 300));

        // 使用 JSplitPane 来分割饼图和表格
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pieChartPanel, tablePanel);
        splitPane.setDividerLocation(400);

        // 使用 JSplitPane 来分割折线图和饼图/表格
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, lineChartPanel, splitPane);
        mainSplitPane.setDividerLocation(300);

        add(mainSplitPane, BorderLayout.CENTER);
    }

    private JFreeChart createLineChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        List<Transaction> transactions = transactionManager.getTransactionsByUserName(username);
        Map<String, Double> monthlyExpenses = new HashMap<>();

        for (Transaction transaction : transactions) {
            String month = transaction.date.substring(0, 7);
            double amount = Double.parseDouble(transaction.amount);
            monthlyExpenses.put(month, monthlyExpenses.getOrDefault(month, 0.0) + amount);
        }

        for (Map.Entry<String, Double> entry : monthlyExpenses.entrySet()) {
            dataset.addValue(entry.getValue(), "Consumption", entry.getKey());
        }

        return ChartFactory.createLineChart(
                "Consumer Trend (Last 7 days)", "Date", "Amount", dataset,
                PlotOrientation.VERTICAL, true, true, false);
    }

    private JFreeChart createPieChart() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        List<Transaction> transactions = transactionManager.getTransactionsByUserName(username);
        Map<String, Double> typeAmountMap = new HashMap<>();

        for (Transaction transaction : transactions) {
            if (!transaction.isIncome) {
                String type = transaction.type.name();
                double amount = Double.parseDouble(transaction.amount);
                typeAmountMap.put(type, typeAmountMap.getOrDefault(type, 0.0) + amount);
            }
        }

        for (Map.Entry<String, Double> entry : typeAmountMap.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }

        return ChartFactory.createPieChart(
                "Consumer Structure (Last 7 days)", dataset, true, true, false);
    }

    private JScrollPane createTablePanel() {
        String[] columnNames = {"Date", "Description", "Amount", "Owner"};
        List<Transaction> transactions = transactionManager.getTransactionsByUserName(username);
        Object[][] data = new Object[transactions.size()][4];

        int index = 0;
        for (Transaction transaction : transactions) {
            data[index][0] = transaction.date.substring(5, 10);
            data[index][1] = transaction.description;
            data[index][2] = String.format("%.2f", Double.parseDouble(transaction.amount)) + " €";
            data[index][3] = transaction.owner.name;
            index++;
        }

        JTable table = new JTable(data, columnNames);
        return new JScrollPane(table);
    }
}