package boundary;

import java.util.ArrayList;
import java.util.List;

class IndexView {
    private List<Directory> directories = new ArrayList<>();

    // 初始化预设目录结构
    public IndexView() {
        initDirectoryStructure();
    }

    private void initDirectoryStructure() {
        // 1. Dashboard目录（Entry: Home）
        Directory dashboardDir = new Directory("Dashboard", null);
        dashboardDir.addEntry(new Entry("Home", new HomePanel(Color.BLUE, Color.LIGHT_GRAY)));
        directories.add(dashboardDir);

        // 2. Statistics目录（Entry: DataAnalysis, AIAdvice）
        Directory statisticsDir = new Directory("Statistics", null);
        statisticsDir.addEntry(new Entry("Data Analysis", new DataAnalysisPanel(Color.BLACK, Color.WHITE)));
        statisticsDir.addEntry(new Entry("AI Advice", new AIAdvicePanel(Color.GREEN, Color.YELLOW)));
        directories.add(statisticsDir);

        // 3. DataQuery目录（Entry: BudgetQuery, TransactionQuery）
        Directory dataQueryDir = new Directory("Data Query", null);
        dataQueryDir.addEntry(new Entry("Budget Query", new BudgetQueryPanel(Color.RED, Color.PINK)));
        dataQueryDir.addEntry(new Entry("Transaction Query", new TransactionQueryPanel(Color.BROWN, Color.CYAN)));
        directories.add(dataQueryDir);

        // 4. AddTransaction目录（Entry: ManualEntry, AutomatedEntry）
        Directory addTransactionDir = new Directory("Add Transaction", null);
        addTransactionDir.addEntry(new Entry("Manual Entry", new ManualDataEntryPanel(Color.MAGENTA, Color.LIGHT_GRAY)));
        addTransactionDir.addEntry(new Entry("Automated Entry", new AutomatedDataEntryPanel(Color.ORANGE, Color.LIGHT_GRAY)));
        directories.add(addTransactionDir);

        // 5. Budget目录（Entry: BudgetAnalysis, BudgetSetup）
        Directory budgetDir = new Directory("Budget", null);
        budgetDir.addEntry(new Entry("Budget Analysis", new BudgetAnalysisPanel(Color.BLUE, Color.LIGHT_BLUE)));
        budgetDir.addEntry(new Entry("Budget Setup", new BudgetSetupPanel(Color.GREEN, Color.LIGHT_GREEN)));
        directories.add(budgetDir);

        // 6. UserProfile目录（Entry: Profile）
        Directory userProfileDir = new Directory("User Profile", null);
        userProfileDir.addEntry(new Entry("Profile", new ProfilePanel(Color.PURPLE, Color.LIGHT_PURPLE)));
        directories.add(userProfileDir);
    }

    public void addDirectory(Directory directory) {
        directories.add(directory);
    }

    // 获取所有目录
    public List<Directory> getDirectories() {
        return directories;
    }
}
