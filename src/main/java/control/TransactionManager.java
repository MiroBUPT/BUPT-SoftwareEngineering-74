package control;

import entity.Transaction;
import entity.TransactionType;
import entity.User;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
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

    public void importFromCSV(String filePath) {
        List<Transaction> loadedTransactions = new ArrayList<>();
        List<String> infos = new ArrayList<>();
        User curUser = UserManager.getInstance().getUserById(UserManager.getInstance().getCurrentUserId());
        int id = Integer.parseInt(transactionList.getLast().transactionId) + 1;
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {

            for (int i = 0; i < 17; i++) {
                reader.readLine();
            }
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length < 6) {
                    continue;
                }

                Transaction transaction = new Transaction();

                String fullDateTime = values[0].trim();
                transaction.date = fullDateTime.split(" ")[0];

                infos.add(values[1].trim() + "-" + values[2].trim() + "-" + values[3].trim());
                
                transaction.isIncome = values[4].trim().equals("收入");

                transaction.amount = values[5].trim().substring(1);

                transaction.type = TransactionType.none;
                transaction.transactionId = String.format("%04d", id);
                id++;
                transaction.owner = curUser;
                transaction.description = "";
                transaction.location = "";
                loadedTransactions.add(transaction);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] typeAndDesc = getInferredTypeAndDesc(infos);
        for (int i = 0; i < loadedTransactions.size(); i++) {
            if(i >= typeAndDesc.length) {
                break;
            }
            String[] tmp = typeAndDesc[i].split("-");
            try {
                loadedTransactions.get(i).type = TransactionType.valueOf(tmp[0]);
            } catch (IllegalArgumentException e) {
                loadedTransactions.get(i).type = TransactionType.none;
            }
            try {
                loadedTransactions.get(i).description = tmp[1];
            } catch (IllegalArgumentException e) {
                loadedTransactions.get(i).description = "";
            }
        }
        transactionList.addAll(loadedTransactions);
        SavingManager.getInstance().saveData();
    }

    private String[] getInferredTypeAndDesc(List<String> infos) {
        return AIManager.getInstance().getInferredTypeAndDesc(infos).split(",");
    }
}