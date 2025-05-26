package boundary;

import java.awt.*;
import javax.swing.*;
import javax.swing.BorderFactory;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.DateTickUnitType;

import control.BudgetManager;
import control.TransactionManager;
import control.UserManager;
import entity.Budget;
import entity.Transaction;
import entity.TransactionType;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * Panel for analyzing budget data using various chart visualizations.
 * Provides bar charts, pie charts, and line charts for budget analysis.
 */
public class BudgetAnalysisPanel extends JPanel {
    /** Array of month names for display and selection */
    private static final String[] MONTHS = {"January", "February", "March", "April", "May", "June", 
                                          "July", "August", "September", "October", "November", "December"};
    /** Array of available transaction types for analysis */
    private static final String[] TRANSACTION_TYPES = {"income", "health", "food", "rent", "groceries",
                                                     "transportation", "entertainment", "cosmetics",
                                                     "education", "game", "digitalProduct", "travel"};
    
    /** Manager for budget-related operations */
    private BudgetManager budgetManager;
    /** Manager for transaction-related operations */
    private TransactionManager transactionManager;
    /** Manager for user-related operations */
    private UserManager userManager;
    /** Username of the current user */
    private String currentUsername;
    
    /** Combo box for selecting transaction type */
    private JComboBox<String> typeComboBox;
    /** Combo box for selecting month */
    private JComboBox<String> monthComboBox;
    /** Combo box for selecting year */
    private JComboBox<Integer> yearComboBox;
    /** Panel containing the top section of charts */
    private JPanel topPanel;
    /** Panel containing the bottom section of charts */
    private JPanel bottomPanel;
    /** Panel containing the bar chart */
    private ChartPanel barChartPanel;
    /** Panel containing the pie chart */
    private ChartPanel pieChartPanel;
    /** Panel containing the line chart */
    private ChartPanel lineChartPanel;
    /** Panel containing control elements */
    private JPanel controlPanel;

    /**
     * Constructs a new BudgetAnalysisPanel with specified colors.
     * @param borderColor The color for the panel's border
     * @param fillColor The background color for the panel
     */
    public BudgetAnalysisPanel(Color borderColor, Color fillColor) {
        this.setBorder(BorderFactory.createLineBorder(borderColor));
        this.setBackground(fillColor);
        
        // 初始化管理器
        budgetManager = BudgetManager.getInstance();
        transactionManager = TransactionManager.getInstance();
        userManager = UserManager.getInstance();
        
        // 获取当前用户信息
        String currentUserId = userManager.getCurrentUserId();
        this.currentUsername = userManager.getUserName(currentUserId);
        
        // 设置布局
        this.setLayout(new BorderLayout());
        
        // 创建控制面板
        createControlPanel();
        
        // 创建图表面板
        createChartPanels();
        
        // 初始化数据
        updateCharts();
    }

    /**
     * Creates the control panel with type, year, and month selection components.
     */
    private void createControlPanel() {
        controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        typeComboBox = new JComboBox<>(TRANSACTION_TYPES);
        monthComboBox = new JComboBox<>(MONTHS);
        
        // 创建年份选择下拉框
        Calendar cal = Calendar.getInstance();
        int currentYear = cal.get(Calendar.YEAR);
        Integer[] years = new Integer[5];
        for (int i = 0; i < 5; i++) {
            years[i] = currentYear - i;
        }
        yearComboBox = new JComboBox<>(years);
        
        controlPanel.add(new JLabel("Type:"));
        controlPanel.add(typeComboBox);
        controlPanel.add(new JLabel("Year:"));
        controlPanel.add(yearComboBox);
        controlPanel.add(new JLabel("Month:"));
        controlPanel.add(monthComboBox);
        
        // 添加事件监听器
        typeComboBox.addActionListener(e -> updateCharts());
        monthComboBox.addActionListener(e -> updateCharts());
        yearComboBox.addActionListener(e -> updateCharts());
        
        this.add(controlPanel, BorderLayout.NORTH);
    }

