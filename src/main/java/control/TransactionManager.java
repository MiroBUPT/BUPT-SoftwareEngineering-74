package control;

import entity.Transaction;
import entity.TransactionType;
import control.Manager;

import java.util.ArrayList;
import java.util.List;

public class TransactionManager extends Manager {
    private static TransactionManager instance;

    public static TransactionManager getInstance() {
        if (instance == null)
            instance = new TransactionManager();
        return instance;
    }

    private List<Transaction> transactionList = new ArrayList<>();

    @Override
    public void Init() {
        System.out.println("TransactionManager initialized.");
    }

    public void importData(Transaction transaction){
        transactionList.add(transaction);
    }

    public void editData(Transaction transaction, String transactionId) {

    }

    public List<Transaction> queryByTime(String startDate, String endDate) {
        return transactionList;
    }

    public List<Transaction> queryByOwner(String owner) {
        return transactionList;
    }

    public List<Transaction> queryByType(TransactionType type) {
        return transactionList;
    }

    public List<Transaction> queryByIncome(boolean isIncome) {
        return transactionList;
    }

    public void loadData(List<Transaction> transactions) {
        transactionList.clear();
        if (transactions != null) {
            transactionList.addAll(transactions);
        }
    }

}
