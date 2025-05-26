package control;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entity.Budget;
import entity.Transaction;
import entity.TransactionType;
import entity.User;

/**
 * Manager class for handling data persistence operations.
 * Implements the Singleton pattern and manages saving/loading of data to/from CSV files.
 */
public class SavingManager extends Manager {
    /** Singleton instance of SavingManager */
    private static SavingManager instance;
    /** Flag indicating whether the manager has been initialized */
    private static boolean isInitialized = false;

    /**
     * Gets the singleton instance of SavingManager.
     * @return The singleton instance
     */
    public static SavingManager getInstance() {
        if (instance == null)
            instance = new SavingManager();
        return instance;
    }

    /** Manager for budget-related operations */
    private BudgetManager budgetManager;
    /** Manager for transaction-related operations */
    private TransactionManager transactionManager;
    /** Manager for user-related operations */
    private UserManager userManager;
    /** File path for user data */
    private static String userFilePath = "src/main/resources/user.csv";
    /** File path for budget data */
    private static String budgetFilePath = "src/main/resources/budget.csv";
    /** File path for transaction data */
    private static String transactionFilePath = "src/main/resources/transaction.csv";

    @Override
    public void Init() {
        if (!isInitialized) {
            budgetManager = BudgetManager.getInstance();
            transactionManager = TransactionManager.getInstance();
            userManager = UserManager.getInstance();
            System.out.println("SavingManager initialized");
            loadData();
            isInitialized = true;
        }
    }

    /**
     * Saves all data to CSV files.
     * Saves users, budgets, and transactions.
     */
    public void saveData() {
        saveUsersToCSV();
        saveBudgetsToCSV();
        saveTransactionsToCSV();
    }

    /**
     * Loads all data from CSV files.
     * Loads users, budgets, and transactions.
     */
    public void loadData() {
        loadUsersFromCSV(userFilePath);
        loadBudgetFromCSV(budgetFilePath);
        loadTransactionsFromCSV(transactionFilePath);
    }

