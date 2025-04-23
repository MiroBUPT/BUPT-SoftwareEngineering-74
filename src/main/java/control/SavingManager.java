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
import java.util.List;

import entity.Budget;
import entity.Transaction;
import entity.TransactionType;
import entity.User;
import control.BudgetManager;
import control.Manager;
import control.TransactionManager;
import control.UserManager;

public class SavingManager extends Manager {
    private static SavingManager instance;
    private static boolean isInitialized = false;

    public static SavingManager getInstance() {
        if (instance == null)
            instance = new SavingManager();
        return instance;
    }

    private BudgetManager budgetManager;
    private TransactionManager transactionManager;
    private UserManager userManager;
    private static String userFilePath = "src/main/resources/user.csv";
    private static String budgetFilePath = "src/main/resources/budget.csv";
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

    public void saveData() {
        saveUsersToCSV();
        saveBudgetsToCSV();
        saveTransactionsToCSV();
    }

    public void loadData() {
        loadUsersFromCSV(userFilePath);
        loadBudgetFromCSV(budgetFilePath);
        loadTransactionsFromCSV(transactionFilePath);
    }

    public boolean saveUsersToCSV() {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(userFilePath), StandardCharsets.UTF_8))) {
            var userList = userManager.getUserList();
            // 写入CSV表头
            writer.write("userId,name,password");
            writer.newLine();

            // 写入每个用户的数据
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

    public boolean saveBudgetsToCSV() {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(budgetFilePath), StandardCharsets.UTF_8))) {
            var budgetList = budgetManager.getBudgetList();
            // 写入CSV表头
            writer.write("budgetId,amount,type,owner,date");
            writer.newLine();

            // 写入每个用户的数据
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

    // 保存交易数据到 CSV 文件
    public boolean saveTransactionsToCSV() {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(transactionFilePath), StandardCharsets.UTF_8))) {
            var transactionList = transactionManager.getTransactionList();
            // 写入 CSV 表头
            writer.write("transactionId,date,amount,description,type,owner,isIncome,location");
            writer.newLine();

            // 写入每个交易的数据
            for (Transaction transaction : transactionList) {
                String line = String.format("%s,%s,%s,%s,%s,%s,%s,%s",
                        escapeCsvField(transaction.transactionId),
                        escapeCsvField(transaction.date),
                        escapeCsvField(transaction.amount),
                        escapeCsvField(transaction.description),
                        escapeCsvField(String.valueOf(transaction.type.ordinal())),
                        escapeCsvField(transaction.owner.userId),
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
     * 从CSV文件加载用户列表
     * 
     * @param filePath 文件路径
     * @return 是否加载成功
     */
    private boolean loadUsersFromCSV(String filePath) {
        List<User> loadedUsers = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {

            // 跳过表头
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
     * Load Budget data from a CSV file.
     * 
     * @param filePath file path of the CSV file
     * @return whether the loading was successful
     */
    private boolean loadBudgetFromCSV(String filePath) {
        List<Budget> loadedBudgets = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {

            // 跳过表头
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = parseCsvLine(line);
                if (fields.length >= 5) {
                    Budget budget = new Budget();
                    budget.budgetId = unescapeCsvField(fields[0]);
                    budget.amount = unescapeCsvField(fields[1]);
                    budget.type = TransactionType.valueOf(unescapeCsvField(fields[2]));
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

    // 从 CSV 文件加载交易数据
    private boolean loadTransactionsFromCSV(String filePath) {
        List<Transaction> loadedTransactions = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {

            // 跳过表头
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
                    transaction.type = TransactionType.values()[Integer.parseInt(unescapeCsvField(fields[4]))];
                    transaction.owner = userManager.getUserById(unescapeCsvField(fields[5]));
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

    // CSV字段转义处理（处理包含逗号或引号的情况）
    private String escapeCsvField(String field) {
        if (field == null) {
            return "";
        }
        if (field.contains(",") || field.contains("\"")) {
            return "\"" + field.replace("\"", "\"\"") + "\"";
        }
        return field;
    }

    // CSV字段解析
    private String[] parseCsvLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean inQuotes = false;

        for (char c : line.toCharArray()) {
            if (c == ',' && !inQuotes) {
                fields.add(field.toString());
                field.setLength(0);
            } else if (c == '"') {
                inQuotes = !inQuotes;
            } else {
                field.append(c);
            }
        }
        fields.add(field.toString());
        return fields.toArray(new String[0]);
    }

    // CSV字段反转义
    private String unescapeCsvField(String field) {
        if (field.startsWith("\"") && field.endsWith("\"")) {
            field = field.substring(1, field.length() - 1);
            field = field.replace("\"\"", "\"");
        }
        return field;
    }
}
