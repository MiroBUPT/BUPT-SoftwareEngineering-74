import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

import control.AIManager;
import control.BudgetManager;
import control.TransactionManager;
import control.UserManager;
import entity.Budget;
import entity.Transaction;
import entity.TransactionType;
import entity.User;

/**
 * Test class for AIManager functionality.
 * Tests the AI-powered financial advice generation using mocked dependencies.
 */
public class AIManagerTest {

    /** Mocked instance of BudgetManager for testing */
    private BudgetManager mockBudgetManager;
    /** Mocked instance of TransactionManager for testing */
    private TransactionManager mockTransactionManager;
    /** Mocked instance of UserManager for testing */
    private UserManager mockUserManager;
    /** Instance of AIManager to be tested */
    private AIManager aiManager;

    /**
     * Set up test environment before each test.
     * Initializes AIManager and creates mock instances of required managers.
     */
    @BeforeEach
    public void setUp() {
        aiManager = AIManager.getInstance();
        mockBudgetManager = Mockito.mock(BudgetManager.class);
        mockTransactionManager = Mockito.mock(TransactionManager.class);
        mockUserManager = Mockito.mock(UserManager.class);

        // Set up mock managers in AIManager using reflection
        setPrivateField(aiManager, "budgetManager", mockBudgetManager);
        setPrivateField(aiManager, "transactionManager", mockTransactionManager);
        setPrivateField(aiManager, "userManager", mockUserManager);
    }

    /**
     * Test generating general financial advice.
     * Verifies that general advice can be generated using mocked data.
     */
    @Test
    public void testGenerateGeneralAdvice() {
        // Mock user data
        String userId = "u001";
        String userName = "lisi";
        when(mockUserManager.getCurrentUserId()).thenReturn(userId);
        when(mockUserManager.getUserName(userId)).thenReturn(userName);

        // Mock transaction data
        List<Transaction> transactions = new ArrayList<>();
        Transaction income = new Transaction();
        income.amount = "5000";
        income.isIncome = true;
        income.type = TransactionType.income;
        transactions.add(income);

        Transaction expense = new Transaction();
        expense.amount = "1000";
        expense.isIncome = false;
        expense.type = TransactionType.food;
        transactions.add(expense);

        when(mockTransactionManager.getTransactionsByUserName(userName)).thenReturn(transactions);

        // Mock budget data
        List<Budget> budgets = new ArrayList<>();
        Budget budget = new Budget();
        budget.amount = "2000";
        budget.type = TransactionType.food;
        budgets.add(budget);
        when(mockBudgetManager.queryByOwner(userName)).thenReturn(budgets);

        // Test advice generation
        String advice = aiManager.generateAdvice("general");
        assertNotNull(advice);
        assertTrue(advice.length() > 0);
    }

    /**
     * Test generating budget analysis advice.
     * Verifies that budget analysis can be generated using mocked data.
     */
    @Test
    public void testGenerateBudgetAdvice() {
        // Mock user data
        String userId = "u001";
        String userName = "lisi";
        when(mockUserManager.getCurrentUserId()).thenReturn(userId);
        when(mockUserManager.getUserName(userId)).thenReturn(userName);

        // Mock transaction data
        List<Transaction> transactions = new ArrayList<>();
        Transaction expense = new Transaction();
        expense.amount = "1500";
        expense.isIncome = false;
        expense.type = TransactionType.food;
        transactions.add(expense);

        when(mockTransactionManager.getTransactionsByUserName(userName)).thenReturn(transactions);

        // Mock budget data
        List<Budget> budgets = new ArrayList<>();
        Budget budget = new Budget();
        budget.amount = "1000";
        budget.type = TransactionType.food;
        budgets.add(budget);
        when(mockBudgetManager.queryByOwner(userName)).thenReturn(budgets);

        // Test advice generation
        String advice = aiManager.generateAdvice("budget");
        assertNotNull(advice);
        assertTrue(advice.length() > 0);
    }

