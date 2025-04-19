package boundary;

import java.awt.*;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

class IndexView {
    private List<Directory> directories = new ArrayList<>();

    public IndexView() {
        initDirectoryStructure();
    }

    private void initDirectoryStructure() {
        // 1. Dashboard目录（Entry: Home）
        Directory dashboardDir = new Directory("Dashboard");
        dashboardDir.addEntry(new Entry("Home", new HomePanel(java.awt.Color.BLUE, java.awt.Color.LIGHT_GRAY)));
        directories.add(dashboardDir);

        // 2. Statistics目录（Entry: DataAnalysis, AIAdvice）
        Directory statisticsDir = new Directory("Statistics");
        statisticsDir.addEntry(new Entry("Data Analysis", new DataAnalysisPanel(java.awt.Color.BLACK, java.awt.Color.WHITE)));
        statisticsDir.addEntry(new Entry("AI Advice", new AIAdvicePanel(java.awt.Color.GREEN, java.awt.Color.LIGHT_GRAY)));
        directories.add(statisticsDir);

        // 3. DataQuery目录（Entry: BudgetQuery, TransactionQuery）
        Directory dataQueryDir = new Directory("Data Query");
        dataQueryDir.addEntry(new Entry("Budget Query", new BudgetQueryPanel(java.awt.Color.RED, java.awt.Color.PINK)));
        dataQueryDir.addEntry(new Entry("Transaction Query", new TransactionQueryPanel(java.awt.Color.DARK_GRAY, java.awt.Color.CYAN)));
        directories.add(dataQueryDir);

        // 4. AddTransaction目录（Entry: ManualEntry, AutomatedEntry）
        Directory addTransactionDir = new Directory("Add Transaction");
        addTransactionDir.addEntry(new Entry("Manual Entry", new ManualDataEntryPanel(java.awt.Color.MAGENTA, java.awt.Color.LIGHT_GRAY)));
        addTransactionDir.addEntry(new Entry("Automated Entry", new AutomatedDataEntryPanel(java.awt.Color.ORANGE, java.awt.Color.LIGHT_GRAY)));
        directories.add(addTransactionDir);

        // 5. Budget目录（Entry: BudgetAnalysis, BudgetSetup）
        Directory budgetDir = new Directory("Budget");
        budgetDir.addEntry(new Entry("Budget Analysis", new BudgetAnalysisPanel(java.awt.Color.BLUE, java.awt.Color.LIGHT_GRAY)));
        budgetDir.addEntry(new Entry("Budget Setup", new BudgetSetupPanel(java.awt.Color.GREEN, java.awt.Color.LIGHT_GRAY)));
        directories.add(budgetDir); // 确保只添加一次

        // 6. UserProfile目录（Entry: Profile）
        Directory userProfileDir = new Directory("Individual Center");
        userProfileDir.addEntry(new Entry("Profile", new ProfilePanel(java.awt.Color.DARK_GRAY, Color.LIGHT_GRAY)));
        directories.add(userProfileDir);
    }

    public List<Directory> getDirectories() {
        return directories;
    }
}