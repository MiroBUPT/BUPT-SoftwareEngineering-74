package boundary;
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



import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BudgetAnalysisPanel extends JPanel {

    private static final String CSV_FILE_PATH = "src/main/resources/budget.csv";

    public BudgetAnalysisPanel(Color borderColor, Color fillColor) {
        this.setBorder(BorderFactory.createLineBorder(borderColor));
        this.setBackground(fillColor);
        // 使用 GridLayout，2 行 1 列
        this.setLayout(new GridLayout(2, 1));

        // 初始化图表数据
        init();
    }

    private void init() {
        System.out.println("预算分析面板init");

        // 读取并解析 CSV 文件
        List<BudgetItem> budgetItems = readBudgetItemsFromCSV();

        // 初始化饼图数据
        DefaultPieDataset pieDataset = createPieDataset(budgetItems);

        // 创建饼图
        JFreeChart pieChart = ChartFactory.createPieChart(
                "Each project budget proportion", pieDataset, true, true, false);

        // 初始化折线图数据
        DefaultCategoryDataset lineDataset = createMonthlyExpenseDataset(budgetItems);

        // 创建折线图
        JFreeChart lineChart = ChartFactory.createLineChart(
                "Monthly Expense Trend", // 图表标题
                "Month", // X轴标签
                "Total Expense", // Y轴标签
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

    private List<BudgetItem> readBudgetItemsFromCSV() {
        List<BudgetItem> budgetItems = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
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

    private DefaultPieDataset createPieDataset(List<BudgetItem> budgetItems) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        Map<String, Double> typeAmountMap = new HashMap<>();
        for (BudgetItem item : budgetItems) {
            String type = item.getType();
            double amount = item.getAmount();
            typeAmountMap.put(type, typeAmountMap.getOrDefault(type, 0.0) + amount);
        }
        for (Map.Entry<String, Double> entry : typeAmountMap.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }
        return dataset;
    }

    private DefaultCategoryDataset createMonthlyExpenseDataset(List<BudgetItem> budgetItems) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Map<String, Map<Integer, Double>> yearlyExpenseMap = new HashMap<>();

        for (BudgetItem item : budgetItems) {
            String date = item.getDate();
            int year = Integer.parseInt(date.substring(0, 4));
            int month = Integer.parseInt(date.substring(5, 7));
            double amount = item.getAmount();

            Map<Integer, Double> monthlyExpenses = yearlyExpenseMap.computeIfAbsent(String.valueOf(year), k -> new HashMap<>());
            monthlyExpenses.put(month, monthlyExpenses.getOrDefault(month, 0.0) + amount);
        }

        // 将数据添加到数据集
        for (Map.Entry<String, Map<Integer, Double>> entry : yearlyExpenseMap.entrySet()) {
            String year = entry.getKey();
            Map<Integer, Double> monthlyExpenses = entry.getValue();
            for (int month = 1; month <= 12; month++) {
                dataset.addValue(monthlyExpenses.getOrDefault(month, 0.0), year, String.valueOf(month));
            }
        }

        return dataset;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("预算分析面板");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new BudgetAnalysisPanel(Color.BLACK, Color.WHITE));
        frame.pack();
        frame.setSize(800, 600);
        frame.setVisible(true);
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

        public String getDate() {
            return date;
        }
    }
}