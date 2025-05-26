package boundary;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages the navigation structure of the application.
 * This class creates and maintains the directory structure for the application's navigation menu.
 */
class IndexView {
    /** List of directories in the navigation structure */
    private List<Directory> directories = new ArrayList<>();

    /**
     * Constructs a new IndexView instance.
     * Initializes the directory structure with predefined navigation items.
     */
    public IndexView() {
        initDirectoryStructure();
    }

    /**
     * Creates the directory structure for the application navigation.
     * This method initializes all directories and their entries with appropriate panels.
     */
    private void initDirectoryStructure() {
        // 1. Dashboard目录（Entry: Home）
        Directory dashboardDir = new Directory("Dashboard", null);
        dashboardDir.addEntry(new Entry("Home", new HomePanel(Color.BLUE, Color.LIGHT_GRAY)));
        directories.add(dashboardDir);

        // 2. Statistics目录（Entry: DataAnalysis, AIAdvice）
        Directory statisticsDir = new Directory("Statistics", null);
        statisticsDir.addEntry(new Entry("Data Analysis", new DataAnalysisPanel(Color.BLACK, Color.WHITE)));
        statisticsDir.addEntry(new Entry("AI Advice", new AIAdvicePanel(Color.GREEN, Color.LIGHT_GRAY)));
        directories.add(statisticsDir);

        // 3. DataQuery目录（Entry: BudgetQuery, TransactionQuery）
        Directory dataQueryDir = new Directory("Data Query", null);
        dataQueryDir.addEntry(new Entry("Budget Query", new BudgetQueryPanel(Color.RED, Color.PINK)));
        dataQueryDir.addEntry(new Entry("Transaction Query", new TransactionQueryPanel(Color.DARK_GRAY, Color.CYAN)));
        directories.add(dataQueryDir);

        // 4. AddTransaction目录（Entry: ManualEntry, AutomatedEntry）
        Directory addTransactionDir = new Directory("Add Transaction", null);
        addTransactionDir.addEntry(new Entry("Manual Entry", new ManualDataEntryPanel(Color.MAGENTA, Color.LIGHT_GRAY)));
        addTransactionDir.addEntry(new Entry("Automated Entry", new AutomatedDataEntryPanel(Color.ORANGE, Color.LIGHT_GRAY)));
        directories.add(addTransactionDir);

        // 5. Budget目录（Entry: BudgetAnalysis, BudgetSetup）
        Directory budgetDir = new Directory("Budget", null);
        budgetDir.addEntry(new Entry("Budget Analysis", new BudgetAnalysisPanel(Color.BLUE, Color.LIGHT_GRAY)));
        budgetDir.addEntry(new Entry("Budget Setup", new BudgetSetupPanel(Color.GREEN, Color.LIGHT_GRAY)));
        directories.add(budgetDir); // 确保只添加一次

        // 6. UserProfile目录（Entry: Profile）
        Directory userProfileDir = new Directory("Individual Center", null);
        userProfileDir.addEntry(new Entry("Profile", new ProfilePanel(Color.DARK_GRAY, Color.LIGHT_GRAY)));
        directories.add(userProfileDir);
    }

    /**
     * Gets the list of directories in the navigation structure.
     * @return List of Directory objects representing the navigation structure
     */
    public List<Directory> getDirectories() {
        return directories;
    }
}