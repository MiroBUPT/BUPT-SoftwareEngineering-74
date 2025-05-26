// BudgetManagerTest.java
import control.BudgetManager;
import entity.Budget;
import entity.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for BudgetManager functionality.
 * Tests all major operations of the BudgetManager including adding, editing, querying, and loading budgets.
 */
public class BudgetManagerTest {
    /** Instance of BudgetManager to be tested */
    private BudgetManager budgetManager;

    /**
     * Set up test environment before each test.
     * Initializes a new BudgetManager instance and clears existing data.
     */
    @BeforeEach
    public void setUp() {
        budgetManager = BudgetManager.getInstance();
        budgetManager.loadData(new ArrayList<>()); // 清空数据
    }

    /**
     * Test adding a new budget to the manager.
     * Verifies that the budget list size increases by 1 after adding.
     */
    @Test
    public void testAddBudget() {
        Budget budget = new Budget();
        budget.budgetId = "123";
        budgetManager.addBudget(budget);
        assertEquals(1, budgetManager.getBudgetList().size(), "添加预算记录后列表大小应为 1");
    }

    /**
     * Test editing an existing budget.
     * Verifies that the edit operation completes without errors.
     */
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

    /**
     * Test querying budgets by owner.
     * Verifies that the query returns a non-null result.
     */
    @Test
    public void testQueryByOwner() {
        String owner = "John";
        List<Budget> result = budgetManager.queryByOwner(owner);
        assertNotNull(result, "查询结果不应为 null");
    }

    /**
     * Test querying budgets by transaction type.
     * Verifies that the query returns a non-null result.
     */
    @Test
    public void testQueryByType() {
        TransactionType type = TransactionType.food;
        List<Budget> result = budgetManager.queryByType(type);
        assertNotNull(result, "查询结果不应为 null");
    }

    /**
     * Test querying budgets by date.
     * Verifies that the query returns a non-null result.
     */
    @Test
    public void testQueryByTime() {
        String date = "2023-01-01";
        List<Budget> result = budgetManager.queryByTime(date);
        assertNotNull(result, "查询结果不应为 null");
    }

    /**
     * Test loading budget data into the manager.
     * Verifies that the budget list size matches the loaded data size.
     */
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