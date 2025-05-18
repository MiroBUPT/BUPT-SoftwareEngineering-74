package control;

import entity.Budget;
import entity.Transaction;
import entity.TransactionType;
import okhttp3.*;
import com.google.gson.*;
import java.io.IOException;
import java.time.YearMonth;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class AIManager extends Manager {
    private static final String DEEPSEEK_API_URL = "https://chat.zju.edu.cn/api/ai/v1/chat/completions";
    private static final String API_KEY = "sk-tuY5xrIzl2kJoruU98505161Cf084e348d041c5dA951F9Ca";
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private final OkHttpClient httpClient = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();

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

    @Override
    public void Init() {
        budgetManager = BudgetManager.getInstance();
        transactionManager = TransactionManager.getInstance();
        userManager = UserManager.getInstance();
        System.out.println("AIManager initialized.");
    }

    /**
     * Generate advices using users' transaction and budget data.
     * 
     * @return
     */
    public String generateAdvice() {
        String currentUserId = userManager.getCurrentUserId();
        String userName = userManager.getUserName(currentUserId);
        userName = "lisi";
        System.out.println("当前用户姓名: " + userName);

        double monthlyIncome = calculateMonthlyIncome(userName);
        Map<String, Double> spendMap = calculateMonthlySpend(userName);
        Map<String, Double> budgets = getMonthlyBudget(userName);

        String prompt = buildPrompt(monthlyIncome, spendMap, budgets);
        //return prompt;
        return callDeepSeekAPI(prompt);
    }

    private String buildPrompt(double monthlyIncome, Map<String, Double> spend, Map<String, Double> budgets) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("生成用户的阅读消费建议。该用户的月度收入为：")
                .append(String.format("%.2f", monthlyIncome))
                .append("。\n");

        prompt.append("该用户这个月在各个类别的消费为：");
        if (spend.isEmpty()) {
            prompt.append("无消费记录");
        } else {
            boolean first = true;
            for (Map.Entry<String, Double> entry : spend.entrySet()) {
                if (!first) {
                    prompt.append(",");
                }
                prompt.append(entry.getKey())
                        .append(":")
                        .append(String.format("%.2f", entry.getValue()));
                first = false;
            }
        }
        prompt.append("。\n");

        prompt.append("该用户这个月在各个类别的计划预算为：");
        if (budgets.isEmpty()) {
            prompt.append("无计划预算");
        } else {
            boolean first = true;
            for (Map.Entry<String, Double> entry : budgets.entrySet()) {
                if (!first) {
                    prompt.append(",");
                }
                prompt.append(entry.getKey())
                        .append(":")
                        .append(String.format("%.2f", entry.getValue()));
                first = false;
            }
        }
        prompt.append("。");
        prompt.append("以字符串形式输出而非markdown格式,不要有markdown的语法出现。将建议浓缩为一个自然段输出而非分段形式。");

        return prompt.toString();
    }

    /**
     * Get the monthly income of the user for the previous month.
     * 
     * @param userName the username to query
     * @return total income of the previous month
     */
    private double calculateMonthlyIncome(String userName) {
        double res = 0;
        List<Transaction> incomes = transactionManager.getIncomeTransactionsByUser(userName);
        YearMonth currentYearMonth = YearMonth.now();
        YearMonth lastYearMonth = currentYearMonth.minusMonths(1);
        int lastMonth = lastYearMonth.getMonthValue();
        int lastYear = lastYearMonth.getYear();
        for (Transaction transaction : incomes) {
            try {
                String[] dateParts = transaction.date.split("-");
                if (dateParts.length != 3)
                    continue;
                int year = Integer.parseInt(dateParts[0]);
                int month = Integer.parseInt(dateParts[1]);
                if (year == lastYear && month == lastMonth) {
                    res += Double.parseDouble(transaction.amount);
                }
            } catch (NumberFormatException e) {
                System.err.println("金额或日期格式错误 - 金额: " + transaction.amount
                        + ", 日期: " + transaction.date);
            }
        }
        return res;
    }

    /**
     * Get the monthly spending of the user for the previous month.
     * 
     * @param userName
     * @return
     */
    private Map<String, Double> calculateMonthlySpend(String userName) {
        List<Transaction> transactions = transactionManager.queryByOwner(userName);
        Map<String, Double> categorySpending = new HashMap<>();
        YearMonth currentYearMonth = YearMonth.now();
        YearMonth lastYearMonth = currentYearMonth.minusMonths(1);
        int lastMonth = lastYearMonth.getMonthValue();
        int lastYear = lastYearMonth.getYear();
        for (Transaction transaction : transactions) {
            try {
                if (transaction.isIncome) {
                    continue;
                }

                String[] dateParts = transaction.date.split("-");
                if (dateParts.length != 3) {
                    continue;
                }

                int year = Integer.parseInt(dateParts[0]);
                int month = Integer.parseInt(dateParts[1]);

                if (year == lastYear && month == lastMonth) {
                    TransactionType type = transaction.type;
                    String typeName = type.name();
                    double amount = Double.parseDouble(transaction.amount);
                    categorySpending.merge(typeName, amount, Double::sum);
                }
            } catch (NumberFormatException e) {
                System.err.println("金额或日期格式错误 - 金额: " + transaction.amount
                        + ", 日期: " + transaction.date);
            }
        }
        return categorySpending;
    }

    /**
     * Get the monthly budget of the user for the previous month.
     * @param userName
     * @return
     */
    private Map<String, Double> getMonthlyBudget(String userName) {
        List<Budget> budgets = budgetManager.queryByOwner(userName);
        Map<String, Double> monthlyBudget = new HashMap<>();

        YearMonth currentYearMonth = YearMonth.now();
        YearMonth lastYearMonth = currentYearMonth.minusMonths(1);
        int lastMonth = lastYearMonth.getMonthValue();
        int lastYear = lastYearMonth.getYear();

        for (Budget budget : budgets) {
            try {
                String[] dateParts = budget.date.split("-");
                if (dateParts.length != 3) {
                    continue;
                }

                int year = Integer.parseInt(dateParts[0]);
                int month = Integer.parseInt(dateParts[1]);

                if (year == lastYear && month == lastMonth) {
                    String category = budget.type.name();
                    double amount = Double.parseDouble(budget.amount);
                    monthlyBudget.put(category, amount);
                }
            } catch (NumberFormatException e) {
                System.err.println("预算金额或日期格式错误 - 金额: " + budget.amount
                        + ", 日期: " + budget.date);
            }
        }
        return monthlyBudget;
    }

    private String callDeepSeekAPI(String prompt) {
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

            Request request = new Request.Builder()
                    .url(DEEPSEEK_API_URL)
                    .addHeader("Authorization", "Bearer " + API_KEY)
                    .addHeader("Content-Type", "application/json")
                    .post(RequestBody.create(gson.toJson(requestBody), JSON))
                    .build();

            try (Response response = httpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    String errorBody = response.body() != null ? response.body().string() : "无错误信息";
                    System.err.println("API 请求失败，响应码: " + response.code() + ", 错误信息: " + errorBody);
                    throw new IOException("Unexpected code " + response + ", 错误信息: " + errorBody);
                }

                // 获取整个响应体
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