package control;

import entity.Budget;
import entity.Transaction;
import entity.TransactionType;
import okhttp3.*;
import com.google.gson.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class AIManager extends Manager {
    // 配置参数
    private static final String DEEPSEEK_API_URL = "https://chat.zju.edu.cn/api/ai/v1/chat/completions";
    private static final String API_KEY = System.getenv("DEEPSEEK_API_KEY");
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private final OkHttpClient httpClient = new OkHttpClient();
    private final Gson gson = new GsonBuilder().create();

    // 单例模式代码
    private static AIManager instance;
    public static AIManager getInstance() {
        if (instance == null)
            instance = new AIManager();
        return instance;
    }

    private BudgetManager budgetManager;
    private TransactionManager transactionManager;
    private UserManager userManager;

    // 添加 setter 方法
    public void setBudgetManager(BudgetManager budgetManager) {
        this.budgetManager = budgetManager;
    }

    public void setTransactionManager(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    @Override
    public void Init() {
        budgetManager = BudgetManager.getInstance();
        transactionManager = TransactionManager.getInstance();
        userManager = UserManager.getInstance();
        System.out.println("AIManager initialized.");
    }

    // 生成综合建议（使用API）
    public String generateAdvice() {
        String currentUserId = userManager.getCurrentUserId();
        String userName = userManager.getUserName(currentUserId);
        System.out.println("当前用户姓名: " + userName);

        // 根据用户名获取该用户的收入交易记录
        List<Transaction> incomeTransactions = transactionManager.getIncomeTransactionsByUser(userName);
        // 计算用户月收入
        double monthlyIncome = calculateMonthlyIncome(incomeTransactions);

        // 根据用户名获取该用户的所有交易记录
        List<Transaction> transactions = transactionManager.getTransactionsByUserName(userName);
        // 根据用户名获取该用户的预算记录
        List<Budget> budgets = budgetManager.getBudgetsByUserName(userName);

        String prompt = buildPrompt(monthlyIncome, transactions, budgets);
        return callDeepSeekAPI(prompt);
    }

    // 构建 API 请求的提示信息
    private String buildPrompt(double monthlyIncome, List<Transaction> transactions, List<Budget> budgets) {
        return String.format("用户月收入：%.2f，近期消费记录：%s，预算记录：%s。请生成包含以下内容的建议："
                + "1. 基于消费习惯的预算分配 2. 合理的储蓄目标 3. 具体节省建议。使用中文Markdown格式。",
                monthlyIncome, formatTransactions(transactions), formatBudgets(budgets));
    }

    // 计算用户月收入
    private double calculateMonthlyIncome(List<Transaction> incomeTransactions) {
        double totalIncome = 0;
        for (Transaction transaction : incomeTransactions) {
            try {
                totalIncome += Double.parseDouble(transaction.amount);
            } catch (NumberFormatException e) {
                // 处理金额格式错误
                System.err.println("金额格式错误: " + transaction.amount);
            }
        }
        return totalIncome;
    }

    // 辅助方法：格式化预算数据
    private String formatBudgets(List<Budget> budgets) {
        return budgets.stream()
               .map(this::formatSingleBudget)
               .collect(Collectors.joining("\n"));
    }

    // 格式化单个预算数据
    private String formatSingleBudget(Budget budget) {
        try {
            // 添加调试信息
            System.out.println("原始预算金额: " + budget.amount);
            double amount = Double.parseDouble(budget.amount);
            System.out.println("转换后的预算金额: " + amount);
            return String.format("[食品预算: ¥%.2f, 交通预算: ¥%.2f, 住房预算: ¥%.2f]",
                    amount, 0.0, 0.0); // 这里假设交通和住房预算为 0，可根据实际情况修改
        } catch (NumberFormatException e) {
            System.err.println("预算金额格式错误: " + budget.amount);
            return "[预算金额格式错误]";
        }
    }

    // 辅助方法：格式化交易数据
    private String formatTransactions(List<Transaction> transactions) {
        return transactions.stream()
               .map(this::formatSingleTransaction)
               .collect(Collectors.joining("\n"));
    }

    // 格式化单个交易数据
    private String formatSingleTransaction(Transaction transaction) {
        try {
            double amount = Double.parseDouble(transaction.amount);
            return String.format("[%s: ¥%.2f - %s]",
                    transaction.type, amount, transaction.description);
        } catch (NumberFormatException e) {
            System.err.println("交易金额格式错误: " + transaction.amount);
            return "[交易金额格式错误]";
        }
    }

    private String callDeepSeekAPI(String prompt) {
        String apiKey = System.getenv("DEEPSEEK_API_KEY");
        System.out.println("使用的 API 密钥: " + apiKey); // 添加调试信息
        if (apiKey == null || apiKey.isEmpty()) {
            throw new RuntimeException("API 密钥未设置，请检查 DEEPSEEK_API_KEY 环境变量。");
        }
        try {
            JsonObject requestBody = new JsonObject();
            requestBody.addProperty("model", "deepseek-r1-distill-qwen");
            JsonArray messages = new JsonArray();

            JsonObject systemMessage = new JsonObject();
            systemMessage.addProperty("role", "system");
            systemMessage.addProperty("content", "You are a helpful assistant.");
            messages.add(systemMessage);

            JsonObject userMessage = new JsonObject();
            userMessage.addProperty("role", "user");
            userMessage.addProperty("content", prompt);
            messages.add(userMessage);

            requestBody.add("messages", messages);
            // 删除了流式响应的设置：requestBody.addProperty("stream", true);

            Request request = new Request.Builder()
                .url(DEEPSEEK_API_URL)
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(gson.toJson(requestBody), JSON))
                .build();

            try (Response response = httpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    String errorBody = response.body() != null ? response.body().string() : "无错误信息";
                    System.err.println("API 请求失败，响应码: " + response.code() + ", 错误信息: " + errorBody);
                    throw new IOException("Unexpected code " + response + ", 错误信息: " + errorBody);
                }

                // 删除了流式响应的处理逻辑，直接获取整个响应体
                if (response.body() != null) {
                    String responseBody = response.body().string();
                    JsonObject jsonResponse = gson.fromJson(responseBody, JsonObject.class);
                    JsonArray choices = jsonResponse.getAsJsonArray("choices");
                    if (choices.size() > 0) {
                        JsonObject choice = choices.get(0).getAsJsonObject();
                        return choice.get("message").getAsJsonObject().get("content").getAsString();
                    } else {
                        System.err.println("API 响应中没有找到有效的建议内容。");
                        return null;
                    }
                } else {
                    System.err.println("未从 API 响应中获取到有效内容。");
                    return null;
                }
            }
        } catch (IOException e) {
            System.err.println("API 调用时发生 I/O 错误: " + e.getMessage());
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            System.err.println("API 调用时发生未知错误: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    } 
}