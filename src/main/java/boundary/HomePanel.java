package boundary;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import control.UserManager;

import org.jfree.chart.plot.PlotOrientation;

import javax.swing.*;
import java.awt.*;

public class HomePanel extends JPanel {
    public HomePanel(java.awt.Color borderColor, java.awt.Color fillColor) {
        setBorder(BorderFactory.createLineBorder(borderColor));
        setBackground(fillColor);
        setLayout(new BorderLayout());
        init();
    }
    
    private void init() {
        System.out.println("主页面板初始化");
        UserManager userManager = UserManager.getInstance(); // 获取 UserManager 实例
        String currentUserId = userManager.getCurrentUserId(); // 获取当前用户 ID
        String userName = userManager.getUserName(currentUserId); // 获取当前用户名
        
        // 主面板，使用 BorderLayout
        setLayout(new BorderLayout());
        
        // 顶部信息面板，包含欢迎信息和财务概要
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(java.awt.Color.WHITE);
        
        // 欢迎信息面板
        JPanel welcomePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        welcomePanel.setBackground(java.awt.Color.WHITE);
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel welcomeLabel = new JLabel("👋 " + "Welcome, " + (userName != null ? userName : "Guest") + " 🎉");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomePanel.add(welcomeLabel);
        
        // 财务概要面板
        JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        summaryPanel.setBackground(java.awt.Color.WHITE);
        summaryPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel assetsLabel = new JLabel("Remaining Assets: 3734 €");
        JLabel spendingLabel = new JLabel("Today's Spending: 400 €");
        JLabel yesterdayLabel = new JLabel("Yesterday's Spending: 300 €");
        JLabel changeLabel = new JLabel("Compared to Yesterday: 33.33%");
        
        summaryPanel.add(assetsLabel);
        summaryPanel.add(spendingLabel);
        summaryPanel.add(yesterdayLabel);
        summaryPanel.add(changeLabel);
        
        topPanel.add(welcomePanel, BorderLayout.NORTH);
        topPanel.add(summaryPanel, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);
    
        // 创建一个中心面板，用于放置折线图和其他内容
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        
        // 使用 JSplitPane 来分割折线图和其他内容
        JSplitPane splitPaneCenter = new JSplitPane(JSplitPane.VERTICAL_SPLIT, createChartPanel(), createLowerPanel());
        splitPaneCenter.setDividerLocation(600); // 设置折线图区域的高度
        centerPanel.add(splitPaneCenter);
        
        add(centerPanel, BorderLayout.CENTER); // 将中心面板添加到主面板的中心
    }
    
    private JPanel createChartPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JFreeChart lineChart = createLineChart();
        ChartPanel lineChartPanel = new ChartPanel(lineChart);
        panel.add(lineChartPanel, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createLowerPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // 使用 JSplitPane 来分割饼图和表格
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createPieChartPanel(), createTablePanel());
        splitPane.setDividerLocation(400); // 设置分隔线位置
        panel.add(splitPane, BorderLayout.CENTER);
        
        return panel;
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