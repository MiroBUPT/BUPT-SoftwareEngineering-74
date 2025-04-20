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

import entity.User;

public class SavingManager extends Manager {
    private static SavingManager instance;

    public static SavingManager getInstance() {
        if (instance == null)
            instance = new SavingManager();
        return instance;
    }

    private BudgetManager budgetManager;
    private TransactionManager transactionManager;
    private UserManager userManager;
    private static String userFilePath = "users.csv";

    @Override
    public void Init() {
        budgetManager = BudgetManager.getInstance();
        transactionManager = TransactionManager.getInstance();
        userManager = UserManager.getInstance();
        System.out.println("SavingManager initialized.");
        loadData();
    }

    public void saveData() {

    }

    public void loadData() {
        loadUsersFromCSV(userFilePath);
        budgetManager.loadData(null);
        transactionManager.loadData(null);
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