    /**
     * Creates the panels for displaying charts.
     */
    private void createChartPanels() {
        // 创建上半部分（柱状图）面板
        topPanel = new JPanel(new BorderLayout());
        
        // 创建下半部分面板（包含饼图和折线图）
        bottomPanel = new JPanel(new GridLayout(1, 2));
        
        // 将面板添加到主面板
        JPanel chartsPanel = new JPanel(new GridLayout(2, 1));
        chartsPanel.add(topPanel);
        chartsPanel.add(bottomPanel);
        
        this.add(chartsPanel, BorderLayout.CENTER);
    }

    /**
     * Updates all charts based on current selection values.
     */
    private void updateCharts() {
        String selectedType = (String) typeComboBox.getSelectedItem();
        String selectedMonth = (String) monthComboBox.getSelectedItem();
        Integer selectedYear = (Integer) yearComboBox.getSelectedItem();
        
        // 更新柱状图
        updateBarChart(selectedType);
        
        // 更新饼图
        updatePieChart(selectedYear, selectedMonth);
        
        // 更新折线图
        updateLineChart(selectedType);
    }

    /**
     * Updates the bar chart with historical budget data for the selected type.
     * @param type The selected transaction type
     */
    private void updateBarChart(String type) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        List<Budget> budgets = budgetManager.getBudgetList();
        
        // 获取所有历史数据
        Map<String, Double> monthlyBudgets = new TreeMap<>();
        for (Budget budget : budgets) {
            if (budget.owner.name.equals(currentUsername) && 
                budget.type.name().equals(type)) {
                String month = budget.date.substring(0, 7); // 获取 YYYY-MM 格式
                monthlyBudgets.put(month, monthlyBudgets.getOrDefault(month, 0.0) + Double.parseDouble(budget.amount));
            }
        }
        
        // 添加数据到数据集
        for (Map.Entry<String, Double> entry : monthlyBudgets.entrySet()) {
            String[] dateParts = entry.getKey().split("-");
            String month = MONTHS[Integer.parseInt(dateParts[1]) - 1];
            dataset.addValue(entry.getValue(), type, month + " " + dateParts[0]);
        }
        
        JFreeChart chart = ChartFactory.createBarChart(
            "Historical Budget Comparison for " + type,
            "Month",
            "Budget Amount",
            dataset,
            PlotOrientation.VERTICAL,
            true,
            true,
            false
        );
        