    /**
     * Saves user data to CSV file.
     * @return true if save was successful, false otherwise
     */
    public boolean saveUsersToCSV() {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(userFilePath), StandardCharsets.UTF_8))) {
            var userList = userManager.getUserList();
            writer.write("userId,name,password");
            writer.newLine();

            for (User user : userList) {
                String line = String.format("%s,%s,%s",
                        escapeCsvField(user.userId),
                        escapeCsvField(user.name),
                        escapeCsvField(user.password));
                writer.write(line);
                writer.newLine();
            }

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Saves budget data to CSV file.
     * @return true if save was successful, false otherwise
     */
    public boolean saveBudgetsToCSV() {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(budgetFilePath), StandardCharsets.UTF_8))) {
            var budgetList = budgetManager.getBudgetList();
            writer.write("budgetId,amount,type,owner,date");
            writer.newLine();

            for (Budget budget : budgetList) {
                String line = String.format("%s,%s,%s,%s,%s",
                        escapeCsvField(budget.budgetId),
                        escapeCsvField(budget.amount),
                        escapeCsvField(String.valueOf(budget.type.ordinal())),
                        escapeCsvField(budget.owner.userId),
                        escapeCsvField(budget.date));
                writer.write(line);
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Saves transaction data to CSV file.
     * @return true if save was successful, false otherwise
     */
    public boolean saveTransactionsToCSV() {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(transactionFilePath), StandardCharsets.UTF_8))) {
            var transactionList = transactionManager.getTransactionList();
            writer.write("transactionId,date,amount,description,type,owner,isIncome,location");
            writer.newLine();

            for (Transaction transaction : transactionList) {
                String ownerName = transaction.owner != null && transaction.owner.name != null ? 
                                  transaction.owner.name : 
                                  (transaction.owner != null ? transaction.owner.userId : "unknown");
                
                String line = String.format("%s,%s,%s,%s,%s,%s,%s,%s",
                        escapeCsvField(transaction.transactionId),
                        escapeCsvField(transaction.date),
                        escapeCsvField(transaction.amount),
                        escapeCsvField(transaction.description),
                        escapeCsvField(String.valueOf(transaction.type.ordinal())),
                        escapeCsvField(ownerName),
                        escapeCsvField(String.valueOf(transaction.isIncome)),
                        escapeCsvField(transaction.location));
                writer.write(line);
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Loads user data from a CSV file.
     * @param filePath The path to the CSV file
     * @return true if load was successful, false otherwise
     */
    private boolean loadUsersFromCSV(String filePath) {
        List<User> loadedUsers = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {

            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = parseCsvLine(line);
                if (fields.length >= 3) {
                    User user = new User();
                    user.userId = unescapeCsvField(fields[0]);
                    user.name = unescapeCsvField(fields[1]);
                    user.password = unescapeCsvField(fields[2]);
                    loadedUsers.add(user);
                }
            }

            userManager.loadData(loadedUsers);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Loads budget data from a CSV file.
     * @param filePath The path to the CSV file
     * @return true if load was successful, false otherwise
     */
    private boolean loadBudgetFromCSV(String filePath) {
        List<Budget> loadedBudgets = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {

            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = parseCsvLine(line);
                if (fields.length >= 5) {
                    Budget budget = new Budget();
                    budget.budgetId = unescapeCsvField(fields[0]);
                    budget.amount = unescapeCsvField(fields[1]);
                    String typeStr = unescapeCsvField(fields[2]);
                    try {
                        budget.type = TransactionType.valueOf(typeStr);
                    } catch (IllegalArgumentException ex) {
                        try {
                            int idx = Integer.parseInt(typeStr);
                            budget.type = TransactionType.values()[idx];
                        } catch (Exception e2) {
                            System.err.println("Invalid TransactionType value: " + typeStr);
                            continue;
                        }
                    }
                    budget.owner = userManager.getUserById(unescapeCsvField(fields[3]));
                    budget.date = unescapeCsvField(fields[4]);
                    loadedBudgets.add(budget);
                }
            }

            budgetManager.loadData(loadedBudgets);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Loads transaction data from a CSV file.
     * @param filePath The path to the CSV file
     * @return true if load was successful, false otherwise
     */
    private boolean loadTransactionsFromCSV(String filePath) {
        List<Transaction> loadedTransactions = new ArrayList<>();

        Map<String, TransactionType> typeMap = new HashMap<>();
        typeMap.put("groceries", TransactionType.groceries);
        typeMap.put("health", TransactionType.health);
        typeMap.put("food", TransactionType.food);
        typeMap.put("income", TransactionType.income);
        typeMap.put("rent", TransactionType.rent);
        typeMap.put("entertainment", TransactionType.entertainment);
        typeMap.put("digitalProduct", TransactionType.digitalProduct);
        typeMap.put("game", TransactionType.game);
        typeMap.put("cosmetics", TransactionType.cosmetics);
        typeMap.put("transportation", TransactionType.transportation);
        typeMap.put("education", TransactionType.education);
        typeMap.put("travel", TransactionType.travel);

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {

            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = parseCsvLine(line);
                if (fields.length >= 8) {
                    Transaction transaction = new Transaction();
                    transaction.transactionId = unescapeCsvField(fields[0]);
                    transaction.date = unescapeCsvField(fields[1]);
                    transaction.amount = unescapeCsvField(fields[2]);
                    transaction.description = unescapeCsvField(fields[3]);
                    String typeStr = unescapeCsvField(fields[4]);
                    try {
                        transaction.type = TransactionType.valueOf(typeStr);
                    } catch (IllegalArgumentException ex) {
                        try {
                            int idx = Integer.parseInt(typeStr);
                            transaction.type = TransactionType.values()[idx];
                        } catch (Exception e2) {
                            System.err.println("Invalid TransactionType value: " + typeStr);
                            continue;
                        }
                    }
                    String ownerId = unescapeCsvField(fields[5]);
                    transaction.owner = userManager.getUserById(ownerId);
                    transaction.isIncome = Boolean.parseBoolean(unescapeCsvField(fields[6]));
                    transaction.location = unescapeCsvField(fields[7]);
                    loadedTransactions.add(transaction);
                }
            }

            transactionManager.loadData(loadedTransactions);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Checks if a string is numeric.
     * @param str The string to check
     * @return true if the string is numeric, false otherwise
     */
    private boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    /**
     * Escapes a field for CSV format.
     * @param field The field to escape
     * @return The escaped field
     */
    private String escapeCsvField(String field) {
        if (field == null) {
            return "";
        }
        if (field.contains(",") || field.contains("\"") || field.contains("\n")) {
            return "\"" + field.replace("\"", "\"\"") + "\"";
        }
        return field;
    }

    /**
     * Parses a line from a CSV file.
     * @param line The line to parse
     * @return Array of fields from the line
     */
    private String[] parseCsvLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean inQuotes = false;

        for (char c : line.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                fields.add(field.toString());
                field = new StringBuilder();
            } else {
                field.append(c);
            }
        }
        fields.add(field.toString());

        return fields.toArray(new String[0]);
    }

    /**
     * Unescapes a field from CSV format.
     * @param field The field to unescape
     * @return The unescaped field
     */
    private String unescapeCsvField(String field) {
        if (field == null || field.isEmpty()) {
            return "";
        }
        if (field.startsWith("\"") && field.endsWith("\"")) {
            return field.substring(1, field.length() - 1).replace("\"\"", "\"");
        }
        return field;
    }
}