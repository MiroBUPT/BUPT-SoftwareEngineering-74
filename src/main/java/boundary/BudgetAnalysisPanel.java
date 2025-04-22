package main.java.boundary;
import java.awt.*;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;


public class BudgetAnalysisPanel extends JPanel {
    public BudgetAnalysisPanel(java.awt.Color borderColor, java.awt.Color fillColor) {
        this.setBorder(BorderFactory.createLineBorder(borderColor));
        this.setBackground(fillColor);
        // 使用 GridLayout，2 行 1 列
        this.setLayout(new GridLayout(2, 1));

        // 初始化饼图数据
        DefaultPieDataset pieDataset = new DefaultPieDataset();
        pieDataset.setValue("Shopping", new Double(16));
        pieDataset.setValue("Food", new Double(48));
        pieDataset.setValue("Childcare", new Double(36));
        pieDataset.setValue("Hobby", new Double(44));

        // 创建饼图
        JFreeChart pieChart = ChartFactory.createPieChart(
                "Each project budget proportion", pieDataset, true, true, false);

        // 初始化折线图数据
        DefaultCategoryDataset lineDataset = new DefaultCategoryDataset();
        lineDataset.addValue(100, "Budget", "Jan");
        lineDataset.addValue(150, "Execute", "Jan");
        lineDataset.addValue(140, "Budget", "Feb");
        lineDataset.addValue(100, "Execute", "Feb");
        lineDataset.addValue(230, "Budget", "Mar");
        lineDataset.addValue(200, "Execute", "Mar");
        lineDataset.addValue(90, "Budget", "Apr");
        lineDataset.addValue(140, "Execute", "Apr");
        lineDataset.addValue(130, "Budget", "May");
        lineDataset.addValue(100, "Execute", "May");

        // 创建折线图
        JFreeChart lineChart = ChartFactory.createLineChart(
                "Budget vs Execute Comparison (Line Chart)", // 图表标题
                "Month", // X轴标签
                "Amount", // Y轴标签
                lineDataset, // 数据集
                PlotOrientation.VERTICAL, // 图表方向
                true, // 是否显示图例
                true, // 是否显示工具提示
                false // 是否生成URL链接
        );

        // 添加图表到面板
        this.add(new ChartPanel(pieChart));
        this.add(new ChartPanel(lineChart));
    }

    public void init() {
        System.out.println("预算分析面板初始化");
    }

    public void updateData() {
        System.out.println("预算分析面板数据更新");
    }
}