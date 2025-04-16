package com.example.personalfinance;

import java.util.ArrayList;
import java.util.List;

public class TransactionManagerImpl implements TransactionManager {
    private static final TransactionManager INSTANCE = new TransactionManagerImpl();
    private List<Transaction> transactionList;

    private TransactionManagerImpl() {
        transactionList = new ArrayList<>();
    }

    public static TransactionManager getInstance() {
        return INSTANCE;
    }

    @Override
    public void importData(Transaction transaction) {
        transactionList.add(transaction);
    }

    @Override
    public void editData(Transaction transaction, String newData) {
        // 这里简单示例修改描述，可按需扩展
        for (Transaction t : transactionList) {
            if (t.getTransactionId().equals(transaction.getTransactionId())) {
                t = new Transaction(t.getTransactionId(), t.getDate(), t.getAmount(), newData, t.getType(), t.getOwner());
                break;
            }
        }
    }

    @Override
    public List<Transaction> queryByTime(String startTime, String endTime) {
        List<Transaction> result = new ArrayList<>();
        for (Transaction transaction : transactionList) {
            if (transaction.getDate().compareTo(startTime) >= 0 && transaction.getDate().compareTo(endTime) <= 0) {
                result.add(transaction);
            }
        }
        return result;
    }

    @Override
    public List<Transaction> queryByOwner(String owner) {
        List<Transaction> result = new ArrayList<>();
        for (Transaction transaction : transactionList) {
            if (transaction.getOwner().getUserId().equals(owner)) {
                result.add(transaction);
            }
        }
        return result;
    }

    @Override
    public List<Transaction> queryByType(TransactionType type) {
        List<Transaction> result = new ArrayList<>();
        for (Transaction transaction : transactionList) {
            if (transaction.getType() == type) {
                result.add(transaction);
            }
        }
        return result;
    }

    @Override
    public List<Transaction> querryBylncome(boolean isIncome) {
        List<Transaction> result = new ArrayList<>();
        for (Transaction transaction : transactionList) {
            if (transaction.getOwner().isIncome() == isIncome) {
                result.add(transaction);
            }
        }
        return result;
    }

    @Override
    public void initData() {
        // 初始化交易数据
    }
}