package control;

import java.util.ArrayList;
import java.util.List;

import entity.User;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class UserManager extends Manager {
    private static UserManager instance;
    Private static String filePath = "users.csv";

    public static UserManager getInstance() {
        if (instance == null)
            instance = new UserManager();
        return instance;
    }

    private String currentUserId;
    private List<User> userList = new ArrayList<>();

    @Override
    public void Init() {
        System.out.println("UserManager initialized.");
    }

    /**
     * 根据用户ID获取用户名
     * @param userId 用户ID
     * @return 用户名，如果用户不存在则返回null
     */
    public String getUserName(String userId) {
        for (User user : userList) {
            if (user.userId.equals(userId)) {
                return user.name;
            }
        }
        return null;
    }

    /**
     * 修改用户密码
     * @param userId 用户ID
     * @param password 新密码
     * @return 是否修改成功
     */
    public boolean editPassword(String userId, String password) {
        for (User user : userList) {
            if (user.userId.equals(userId)) {
                user.password = password;
                return true;
            }
        }
        return false;
    }

    /**
     * 修改用户名
     * @param userId 用户ID
     * @param userName 新用户名
     * @return 是否修改成功
     */
    public boolean editUserName(String userId, String userName) {
        for (User user : userList) {
            if (user.userId.equals(userId)) {
                user.name = userName;
                return true;
            }
        }
        return false;
    }

    /**
     * 加载用户数据
     * @param users 用户列表
     */
    public void loadData(List<User> users) {
        userList.clear();
        userList.addAll(users);
    }

    /**
     * 获取当前登录用户ID
     * @return 当前用户ID
     */
    public String getCurrentUserId() {
        return currentUserId;
    }

    /**
     * 设置当前登录用户
     * @param userId 用户ID
     */
    public void setCurrentUser(String userId) {
        this.currentUserId = userId;
    }

    /**
     * 添加新用户
     * @param user 要添加的用户对象
     * @return 是否添加成功（如果用户ID已存在则失败）
     */
    public boolean addUser(User user) {
        for (User u : userList) {
            if (u.userId.equals(user.userId)) {
                return false;
            }
        }
        userList.add(user);
        return true;
    }

    /**
     * 根据用户ID获取用户对象
     * @param userId 用户ID
     * @return 用户对象，如果不存在则返回null
     */
    public User getUserById(String userId) {
        for (User user : userList) {
            if (user.userId.equals(userId)) {
                return user;
            }
        }
        return null;
    }
}

public boolean saveUsersToCSV(String filePath) {
    try (BufferedWriter writer = new BufferedWriter(
            new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8))) {
        
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
 * @param filePath 文件路径
 * @return 是否加载成功
 */
public boolean loadUsersFromCSV(String filePath) {
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
        
        this.userList = loadedUsers;
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