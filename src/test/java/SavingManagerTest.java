import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import control.BudgetManager;
import control.SavingManager;
import control.TransactionManager;
import control.UserManager;
import entity.Budget;
import entity.Transaction;
import entity.TransactionType;
import entity.User;

/**
 * Test class for SavingManager functionality.
 * Tests the persistence and loading of user, budget, and transaction data using CSV files.
 */
public class SavingManagerTest {

    /** Instance of SavingManager to be tested */
    private SavingManager savingManager;
    /** Instance of UserManager for user data operations */
    private UserManager userManager;
    /** Instance of BudgetManager for budget data operations */
    private BudgetManager budgetManager;
    /** Instance of TransactionManager for transaction data operations */
    private TransactionManager transactionManager;

    /** Path to the test user data CSV file */
    private final String testUserCsv = "src/test/resources/user_test.csv";
    /** Path to the test budget data CSV file */
    private final String testBudgetCsv = "src/test/resources/budget_test.csv";
    /** Path to the test transaction data CSV file */
    private final String testTransactionCsv = "src/test/resources/transaction_test.csv";

    /**
     * Set up test environment before each test.
     * Initializes all managers and test data, and configures test file paths.
     */
    @BeforeEach
    public void setUp() {
        // 初始化管理器
        userManager = UserManager.getInstance();
        budgetManager = BudgetManager.getInstance();
        transactionManager = TransactionManager.getInstance();
        savingManager = SavingManager.getInstance();

        // 初始化用户数据
        User user = new User();
        user.userId = "u001";
        user.name = "Alice";
        user.password = "123456";
        userManager.loadData(List.of(user));

        // 初始化预算数据
        Budget budget = new Budget();
        budget.budgetId = "b001";
        budget.amount = "500";
        budget.type = TransactionType.food;
        budget.owner = user;
        budget.date = "2024-01";
        budgetManager.loadData(List.of(budget));

        // 初始化交易数据
        Transaction tx = new Transaction();
        tx.transactionId = "t001";
        tx.date = "2024-01-01";
        tx.amount = "20";
        tx.description = "snack";
        tx.type = TransactionType.food;
        tx.owner = user;
        tx.isIncome = false;
        tx.location = "Beijing";
        transactionManager.loadData(List.of(tx));

        // 使用测试路径替换 SavingManager 内部路径（可通过反射或 setter 方式优化）
        setPrivateStaticField(SavingManager.class, "userFilePath", testUserCsv);
        setPrivateStaticField(SavingManager.class, "budgetFilePath", testBudgetCsv);
        setPrivateStaticField(SavingManager.class, "transactionFilePath", testTransactionCsv);
    }

    /**
     * Test saving and loading all data types.
     * Verifies that data can be saved to CSV files and correctly reloaded.
     */
    @Test
    public void testSaveAndLoadData() {
        savingManager.saveData(); // 写入 CSV
        savingManager.loadData(); // 重新加载
        assertEquals("Alice", userManager.getUserName("u001"));
        assertEquals(1, budgetManager.getBudgetList().size());
        assertEquals(1, transactionManager.getTransactionList().size());
    }

    /**
     * Test user data persistence and reloading.
     * Verifies that user data is correctly saved and loaded from CSV files.
     */
    @Test
    public void testUserDataAfterReload() {
        savingManager.saveData();
        savingManager.loadData(); // 加载

        User user = userManager.getUserById("u001");
        assertEquals("u001", user.userId);
        assertEquals("Alice", user.name);
        assertEquals("123456", user.password);
    }

    /**
     * Test budget data persistence and reloading.
     * Verifies that budget data is correctly saved and loaded from CSV files.
     */
    @Test
    public void testBudgetDataAfterReload() {
        savingManager.saveData();
        savingManager.loadData(); // 加载

        Budget budget = budgetManager.getBudgetList().get(0);
        assertEquals("b001", budget.budgetId);
        assertEquals("500", budget.amount);
        assertEquals(TransactionType.food, budget.type);
        assertEquals("u001", budget.owner.userId);
        assertEquals("2024-01", budget.date);
    }

    /**
     * Test transaction data persistence and reloading.
     * Verifies that transaction data is correctly saved and loaded from CSV files.
     */
    @Test
    public void testTransactionDataAfterReload() {
        savingManager.saveData();
        savingManager.loadData(); // 加载

        Transaction tx = transactionManager.getTransactionList().get(0);
        assertEquals("t001", tx.transactionId);
        assertEquals("2024-01-01", tx.date);
        assertEquals("20", tx.amount);
        assertEquals("snack", tx.description);
        assertEquals(TransactionType.food, tx.type);
        assertEquals("u001", tx.owner.userId);
        assertEquals("Beijing", tx.location);
    }

    /**
     * Helper method to set private static fields using reflection.
     * @param clazz The class containing the field
     * @param fieldName The name of the field to set
     * @param value The value to set
     */
    private static void setPrivateStaticField(Class<?> clazz, String fieldName, String value) {
        try {
            var field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(null, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Clean up test environment after each test.
     * Deletes test CSV files created during testing.
     */
    @AfterEach
    public void cleanup() {
        try {
            Files.deleteIfExists(Paths.get(testUserCsv));
            Files.deleteIfExists(Paths.get(testBudgetCsv));
            Files.deleteIfExists(Paths.get(testTransactionCsv));
        } catch (IOException ignored) {}
    }
}
