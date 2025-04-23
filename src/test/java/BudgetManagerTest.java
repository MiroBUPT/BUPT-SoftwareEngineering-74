// BudgetManagerTest.java
import control.BudgetManager;
import entity.Budget;
import entity.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BudgetManagerTest {
    private BudgetManager budgetManager;

    @BeforeEach
    public void setUp() {
        budgetManager = BudgetManager.getInstance();
        budgetManager.loadData(new ArrayList<>()); // 清空数据
    }

    @Test
    public void testAddBudget() {
        Budget budget = new Budget();
        budget.budgetId = "123";
        budgetManager.addBudget(budget);
        assertEquals(1, budgetManager.getBudgetList().size(), "添加预算记录后列表大小应为 1");
    }

    @Test
    public void testEditBudget() {
        Budget budget = new Budget();
        budget.budgetId = "123";
        String budgetId = "123";
        budgetManager.addBudget(budget);
        budgetManager.editBudget(budget, budgetId);
        // 这里只是测试调用不报错
        assertTrue(true);
    }

    @Test
    public void testQueryByOwner() {
        String owner = "John";
        List<Budget> result = budgetManager.queryByOwner(owner);
        assertNotNull(result, "查询结果不应为 null");
    }

    @Test
    public void testQueryByType() {
        TransactionType type = TransactionType.food;
        List<Budget> result = budgetManager.queryByType(type);
        assertNotNull(result, "查询结果不应为 null");
    }

    @Test
    public void testQueryByTime() {
        String date = "2023-01-01";
        List<Budget> result = budgetManager.queryByTime(date);
        assertNotNull(result, "查询结果不应为 null");
    }

    @Test
    public void testLoadData() {
        List<Budget> budgets = new ArrayList<>();
        Budget budget = new Budget();
        budget.budgetId = "123";
        budgets.add(budget);
        budgetManager.loadData(budgets);
        assertEquals(1, budgetManager.getBudgetList().size(), "加载数据后列表大小应为 1");
    }
}