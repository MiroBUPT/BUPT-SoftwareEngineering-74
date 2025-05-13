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
                // 获取用户名，如果找不到则使用userId
                String ownerName = transaction.owner != null && transaction.owner.name != null ? 
                                  transaction.owner.name : 
                                  (transaction.owner != null ? transaction.owner.userId : "unknown");
                
                String line = String.format("%s,%s,%s,%s,%s,%s,%s,%s",
                        escapeCsvField(transaction.transactionId),
                        escapeCsvField(transaction.date),
                        escapeCsvField(transaction.amount),
                        escapeCsvField(transaction.description),
                        escapeCsvField(String.valueOf(transaction.type.ordinal())),
                        escapeCsvField(ownerName), // 使用用户名而非用户ID
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

    private boolean loadTransactionsFromCSV(String filePath) {
    List<Transaction> loadedTransactions = new ArrayList<>();

    // 定义一个映射表，将字符串映射到 TransactionType 枚举值
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

        // 跳过表头
        reader.readLine();

        String line;
        while ((line = reader.readLine()) != null) {
            String[] fields = parseCsvLine(line);
            if (fields.length >= 8) {
                Transaction transaction = new Transaction();
                transaction.transactionId = unescapeCsvField(fields[0]);
                transaction.date = unescapeCsvField(fields[1]);

                // 验证金额是否为数字
                String amountStr = unescapeCsvField(fields[2]);
                if (!isNumeric(amountStr)) {
                    System.err.println("Invalid numeric value for amount: " + amountStr);
                    continue;
                }
                transaction.amount = amountStr;

                transaction.description = unescapeCsvField(fields[3]);

                // 验证 TransactionType 是否有效
                String typeStr = unescapeCsvField(fields[4]);
                try {
                    // 尝试将type值解析为枚举索引
                    int typeOrdinal = Integer.parseInt(typeStr);
                    if (typeOrdinal >= 0 && typeOrdinal < TransactionType.values().length) {
                        transaction.type = TransactionType.values()[typeOrdinal];
                    } else {
                        // 如果索引越界，尝试使用名称映射
                        TransactionType type = typeMap.get(typeStr.toLowerCase());
                        if (type == null) {
                            System.err.println("Invalid TransactionType value: " + typeStr);
                            continue;
                        }
                        transaction.type = type;
                    }
                } catch (NumberFormatException e) {
                    // 如果不是数字，尝试使用名称映射
                    TransactionType type = typeMap.get(typeStr.toLowerCase());
                    if (type == null) {
                        System.err.println("Invalid TransactionType value: " + typeStr);
                        continue;
                    }
                    transaction.type = type;
                }

                // 根据owner字段查找用户，先尝试按用户名查找，再尝试按用户ID查找
                String ownerValue = unescapeCsvField(fields[5]);
                User owner = null;
                
                // 先尝试通过ID查找
                owner = userManager.getUserById(ownerValue);
                
                if (owner == null) {
                    // 如果通过ID找不到，尝试通过名称查找
                    String userId = userManager.getUserIdByName(ownerValue);
                    if (userId != null) {
                        owner = userManager.getUserById(userId);
                    }
                }
                
                if (owner == null) {
                    // 如果仍然找不到，创建一个临时用户
                    owner = new User();
                    owner.userId = "temp_" + ownerValue;
                    owner.name = ownerValue;
                    System.err.println("Creating temporary user for owner: " + ownerValue);
                }
                
                transaction.owner = owner;
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
    
    // 辅助方法：检查字符串是否为数字
    private boolean isNumeric(String str) {
        return str != null && str.matches("-?\\d+(\\.\\d+)?");
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
