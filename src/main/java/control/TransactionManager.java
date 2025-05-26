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

/**
 * Manager class for handling transaction-related operations.
 * Implements the Singleton pattern and manages transaction data including creation, modification, and queries.
 */
public class TransactionManager extends Manager {
    /** Singleton instance of TransactionManager */
    private static TransactionManager instance;
    /** List of all transactions in the system */
    private List<Transaction> transactionList = new ArrayList<>();

    /**
     * Gets the singleton instance of TransactionManager.
     * @return The singleton instance
     */
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

    /**
     * Imports a new transaction into the system.
     * @param transaction The transaction to import
     */
    public void importData(Transaction transaction) {
        transactionList.add(transaction);
    }

    /**
     * Edits an existing transaction by its ID.
     * @param transaction The new transaction data
     * @param transactionId The ID of the transaction to edit
     */
    public void editData(Transaction transaction, String transactionId) {
        for (int i = 0; i < transactionList.size(); i++) {
            if (transactionList.get(i).transactionId.equals(transactionId)) {
                transactionList.set(i, transaction);
                return;
            }
        }
    }

    /**
     * Queries transactions within a specified time range.
     * @param startDate The start date of the range (inclusive)
     * @param endDate The end date of the range (inclusive)
     * @return List of transactions within the specified time range
     */
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

    /**
     * Queries transactions by owner.
     * @param owner The name of the owner
     * @return List of transactions owned by the specified owner
     */
    public List<Transaction> queryByOwner(String owner) {
        List<Transaction> result = new ArrayList<>();
        for (Transaction transaction : transactionList) {
            if (transaction.owner.name.equals(owner)) {
                result.add(transaction);
            }
        }
        return result;
    }

    /**
     * Queries transactions by type.
     * @param type The type of transaction to query
     * @return List of transactions of the specified type
     */
    public List<Transaction> queryByType(TransactionType type) {
        List<Transaction> result = new ArrayList<>();
        for (Transaction transaction : transactionList) {
            if (transaction.type == type) {
                result.add(transaction);
            }
        }
        return result;
    }

    /**
     * Queries transactions by income status.
     * @param isIncome true for income transactions, false for expenses
     * @return List of transactions matching the income status
     */
    public List<Transaction> queryByIncome(boolean isIncome) {
        List<Transaction> result = new ArrayList<>();
        for (Transaction transaction : transactionList) {
            if (transaction.isIncome == isIncome) {
                result.add(transaction);
            }
        }
        return result;
    }

    /**
     * Loads transaction data from a list.
     * @param transactions The list of transactions to load
     */
    public void loadData(List<Transaction> transactions) {
        transactionList.clear();
        if (transactions != null) {
            transactionList.addAll(transactions);
        }
    }

    /**
     * Gets all transactions in the system.
     * @return List of all transactions
     */
    public List<Transaction> getTransactionList() {
        return transactionList;
    }

    /**
     * Gets the next available transaction ID.
     * @return The next transaction ID in sequence
     */
    public String getMaxTransactionId() {
        int maxId = 0;
        for (Transaction transaction : transactionList) {
            try {
                String idStr = transaction.transactionId.replaceAll("[^0-9]", "");
                if (!idStr.isEmpty()) {
                    int id = Integer.parseInt(idStr);
                    maxId = Math.max(maxId, id);
                }
            } catch (NumberFormatException e) {
                continue;
            }
        }
        return String.format("%04d", maxId + 1);
    }

    /**
     * Gets all income transactions for a specific user.
     * @param userName The name of the user
     * @return List of income transactions for the user
     */
    public List<Transaction> getIncomeTransactionsByUser(String userName) {
        List<Transaction> incomeTransactions = new ArrayList<>();
        for (Transaction transaction : transactionList) {
            if (transaction.owner.name.equals(userName) && transaction.isIncome) {
                incomeTransactions.add(transaction);
            }
        }
        return incomeTransactions;
    }

    /**
     * Gets all transactions for a specific user.
     * @param userName The name of the user
     * @return List of all transactions for the user
     */
    public List<Transaction> getTransactionsByUserName(String userName) {
        List<Transaction> userTransactions = new ArrayList<>();
        for (Transaction transaction : transactionList) {
            if (transaction.owner.name.equals(userName)) {
                userTransactions.add(transaction);
            }
        }
        return userTransactions;
    }

    /**
     * Imports transactions from a CSV file.
     * @param filePath The path to the CSV file
     */
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

    /**
     * Gets inferred transaction types and descriptions using AI.
     * @param infos List of transaction information strings
     * @return Array of type-description pairs
     */
    private String[] getInferredTypeAndDesc(List<String> infos) {
        return AIManager.getInstance().getInferredTypeAndDesc(infos).split(",");
    }
}