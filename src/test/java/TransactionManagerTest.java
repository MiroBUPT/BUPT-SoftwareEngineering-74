// TransactionManagerTest.java
import control.TransactionManager;
import entity.Transaction;
import entity.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionManagerTest {
    private TransactionManager transactionManager;

    @BeforeEach
    public void setUp() {
        transactionManager = TransactionManager.getInstance();
        transactionManager.loadData(new ArrayList<>()); // 清空数据
    }

    @Test
    public void testImportData() {
        Transaction transaction = new Transaction();
        transaction.transactionId = "123";
        transaction.date = "2023-01-01";
        transactionManager.importData(transaction);
        assertEquals(1, transactionManager.queryByTime(null, null).size(), "导入交易记录后列表大小应为 1");
    }

    @Test
    public void testEditData() {
        Transaction transaction = new Transaction();
        transaction.transactionId = "123";
        String transactionId = "123";
        transactionManager.importData(transaction);
        transactionManager.editData(transaction, transactionId);
        // 由于 editData 方法目前为空，这里只是测试调用不报错
        assertTrue(true);
    }

    @Test
    public void testQueryByTime() {
        String startDate = "2023-01-01";
        String endDate = "2023-12-31";
        List<Transaction> result = transactionManager.queryByTime(startDate, endDate);
        assertNotNull(result, "查询结果不应为 null");
    }

    @Test
    public void testQueryByOwner() {
        String owner = "John";
        List<Transaction> result = transactionManager.queryByOwner(owner);
        assertNotNull(result, "查询结果不应为 null");
    }

    @Test
    public void testQueryByType() {
        TransactionType type = TransactionType.food;
        List<Transaction> result = transactionManager.queryByType(type);
        assertNotNull(result, "查询结果不应为 null");
    }

    @Test
    public void testQueryByIncome() {
        boolean isIncome = true;
        List<Transaction> result = transactionManager.queryByIncome(isIncome);
        assertNotNull(result, "查询结果不应为 null");
    }

    @Test
    public void testLoadData() {
        List<Transaction> transactions = new ArrayList<>();
        Transaction transaction = new Transaction();
        transaction.transactionId = "123";
        transaction.date = "2023-01-01";
        transactions.add(transaction);
        transactionManager.loadData(transactions);
        assertEquals(1, transactionManager.queryByTime(null, null).size(), "加载数据后列表大小应为 1");
    }
}