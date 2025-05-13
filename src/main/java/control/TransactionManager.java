package control;

import entity.Transaction;
import entity.TransactionType;
import java.util.ArrayList;
import java.util.List;

public class TransactionManager extends Manager {
    private static TransactionManager instance;
    private List<Transaction> transactionList = new ArrayList<>();

    public static TransactionManager getInstance() {
        if (instance == null) {
            instance = new TransactionManager();
        }
        return instance;
    }

    @Override
    public void Init() {
        System.out.println("TransactionManager initialized.");
    }

    // 导入交易数据
    public void importData(Transaction transaction) {
        transactionList.add(transaction);
    }

    // 编辑交易数据
    public void editData(Transaction transaction, String transactionId) {
        for (int i = 0; i < transactionList.size(); i++) {
            if (transactionList.get(i).transactionId.equals(transactionId)) {
                transactionList.set(i, transaction);
                return;
            }
        }
    }

    // 根据时间范围查询交易记录
    public List<Transaction> queryByTime(String startDate, String endDate) {
        List<Transaction> result = new ArrayList<>();
        for (Transaction transaction : transactionList) {
            if (transaction.date != null) {
                if ((startDate == null || transaction.date.compareTo(startDate) >= 0) &&
                        (endDate == null || transaction.date.compareTo(endDate) <= 0)) {
                    result.add(transaction);
                }
            }
        }
        return result;
    }

    // 根据所有者查询交易记录
    public List<Transaction> queryByOwner(String owner) {
        List<Transaction> result = new ArrayList<>();
        for (Transaction transaction : transactionList) {
            if (transaction.owner.name.equals(owner)) {
                result.add(transaction);
            }
        }
        return result;
    }

    // 根据交易类型查询交易记录
    public List<Transaction> queryByType(TransactionType type) {
        List<Transaction> result = new ArrayList<>();
        for (Transaction transaction : transactionList) {
            if (transaction.type == type) {
                result.add(transaction);
            }
        }
        return result;
    }

    // 根据是否为收入查询交易记录
    public List<Transaction> queryByIncome(boolean isIncome) {
        List<Transaction> result = new ArrayList<>();
        for (Transaction transaction : transactionList) {
            if (transaction.isIncome == isIncome) {
                result.add(transaction);
            }
        }
        return result;
    }

    // 加载交易数据
    public void loadData(List<Transaction> transactions) {
        transactionList.clear();
        if (transactions != null) {
            transactionList.addAll(transactions);
        }
    }

    // 获取所有交易记录
    public List<Transaction> getTransactionList() {
        return transactionList;
    }

    // 获取当前最大的交易ID
    public String getMaxTransactionId() {
        int maxId = 0;
        for (Transaction transaction : transactionList) {
            try {
                // 尝试从交易ID中提取数字部分
                String idStr = transaction.transactionId.replaceAll("[^0-9]", "");
                if (!idStr.isEmpty()) {
                    int id = Integer.parseInt(idStr);
                    maxId = Math.max(maxId, id);
                }
            } catch (NumberFormatException e) {
                // 忽略无法解析的ID
                continue;
            }
        }
        return String.format("%04d", maxId + 1);
    }
    
    // 获取指定用户的收入交易记录
    public List<Transaction> getIncomeTransactionsByUser(String userName) {
        List<Transaction> incomeTransactions = new ArrayList<>();
        for (Transaction transaction : transactionList) {
            if (transaction.owner.name.equals(userName) && transaction.isIncome) {
                incomeTransactions.add(transaction);
            }
        }
        return incomeTransactions;
    }

    // 获取指定用户的所有交易记录
    public List<Transaction> getTransactionsByUserName(String userName) {
        List<Transaction> userTransactions = new ArrayList<>();
        for (Transaction transaction : transactionList) {
            if (transaction.owner.name.equals(userName)) {
                userTransactions.add(transaction);
            }
        }
        return userTransactions;
    }
}