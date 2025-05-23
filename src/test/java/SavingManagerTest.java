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

public class SavingManagerTest {

    private SavingManager savingManager;
    private UserManager userManager;
    private BudgetManager budgetManager;
    private TransactionManager transactionManager;

    private final String testUserCsv = "src/test/resources/user_test.csv";
    private final String testBudgetCsv = "src/test/resources/budget_test.csv";
    private final String testTransactionCsv = "src/test/resources/transaction_test.csv";

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

    @Test
    public void testSaveAndLoadData() {
        savingManager.saveData(); // 写入 CSV
        savingManager.loadData(); // 重新加载

        assertEquals("Alice", userManager.getUserName("u001"));
        assertEquals(1, budgetManager.getBudgetList().size());
        assertEquals(1, transactionManager.getTransactionList().size());
    }

    // 用于设置私有静态字段
    private static void setPrivateStaticField(Class<?> clazz, String fieldName, String value) {
        try {
            var field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(null, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    public void cleanup() {
        try {
            Files.deleteIfExists(Paths.get(testUserCsv));
            Files.deleteIfExists(Paths.get(testBudgetCsv));
            Files.deleteIfExists(Paths.get(testTransactionCsv));
        } catch (IOException ignored) {}
    }
}
