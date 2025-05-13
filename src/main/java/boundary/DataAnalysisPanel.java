package boundary;

import javax.swing.*;
import java.awt.*;
import java.awt.Color;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.plot.PlotOrientation;

import control.UserManager;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataAnalysisPanel extends JPanel {
    // 设置为绝对路径
    private static final String CSV_FILE_PATH = "src/main/resources/transaction.csv";
    private String currentUsername;

    public DataAnalysisPanel(Color borderColor, Color fillColor) {
        setBorder(BorderFactory.createLineBorder(borderColor));
        setBackground(fillColor);
        setLayout(new GridLayout(2, 1)); // 主面板保持两行一列
        
        // 获取当前用户信息
        UserManager userManager = UserManager.getInstance();
        String currentUserId = userManager.getCurrentUserId();
        this.currentUsername = userManager.getUserName(currentUserId);
        
        init();
    }

    private void init() {
        System.out.println("数据统计分析面板初始化");

        // 读取并解析 CSV 文件
        List<Transaction> transactions = readTransactionsFromCSV();

        // 创建支出排名表格
        String[] columnNames = {"Rank", "Description", "Amount"};
        Object[][] data = getTopSpending(transactions);

        JTable table = new JTable(data, columnNames);
        JScrollPane tableScrollPane = new JScrollPane(table);

        // 创建表格标题
        JLabel tableTitle = new JLabel("Outcome ranking for " + currentUsername);
        tableTitle.setHorizontalAlignment(JLabel.CENTER);

        // 创建一个面板来放置标题和表格
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        tablePanel.add(tableTitle, BorderLayout.NORTH);
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);

        // 添加表格面板到主面板
        add(tablePanel);

        // 创建不同月份的收入和支出对比图
        DefaultCategoryDataset incomeDataset = createMonthlyIncomeDataset(transactions);
        DefaultCategoryDataset expenseDataset = createMonthlyExpenseDataset(transactions);

        JFreeChart incomeChart = ChartFactory.createBarChart(
                "Monthly income for " + currentUsername, "Month", "Amount", incomeDataset,
                PlotOrientation.VERTICAL, true, true, false);

        JFreeChart expenseChart = ChartFactory.createBarChart(
                "Monthly outcome for " + currentUsername, "Month", "Amount", expenseDataset,
                PlotOrientation.VERTICAL, true, true, false);

        // 创建图表面板
        ChartPanel incomeChartPanel = new ChartPanel(incomeChart);
        ChartPanel expenseChartPanel = new ChartPanel(expenseChart);

        // 创建一个面板来放置两个图表，并列显示
        JPanel chartsPanel = new JPanel();
        chartsPanel.setLayout(new GridLayout(1, 2)); // 修改为一行两列
        chartsPanel.add(incomeChartPanel);
        chartsPanel.add(expenseChartPanel);

        // 添加图表面板到主面板
        add(chartsPanel);
    }

    private List<Transaction> readTransactionsFromCSV() {
        List<Transaction> transactions = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;
            boolean isFirstLine = true; // 用于跳过表头
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false; // 跳过表头
                    continue;
                }
                String[] values = line.split(",");
                if (values.length == 8) {
                    // 只添加当前用户的交易记录
                    if (values[5].equals(currentUsername)) {
                        transactions.add(new Transaction(
                                values[0], values[1], Double.parseDouble(values[2]),
                                values[3], values[4], values[5],
                                Boolean.parseBoolean(values[6]), values[7]
                        ));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    private Object[][] getTopSpending(List<Transaction> transactions) {
        // 只统计支出
        transactions.removeIf(t -> t.isIncome());
        transactions.sort((t1, t2) -> Double.compare(t2.getAmount(), t1.getAmount()));
        Object[][] data = new Object[Math.min(7, transactions.size())][3];
        for (int i = 0; i < data.length; i++) {
            Transaction t = transactions.get(i);
            data[i][0] = i + 1;
            data[i][1] = t.getDescription();
            data[i][2] = String.format("%.2f", t.getAmount());
        }
        return data;
    }

    private DefaultCategoryDataset createMonthlyIncomeDataset(List<Transaction> transactions) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Transaction t : transactions) {
            if (t.isIncome()) {
                String month = t.getDate().substring(0, 7);
                dataset.addValue(t.getAmount(), "Income", month);
            }
        }
        return dataset;
    }

    private DefaultCategoryDataset createMonthlyExpenseDataset(List<Transaction> transactions) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Transaction t : transactions) {
            if (!t.isIncome()) {
                String month = t.getDate().substring(0, 7);
                dataset.addValue(t.getAmount(), "Expense", month);
            }
        }
        return dataset;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("数据统计分析面板");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new DataAnalysisPanel(Color.BLACK, Color.WHITE));
        frame.pack();
        frame.setSize(800, 600);
        frame.setVisible(true);
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

        public double getAmount() {
            return amount;
        }

        public String getDate() {
            return date;
        }

        public boolean isIncome() {
            return isIncome;
        }

        public String getDescription() {
            return description;
        }
    }
}