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
import control.TransactionManager;
import entity.Transaction;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

/**
 * Panel for analyzing financial data with visualizations.
 * Displays spending rankings and monthly income/expense comparisons.
 */
public class DataAnalysisPanel extends JPanel {
    /** Username of the current user */
    private String currentUsername;
    /** Manager for transaction-related operations */
    private TransactionManager transactionManager;

    /**
     * Constructs a new DataAnalysisPanel with specified colors.
     * @param borderColor The color for the panel's border
     * @param fillColor The background color for the panel
     */
    public DataAnalysisPanel(Color borderColor, Color fillColor) {
        setBorder(BorderFactory.createLineBorder(borderColor));
        setBackground(fillColor);
        setLayout(new GridLayout(2, 1)); // 主面板保持两行一列
        
        // 获取当前用户信息和管理器实例
        UserManager userManager = UserManager.getInstance();
        transactionManager = TransactionManager.getInstance();
        String currentUserId = userManager.getCurrentUserId();
        this.currentUsername = userManager.getUserName(currentUserId);
        
        init();
    }

    /**
     * Initializes the panel components and layout.
     * Creates spending ranking table and income/expense comparison charts.
     */
    private void init() {
        System.out.println("数据统计分析面板初始化");

        // 获取交易数据
        List<Transaction> transactions = transactionManager.getTransactionsByUserName(currentUsername);

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
        chartsPanel.setLayout(new GridLayout(1, 2));
        chartsPanel.add(incomeChartPanel);
        chartsPanel.add(expenseChartPanel);

        // 添加图表面板到主面板
        add(chartsPanel);
    }

    /**
     * Gets the top spending transactions sorted by amount.
     * @param transactions List of all transactions
     * @return 2D array containing rank, description, and amount for top expenses
     */
    private Object[][] getTopSpending(List<Transaction> transactions) {
        // 只统计支出
        List<Transaction> expenses = new ArrayList<>();
        for (Transaction t : transactions) {
            if (!t.isIncome) {
                expenses.add(t);
            }
        }
        
        // 按金额排序
        expenses.sort((t1, t2) -> 
            Double.compare(Double.parseDouble(t2.amount), Double.parseDouble(t1.amount)));
        
        Object[][] data = new Object[Math.min(7, expenses.size())][3];
        for (int i = 0; i < data.length; i++) {
            Transaction t = expenses.get(i);
            data[i][0] = i + 1;
            data[i][1] = t.description;
            data[i][2] = String.format("%.2f", Double.parseDouble(t.amount));
        }
        return data;
    }

    /**
     * Creates a dataset for monthly income visualization.
     * @param transactions List of all transactions
     * @return CategoryDataset containing monthly income data
     */
    private DefaultCategoryDataset createMonthlyIncomeDataset(List<Transaction> transactions) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Transaction t : transactions) {
            if (t.isIncome) {
                String month = t.date.substring(0, 7);
                dataset.addValue(Double.parseDouble(t.amount), "Income", month);
            }
        }
        return dataset;
    }

    /**
     * Creates a dataset for monthly expense visualization.
     * @param transactions List of all transactions
     * @return CategoryDataset containing monthly expense data
     */
    private DefaultCategoryDataset createMonthlyExpenseDataset(List<Transaction> transactions) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Transaction t : transactions) {
            if (!t.isIncome) {
                String month = t.date.substring(0, 7);
                dataset.addValue(Double.parseDouble(t.amount), "Expense", month);
            }
        }
        return dataset;
    }

    /**
     * Main method for testing the DataAnalysisPanel.
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame("数据统计分析面板");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new DataAnalysisPanel(Color.BLACK, Color.WHITE));
        frame.pack();
        frame.setSize(800, 600);
        frame.setVisible(true);
    }
}