        if (barChartPanel != null) {
            topPanel.remove(barChartPanel);
        }
        barChartPanel = new ChartPanel(chart);
        topPanel.add(barChartPanel, BorderLayout.CENTER);
        topPanel.revalidate();
    }

    /**
     * Updates the pie chart with budget distribution for the selected month and year.
     * @param year The selected year
     * @param month The selected month
     */
    private void updatePieChart(Integer year, String month) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        List<Budget> budgets = budgetManager.getBudgetList();
        
        // 获取选中月份的数据
        int monthIndex = Arrays.asList(MONTHS).indexOf(month);
        
        Map<String, Double> typeAmountMap = new HashMap<>();
        for (Budget budget : budgets) {
            if (budget.owner.name.equals(currentUsername) &&
                budget.date.startsWith(year + "-" + String.format("%02d", monthIndex + 1))) {
                String type = budget.type.name();
                typeAmountMap.put(type, typeAmountMap.getOrDefault(type, 0.0) + Double.parseDouble(budget.amount));
            }
        }
        
        for (Map.Entry<String, Double> entry : typeAmountMap.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }
        
        JFreeChart chart = ChartFactory.createPieChart(
            "Budget Distribution for " + month + " " + year,
            dataset,
            true,
            true,
            false
        );
        
        if (pieChartPanel != null) {
            bottomPanel.remove(pieChartPanel);
        }
        pieChartPanel = new ChartPanel(chart);
        bottomPanel.add(pieChartPanel, 0); // 确保饼图始终在左侧
        bottomPanel.revalidate();
    }

    /**
     * Updates the line chart comparing budget vs actual spending for the selected type.
     * @param type The selected transaction type
     */
    private void updateLineChart(String type) {
        XYSeries budgetSeries = new XYSeries("Budget");
        XYSeries actualSeries = new XYSeries("Actual");
        List<Budget> budgets = budgetManager.getBudgetList();
        List<Transaction> transactions = transactionManager.getTransactionList();
        
        // 设置时间范围：2024年4月到2025年4月
        Calendar startCal = Calendar.getInstance();
        startCal.set(2024, Calendar.APRIL, 1);
        Calendar endCal = Calendar.getInstance();
        endCal.set(2025, Calendar.APRIL, 1);
        
        // 获取所有历史数据
        Map<String, Double> monthlyBudgets = new TreeMap<>();
        Map<String, Double> monthlyActuals = new TreeMap<>();
        
        // 处理预算数据
        for (Budget budget : budgets) {
            if (budget.owner.name.equals(currentUsername) && 
                budget.type.name().equals(type)) {
                String month = budget.date.substring(0, 7);
                monthlyBudgets.put(month, monthlyBudgets.getOrDefault(month, 0.0) + Double.parseDouble(budget.amount));
            }
        }
        
        // 处理实际支出数据
        for (Transaction transaction : transactions) {
            if (transaction.owner.name.equals(currentUsername) && 
                transaction.type.name().equals(type) &&
                !transaction.isIncome) {
                String month = transaction.date.substring(0, 7);
                monthlyActuals.put(month, monthlyActuals.getOrDefault(month, 0.0) + Double.parseDouble(transaction.amount));
            }
        }
        
        // 添加数据到序列
        int index = 0;
        for (String month : monthlyBudgets.keySet()) {
            String[] dateParts = month.split("-");
            int year = Integer.parseInt(dateParts[0]);
            int monthNum = Integer.parseInt(dateParts[1]);
            
            // 只添加在时间范围内的数据
            if (year >= 2024 && monthNum >= 4 || year <= 2025 && monthNum <= 4) {
                budgetSeries.add(index, monthlyBudgets.get(month));
                actualSeries.add(index, monthlyActuals.getOrDefault(month, 0.0));
                index++;
            }
        }
        
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(budgetSeries);
        dataset.addSeries(actualSeries);
        
        JFreeChart chart = ChartFactory.createXYLineChart(
            "Budget vs Actual for " + type,
            "Month",
            "Amount",
            dataset,
            PlotOrientation.VERTICAL,
            true,
            true,
            false
        );
        
        // 设置线条颜色和样式
        XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.GREEN); // 预算线为绿色
        renderer.setSeriesPaint(1, Color.RED);   // 实际线为红色
        
        // 添加数据标签
        renderer.setDefaultItemLabelsVisible(true);
        renderer.setDefaultItemLabelGenerator((ds, series, item) -> {
            if (series == 1) { // 只在实际支出线上显示标签
                double actual = actualSeries.getY(item).doubleValue();
                double budget = budgetSeries.getY(item).doubleValue();
                if (actual > budget) {
                    return String.format("Over Budget: %.2f", actual - budget);
                }
            }
            return null;
        });
        
        plot.setRenderer(renderer);
        
        if (lineChartPanel != null) {
            bottomPanel.remove(lineChartPanel);
        }
        lineChartPanel = new ChartPanel(chart);
        bottomPanel.add(lineChartPanel);
        bottomPanel.revalidate();
    }

    /**
     * Main method for testing the BudgetAnalysisPanel.
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame("Budget Analysis Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new BudgetAnalysisPanel(Color.BLACK, Color.WHITE));
        frame.pack();
        frame.setSize(1200, 800);
        frame.setVisible(true);
    }
}