    /**
     * Test generating consumption analysis advice.
     * Verifies that consumption analysis can be generated using mocked data.
     */
    @Test
    public void testGenerateConsumptionAdvice() {
        // Mock user data
        String userId = "u001";
        String userName = "lisi";
        when(mockUserManager.getCurrentUserId()).thenReturn(userId);
        when(mockUserManager.getUserName(userId)).thenReturn(userName);

        // Mock transaction data for the past year
        List<Transaction> transactions = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            Transaction expense = new Transaction();
            expense.amount = String.valueOf(1000 + i * 100);
            expense.isIncome = false;
            expense.type = TransactionType.food;
            expense.date = String.format("2023-%02d-01", i);
            transactions.add(expense);
        }

        when(mockTransactionManager.getTransactionsByUserName(userName)).thenReturn(transactions);

        // Test advice generation
        String advice = aiManager.generateAdvice("consumption");
        assertNotNull(advice);
        assertTrue(advice.length() > 0);
    }

    /**
     * Test generating savings analysis advice.
     * Verifies that savings analysis can be generated using mocked data.
     */
    @Test
    public void testGenerateSavingsAdvice() {
        // Mock user data
        String userId = "u001";
        String userName = "lisi";
        when(mockUserManager.getCurrentUserId()).thenReturn(userId);
        when(mockUserManager.getUserName(userId)).thenReturn(userName);

        // Mock income transactions
        List<Transaction> incomeTransactions = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            Transaction income = new Transaction();
            income.amount = "5000";
            income.isIncome = true;
            income.type = TransactionType.income;
            income.date = String.format("2023-%02d-01", i);
            incomeTransactions.add(income);
        }

        // Mock expense transactions
        List<Transaction> expenseTransactions = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            Transaction expense = new Transaction();
            expense.amount = "3000";
            expense.isIncome = false;
            expense.type = TransactionType.food;
            expense.date = String.format("2023-%02d-01", i);
            expenseTransactions.add(expense);
        }

        List<Transaction> allTransactions = new ArrayList<>();
        allTransactions.addAll(incomeTransactions);
        allTransactions.addAll(expenseTransactions);

        when(mockTransactionManager.getTransactionsByUserName(userName)).thenReturn(allTransactions);

        // Test advice generation
        String advice = aiManager.generateAdvice("savings");
        assertNotNull(advice);
        assertTrue(advice.length() > 0);
    }

    /**
     * Test generating long-term analysis advice.
     * Verifies that long-term analysis can be generated using mocked data.
     */
    @Test
    public void testGenerateLongTermAdvice() {
        // Mock user data
        String userId = "u001";
        String userName = "lisi";
        when(mockUserManager.getCurrentUserId()).thenReturn(userId);
        when(mockUserManager.getUserName(userId)).thenReturn(userName);

        // Mock transaction data for the past year
        List<Transaction> transactions = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            Transaction expense = new Transaction();
            expense.amount = String.valueOf(1000 + i * 100);
            expense.isIncome = false;
            expense.type = TransactionType.food;
            expense.date = String.format("2023-%02d-01", i);
            transactions.add(expense);
        }

        when(mockTransactionManager.getTransactionsByUserName(userName)).thenReturn(transactions);

        // Test advice generation
        String advice = aiManager.generateAdvice("longterm");
        assertNotNull(advice);
        assertTrue(advice.length() > 0);
    }

    /**
     * Test generating holiday advice.
     * Verifies that holiday advice can be generated using mocked data.
     */
    @Test
    public void testGenerateHolidayAdvice() {
        // Mock user data
        String userId = "u001";
        String userName = "lisi";
        when(mockUserManager.getCurrentUserId()).thenReturn(userId);
        when(mockUserManager.getUserName(userId)).thenReturn(userName);

        // Test advice generation
        String advice = aiManager.generateAdvice("holiday");
        assertNotNull(advice);
        assertTrue(advice.length() > 0);
    }

    /**
     * Helper method to set private fields using reflection.
     * @param instance The instance containing the field
     * @param fieldName The name of the field to set
     * @param value The value to set
     */
    private void setPrivateField(Object instance, String fieldName, Object value) {
        try {
            var field = instance.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(instance, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}    