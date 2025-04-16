package com.example.personalfinance;

import java.util.List;

public interface TransactionManager {
    static TransactionManager getinstance() {
        return TransactionManagerImpl.getInstance();
    }

    void importData(Transaction transaction);
    void editData(Transaction transaction, String newData);
    List<Transaction> queryByTime(String startTime, String endTime);
    List<Transaction> queryByOwner(String owner);
    List<Transaction> queryByType(TransactionType type);
    List<Transaction> querryBylncome(boolean isIncome);
    void initData();
}
