import control.AIManager;
import control.BudgetManager;
import control.TransactionManager;
import control.UserManager;
import entity.Budget;
import entity.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

public class AIManagerTest {

    private AIManager aiManager;
    private BudgetManager mockBudgetManager;
    private TransactionManager mockTransactionManager;
    private UserManager mockUserManager;

    @BeforeEach
    public void setUp() {
        aiManager = AIManager.getInstance();
        mockBudgetManager = Mockito.mock(BudgetManager.class);
        mockTransactionManager = Mockito.mock(TransactionManager.class);
        mockUserManager = Mockito.mock(UserManager.class);

        // 使用 setter 方法设置模拟对象
        aiManager.setBudgetManager(mockBudgetManager);
        aiManager.setTransactionManager(mockTransactionManager);
        aiManager.setUserManager(mockUserManager);
    }

    @Test
    public void testGenerateAdvice() {
        // 模拟用户 ID 和用户名
        String userId = "123";
        String userName = "John";
        when(mockUserManager.getCurrentUserId()).thenReturn(userId);
        when(mockUserManager.getUserName(userId)).thenReturn(userName);

        // 模拟收入交易记录
        List<Transaction> incomeTransactions = new ArrayList<>();
        Transaction incomeTransaction = new Transaction();
        incomeTransaction.amount = "1000";
        incomeTransaction.isIncome = true;
        incomeTransactions.add(incomeTransaction);
        when(mockTransactionManager.getIncomeTransactionsByUser(userName)).thenReturn(incomeTransactions);

        // 模拟所有交易记录
        List<Transaction> allTransactions = new ArrayList<>();
        Transaction transaction = new Transaction();
        transaction.amount = "500";
        allTransactions.add(transaction);
        when(mockTransactionManager.getTransactionsByUserName(userName)).thenReturn(allTransactions);

        // 模拟预算记录
        List<Budget> budgets = new ArrayList<>();
        Budget budget = new Budget();
        budget.amount = "2000";
        budgets.add(budget);
        when(mockBudgetManager.queryByOwner(userName)).thenReturn(budgets);

        // 由于调用 API 是实际网络请求，这里不进行真实调用，仅测试方法是否能正常返回
        String advice = aiManager.generateAdvice();
        assertNotNull(advice);
    }
}    