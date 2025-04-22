package main.java.boundary;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.chart.plot.PlotOrientation;

import javax.swing.*;
import java.awt.*;
import java.awt.Color;

public class HomePanel extends JPanel {
    public HomePanel(java.awt.Color borderColor, java.awt.Color fillColor) {
        setBorder(BorderFactory.createLineBorder(borderColor));
        setBackground(fillColor);
        setLayout(new BorderLayout());
        init();
    }

    private void init() {
        System.out.println("主页面板初始化");

        // 左上角欢迎信息面板
        JPanel welcomePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        welcomePanel.setBackground(java.awt.Color.WHITE);
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel welcomeLabel = new JLabel("Welcome, username");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomePanel.add(welcomeLabel);
        add(welcomePanel, BorderLayout.WEST);

        // 顶部信息面板
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.setBackground(java.awt.Color.WHITE);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel assetsLabel = new JLabel("Remaining Assets: 3734 €");
        JLabel spendingLabel = new JLabel("Today's Spending: 400 €");
        JLabel yesterdayLabel = new JLabel("Yesterday's Spending: 300 €");
        JLabel changeLabel = new JLabel("Compared to Yesterday: 33.33%");

        topPanel.add(assetsLabel);
        topPanel.add(spendingLabel);
        topPanel.add(yesterdayLabel);
        topPanel.add(changeLabel);

        add(topPanel, BorderLayout.NORTH);

        // 折线图
        JFreeChart lineChart = createLineChart();
        ChartPanel lineChartPanel = new ChartPanel(lineChart);
        add(lineChartPanel, BorderLayout.NORTH);

        // 使用 JSplitPane 来分割饼图和表格
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createPieChartPanel(), createTablePanel());
        splitPane.setDividerLocation(400); // 设置分隔线位置
        add(splitPane, BorderLayout.CENTER);
    }

    private JFreeChart createLineChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(10, "Consumption", "2025-03-09");
        dataset.addValue(20, "Consumption", "2025-03-10");
        dataset.addValue(30, "Consumption", "2025-03-11");
        dataset.addValue(40, "Consumption", "2025-03-12");
        dataset.addValue(35, "Consumption", "2025-03-13");
        dataset.addValue(45, "Consumption", "2025-03-14");
        dataset.addValue(40, "Consumption", "2025-03-15");
        dataset.addValue(35, "Consumption", "2025-03-16");

        JFreeChart lineChart = ChartFactory.createLineChart(
                "Consumer Trend (Last 7 days)", "Date", "Amount", dataset,
                PlotOrientation.VERTICAL, true, true, false);

        return lineChart;
    }

    private ChartPanel createPieChartPanel() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Shopping", 16);
        dataset.setValue("Food", 48);
        dataset.setValue("Childcare", 36);

        JFreeChart pieChart = ChartFactory.createPieChart(
                "Consumer Structure (Last 7 days)", dataset, true, true, false);

        return new ChartPanel(pieChart);
    }

    private JScrollPane createTablePanel() {
        String[] columnNames = {"Date", "Description", "Amount", "Owner"};
        Object[][] data = {
                {"3-11", "Walmart Shopping", "346.3 €", "Miro"},
                {"3-11", "Walmart Shopping", "346.3 €", "Miro"},
                {"3-11", "Walmart Shopping", "346.3 €", "Miro"},
                {"3-11", "Walmart Shopping", "346.3 €", "Miro"},
                {"3-11", "Walmart Shopping", "346.3 €", "Miro"},
                {"3-11", "Walmart Shopping", "346.3 €", "Miro"},
                {"3-11", "Walmart Shopping", "346.3 €", "Miro"}
        };

        JTable table = new JTable(data, columnNames);
        return new JScrollPane(table);
    }



    public static void main(String[] args) {
        JFrame frame = new JFrame("Home Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new HomePanel(java.awt.Color.BLACK, Color.WHITE));
        frame.pack();
        frame.setSize(800, 600);
        frame.setVisible(true);
    }
}