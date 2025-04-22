package main.java.boundary;

import javax.swing.*;
import java.awt.*;
import java.awt.Color;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.plot.PlotOrientation;


public class DataAnalysisPanel extends JPanel {
    public DataAnalysisPanel(java.awt.Color borderColor, java.awt.Color fillColor) {
        setBorder(BorderFactory.createLineBorder(borderColor));
        setBackground(fillColor);
        // 使用 GridLayout，2 行 1 列
        setLayout(new GridLayout(2, 1));
        init();
    }

    private void init() {
        System.out.println("数据统计分析面板初始化");

        // 创建支出排名表格
        String[] columnNames = {"Rank", "Description", "Amount"};
        Object[][] data = {
                {"1", "Walmart Shopping", "2,500"},
                {"2", "Walmart Shopping", "2,100"},
                {"3", "Walmart Shopping", "1,800"},
                {"4", "Walmart Shopping", "1,743"},
                {"5", "Walmart Shopping", "1,421"},
                {"6", "Walmart Shopping", "1,743"},
                {"7", "Walmart Shopping", "1,421"}
        };

        JTable table = new JTable(data, columnNames);
        JScrollPane tableScrollPane = new JScrollPane(table);

        // 创建表格标题
        JLabel tableTitle = new JLabel("expending ranking");
        tableTitle.setHorizontalAlignment(JLabel.CENTER);

        // 创建一个面板来放置标题和表格
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        tablePanel.add(tableTitle, BorderLayout.NORTH);
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);

        // 添加表格面板到主面板
        add(tablePanel);

        // 创建家庭成员消费比例折线图数据集
        XYSeriesCollection dataset = createLineDataset();

        // 创建折线图
        JFreeChart lineChart = ChartFactory.createXYLineChart(
                "Family Member Consumption Ratio", "Hour", "Percentage",
                dataset, PlotOrientation.VERTICAL, true, true, false);

        // 创建折线图面板
        ChartPanel lineChartPanel = new ChartPanel(lineChart);
        // 添加折线图到面板
        add(lineChartPanel);
    }

    private XYSeriesCollection createLineDataset() {
        XYSeriesCollection dataset = new XYSeriesCollection();

        XYSeries series1 = new XYSeries("Kid");
        XYSeries series2 = new XYSeries("Mom");
        XYSeries series3 = new XYSeries("Dad");

        for (int i = 0; i < 24; i++) {
            series1.add((double) i, (i + 1) % 5 + 20); // 确保数据在合理范围内
            series2.add((double) i, (i + 2) % 5 + 20);
            series3.add((double) i, (i + 3) % 5 + 20);
        }

        dataset.addSeries(series1);
        dataset.addSeries(series2);
        dataset.addSeries(series3);

        return dataset;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Data Analysis Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new DataAnalysisPanel(java.awt.Color.BLACK, Color.WHITE));
        frame.pack();
        frame.setSize(800, 600);
        frame.setVisible(true);
    }
}