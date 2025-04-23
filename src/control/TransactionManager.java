package control;

import entity.Transaction;
import entity.TransactionType;
import entity.User;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionManager extends Manager {
    private static TransactionManager instance;
    private static final String CSV_FILE_PATH = "transactions.csv";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public static TransactionManager getInstance() {
        if (instance == null)
            instance = new TransactionManager();
        return instance;
    }

    private List<Transaction> transactionList = new ArrayList<>();

    @Override
    public void Init() {
        System.out.println("TransactionManager initialized.");
        loadFromCSV();
    }

    public void importData(Transaction transaction) {
        transactionList.add(transaction);
        saveToCSV();
    }

    public void editData(Transaction transaction, String transactionId) {
        for (int i = 0; i < transactionList.size(); i++) {
            if (transactionList.get(i).transactionId.equals(transactionId)) {
                transactionList.set(i, transaction);
                saveToCSV();
                return;
            }
        }
    }

    public List<Transaction> queryByTime(String startDate, String endDate) {
        try {
            Date start = DATE_FORMAT.parse(startDate);
            Date end = DATE_FORMAT.parse(endDate);

            return transactionList.stream()
            .filter(t -> {
                try {
                    Date transactionDate = DATE_FORMAT.parse(t.date);
                    return !transactionDate.before(start) && !transactionDate.after(end);
                } catch (ParseException e) {
                    return false;
                }
            })
            .collect(Collectors.toList());
        } catch (ParseException e) {
            System.err.println("Invalid date format");
            return new ArrayList<>();
        }
    }

    public List<Transaction> queryByOwner(User owner) {
        return transactionList.stream()
        .filter(t -> t.owner.userId.equals(owner.userId))
        .collect(Collectors.toList());
    }

    public List<Transaction> queryByType(TransactionType type) {
        return transactionList.stream()
        .filter(t -> t.type == type)
        .collect(Collectors.toList());
    }

    public void loadData(List<Transaction> transactions) {
        transactionList.clear();
        transactionList.addAll(transactions);
        saveToCSV();
    }

    private void loadFromCSV() {
        File file = new File(CSV_FILE_PATH);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 7) {
                    Transaction transaction = new Transaction();
                    transaction.transactionId = data[0];
                    transaction.date = data[1];
                    transaction.amount = data[2];
                    transaction.description = data[3];
                    transaction.location = data[4];
                    transaction.type = TransactionType.valueOf(data[5]);
                    transaction.isIncome = Boolean.parseBoolean(data[6]);
                    
                    User owner = new User();
                    owner.userId = data[7];
                    transaction.owner = owner;
                    
                    transactionList.add(transaction);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading transactions from CSV: " + e.getMessage());
        }
    }

    private void saveToCSV() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_FILE_PATH))) {
            for (Transaction transaction : transactionList) {
                writer.write(String.format("%s,%s,%s,%s,%s,%s,%b,%s%n",
                    transaction.transactionId,
                    transaction.date,
                    transaction.amount,
                    transaction.description,
                    transaction.location,
                    transaction.type,
                    transaction.isIncome,
                    transaction.owner.userId));
            }
        } catch (IOException e) {
            System.err.println("Error saving transactions to CSV: " + e.getMessage());
        }
    }
}
