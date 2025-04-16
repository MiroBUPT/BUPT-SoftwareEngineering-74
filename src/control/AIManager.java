package control;

import entity.*;
import okhttp3.*;
import com.google.gson.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class AIManager extends Manager {
    // 配置参数（建议通过环境变量设置）
    private static final String DEEPSEEK_API_URL = "https://api.deepseek.com/v1/chat/completions";
    private static final String API_KEY = System.getenv("DEEPSEEK_API_KEY");
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    
    private final OkHttpClient httpClient = new OkHttpClient();
    private final Gson gson = new GsonBuilder().create();

    // 其他单例模式代码保持不变...
    private static AIManager instance;
    public static AIManager getInstance() {
        if (instance == null)
            instance = new AIManager();
        return instance;
    }

    private BudgetManager budgetManager;
    private TransactionManager transactionManager;
    private UserManager userManager;

    @Override
    public void Init() {
        budgetManager = BudgetManager.getInstance();
        transactionManager = TransactionManager.getInstance();
        userManager = UserManager.getInstance();
        System.out.println("AIManager initialized.");
    }

    // 交易类型预测（使用API）
    public TransactionType predictType(Transaction transaction) {
        String prompt = String.format("请将以下消费描述分类到[FOOD, TRANSPORT, HOUSING, ENTERTAINMENT, SHOPPING, OTHER]中，只需返回类型名称：%s",
            transaction.getDescription());
        
        String response = callDeepSeekAPI(prompt);
        try {
            return TransactionType.valueOf(response.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return TransactionType.OTHER;
        }
    }

    // 生成综合建议（使用API）
    public String generateAdvice() {
        User user = userManager.getCurrentUser();
        List<Transaction> transactions = transactionManager.getMonthlyTransactions(LocalDate.now());
        
        String prompt = String.format("用户月收入：%.2f，近期消费记录：%s。请生成包含以下内容的建议："
            + "1. 基于消费习惯的预算分配 2. 合理的储蓄目标 3. 具体节省建议。使用中文Markdown格式。",
            user.getIncome(), formatTransactions(transactions));
        
        return callDeepSeekAPI(prompt);
    }

    // 预算预测（使用API）
    public Budget predictBudget() {
        List<Transaction> history = transactionManager.getLastThreeMonthsTransactions();
        String prompt = String.format("根据以下消费历史生成预算分配建议（JSON格式）：%s\n"
            + "只需返回{food: number, transport: number, housing: number}格式",
            formatTransactions(history));

        String jsonResponse = callDeepSeekAPI(prompt);
        return parseBudgetResponse(jsonResponse);
    }

    private String callDeepSeekAPI(String prompt) {
        try {
            JsonObject requestBody = new JsonObject();
            requestBody.addProperty("model", "deepseek-chat");
            requestBody.add("messages", new JsonArray().add(new JsonObject()
                .addProperty("role", "user")
                .addProperty("content", prompt)
            ));

            Request request = new Request.Builder()
                .url(DEEPSEEK_API_URL)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .post(RequestBody.create(gson.toJson(requestBody), JSON))
                .build();

            try (Response response = httpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                
                JsonObject responseJson = gson.fromJson(response.body().charStream(), JsonObject.class);
                return responseJson.getAsJsonArray("choices")
                    .get(0).getAsJsonObject()
                    .getAsJsonObject("message")
                    .get("content").getAsString();
            }
        } catch (Exception e) {
            throw new RuntimeException("API调用失败: " + e.getMessage());
        }
    }

    // 辅助方法
    private String formatTransactions(List<Transaction> transactions) {
        return transactions.stream()
            .map(t -> String.format("[%s: ¥%.2f - %s]", 
                t.getType(), t.getAmount(), t.getDescription()))
            .collect(Collectors.joining("\n"));
    }

    private Budget parseBudgetResponse(String json) {
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
        return new Budget(
            jsonObject.get("food").getAsDouble(),
            jsonObject.get("transport").getAsDouble(),
            jsonObject.get("housing").getAsDouble()
        );
    }
}