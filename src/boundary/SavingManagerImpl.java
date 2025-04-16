package com.example.personalfinance;

public class SavingManagerImpl implements SavingManager {
    private static final SavingManager INSTANCE = new SavingManagerImpl();
    private BudgetManager budgetManager;
    private TransactionManager transactionManager;
    private UserManager userManager;

    private SavingManagerImpl() {
        budgetManager = BudgetManager.getinstance();
        transactionManager = TransactionManager.getinstance();
        userManager = UserManager.getinstance();
        // 这里确保在构造函数中调用initData，避免从静态上下文调用非静态方法
        budgetManager.initData();
        transactionManager.initData();
        userManager.initData();
    }

    public static SavingManager getInstance() {
        return INSTANCE;
    }

    @Override
    public void saveData() {
        // 保存数据
    }

    @Override
    public void initData() {
        // 这里可以再次调用其他管理器的initData方法，根据实际需求决定是否需要
        budgetManager.initData();
        transactionManager.initData();
        userManager.initData();
    }
}