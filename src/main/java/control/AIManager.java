package control;

import entity.Budget;
import entity.Transaction;
import entity.TransactionType;
import entity.User;
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
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .build();

    private final Gson gson = new GsonBuilder().create();

    // 单例模式代码
    private static AIManager instance;

    public static AIManager getInstance() {
        if (instance == null) {
            System.out.println("Creating new AIManager instance");
            instance = new AIManager();
        }
        return instance;
    }

    private BudgetManager budgetManager;
    private TransactionManager transactionManager;
    private UserManager userManager;

    @Override
    public void Init() {
        System.out.println("Initializing AIManager...");
        budgetManager = BudgetManager.getInstance();
        transactionManager = TransactionManager.getInstance();
        userManager = UserManager.getInstance();
        System.out.println("AIManager initialized successfully.");
    }

    /**
     * Generate advices using users' transaction and budget data based on type.
     * 
     * @param type Type of advice (e.g., "general", "budget", "holiday")
     * @return Generated advice as a String
     */
    public String generateAdvice(String type) {
        System.out.println("Generating advice for type: " + type);
        String currentUserId = userManager.getCurrentUserId();
        String userName = userManager.getUserName(currentUserId);
        userName = "lisi";
        System.out.println("Using user: " + userName);

        String prompt;
        System.out.println("Building prompt for type: " + type);

        switch (type) {
            case "general":
                System.out.println("Calculating monthly income...");
                double monthlyIncome = calculateMonthlyIncome(userName);
                System.out.println("Calculating monthly spend...");
                Map<String, Double> spendMap = calculateMonthlySpend(userName);
                System.out.println("Getting monthly budget...");
                Map<String, Double> budgets = getMonthlyBudget(userName);
                prompt = buildGeneralAdvicePrompt(userName, monthlyIncome, spendMap, budgets);
                break;
            case "budget":
                System.out.println("Calculating income for budget analysis...");
                double incomeForBudget = calculateMonthlyIncome(userName);
                System.out.println("Calculating spend for budget analysis...");
                Map<String, Double> spendForBudget = calculateMonthlySpend(userName);
                System.out.println("Getting budget data...");
                Map<String, Double> budgetsForBudget = getMonthlyBudget(userName);
                prompt = buildBudgetAnalysisPrompt(userName, incomeForBudget, spendForBudget, budgetsForBudget);
                break;
            case "consumption":
                System.out.println("Calculating yearly spend for consumption analysis...");
                Map<String, List<Double>> yearlySpend = calculateYearlySpend(userName);
                prompt = buildConsumptionAnalysisPrompt(userName, yearlySpend);
                break;
            case "savings":
                System.out.println("Calculating yearly income for savings analysis...");
                Map<String, Double> yearlyIncome = calculateYearlyIncome(userName);
                System.out.println("Calculating yearly spend for savings analysis...");
                Map<String, List<Double>> yearlySpendForSavings = calculateYearlySpend(userName);
                prompt = buildSavingsAnalysisPrompt(userName, yearlyIncome, yearlySpendForSavings);
                break;
            case "longterm":
                System.out.println("Calculating yearly spend for long-term analysis...");
                Map<String, List<Double>> yearlySpendForLongTerm = calculateYearlySpend(userName);
                prompt = buildLongTermAnalysisPrompt(userName, yearlySpendForLongTerm);
                break;
            case "holiday":
                System.out.println("Building holiday advice prompt...");
                prompt = buildHolidayAdvicePrompt(userName);
                break;
            default:
                System.out.println("Using default general advice prompt...");
                prompt = buildGeneralAdvicePrompt(userName, 0, new HashMap<>(), new HashMap<>());
                break;
        }

        System.out.println("Prompt built successfully, length: " + prompt.length());
        System.out.println("Calling DeepSeek API...");
        String result = callDeepSeekAPI(prompt);
        System.out.println("API call completed, result length: " + (result != null ? result.length() : 0));
        return result;
    }

    /**
     * Builds the prompt for general financial advice.
     */
    private String buildGeneralAdvicePrompt(String userName, double monthlyIncome, Map<String, Double> spend,
            Map<String, Double> budgets) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Based on the financial data of user ").append(userName)
                .append(", generate general financial advice.\n");
        prompt.append("The user's monthly income for last month was: $")
                .append(String.format("%.2f", monthlyIncome))
                .append("\n");

        prompt.append("The user's spending in each category last month was: ");
        if (spend.isEmpty()) {
            prompt.append("No spending records");
        } else {
            boolean first = true;
            for (Map.Entry<String, Double> entry : spend.entrySet()) {
                if (!first) {
                    prompt.append(", ");
                }
                prompt.append(entry.getKey())
                        .append(": $")
                        .append(String.format("%.2f", entry.getValue()));
                first = false;
            }
        }
        prompt.append("\n");

        prompt.append("The user's budget for each category last month was: ");
        if (budgets.isEmpty()) {
            prompt.append("No budget records");
        } else {
            boolean first = true;
            for (Map.Entry<String, Double> entry : budgets.entrySet()) {
                if (!first) {
                    prompt.append(", ");
                }
                prompt.append(entry.getKey())
                        .append(": $")
                        .append(String.format("%.2f", entry.getValue()));
                first = false;
            }
        }
        prompt.append("\n");
        prompt.append(
                "Please provide concise financial advice based on this data. Format the response as a single paragraph in English. Do not include any markdown formatting.");

        return prompt.toString();
    }

    /**
     * Builds the prompt for budget analysis advice.
     */
    private String buildBudgetAnalysisPrompt(String userName, double monthlyIncome, Map<String, Double> spend,
            Map<String, Double> budgets) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Based on the financial data of user ").append(userName)
                .append(", generate budget analysis advice.\n");
        prompt.append("The user's monthly income for last month was: $")
                .append(String.format("%.2f", monthlyIncome))
                .append("\n");

        prompt.append("The user's spending in each category last month was: ");
        if (spend.isEmpty()) {
            prompt.append("No spending records");
        } else {
            boolean first = true;
            for (Map.Entry<String, Double> entry : spend.entrySet()) {
                if (!first) {
                    prompt.append(", ");
                }
                prompt.append(entry.getKey())
                        .append(": $")
                        .append(String.format("%.2f", entry.getValue()));
                first = false;
            }
        }
        prompt.append("\n");

        prompt.append("The user's budget for each category last month was: ");
        if (budgets.isEmpty()) {
            prompt.append("No budget records");
        } else {
            boolean first = true;
            for (Map.Entry<String, Double> entry : budgets.entrySet()) {
                if (!first) {
                    prompt.append(", ");
                }
                prompt.append(entry.getKey())
                        .append(": $")
                        .append(String.format("%.2f", entry.getValue()));
                first = false;
            }
        }
        prompt.append("\n");
        prompt.append(
                "Please compare the user's actual spending with their budget, analyze the budget execution, identify categories that are over budget or well controlled, and provide specific budget optimization suggestions. Format the response as a single paragraph in English. Do not include any markdown formatting.");

        return prompt.toString();
    }

    /**
     * Builds the prompt for holiday spending advice.
     */
    private String buildHolidayAdvicePrompt(String userName) {
        // Note: AIManager doesn't have knowledge of future holidays or typical holiday
        // spending items.
        // This prompt asks the AI to provide general advice based on common knowledge.
        StringBuilder prompt = new StringBuilder();
        prompt.append(
                "Please provide financial advice for the past three holidays based on the user's spending data.\n");
        prompt.append(
                "Please list the names of the past three holidays and provide possible related spending items and budget planning (amounts are examples, can be estimated).\n");
        prompt.append(
                "Please provide the advice in a concise English paragraph. Do not include any markdown formatting.");
        // Optionally, add some context about user's past spending if relevant data
        // structures are available

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
                System.err.println("Amount or date format error - Amount: " + transaction.amount
                        + ", Date: " + transaction.date);
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
                    String typeName = (type != null) ? type.name() : "Unknown";
                    double amount = Double.parseDouble(transaction.amount);
                    categorySpending.merge(typeName, amount, Double::sum);
                }
            } catch (NumberFormatException e) {
                System.err.println("Amount or date format error - Amount: " + transaction.amount
                        + ", Date: " + transaction.date);
            } catch (Exception e) {
                System.err
                        .println("Error processing transaction: " + transaction.transactionId + " - " + e.getMessage());
            }
        }
        return categorySpending;
    }

    /**
     * Get the monthly budget of the user for the previous month.
     * 
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
                    // Check if type is not null before calling name()
                    String category = (budget.type != null) ? budget.type.name() : "Unknown";
                    double amount = Double.parseDouble(budget.amount);
                    monthlyBudget.put(category, amount);
                }
            } catch (NumberFormatException e) {
                System.err.println("Budget amount or date format error - Amount: " + budget.amount
                        + ", Date: " + budget.date);
            } catch (Exception e) {
                System.err.println("Error processing budget: " + budget.budgetId + " - " + e.getMessage());
            }
        }
        return monthlyBudget;
    }

    /**
     * Get the monthly spending of the user for the last 12 months.
     */
    private Map<String, List<Double>> calculateYearlySpend(String userName) {
        List<Transaction> transactions = transactionManager.queryByOwner(userName);
        Map<String, List<Double>> categorySpending = new HashMap<>();
        YearMonth currentYearMonth = YearMonth.now();

        // Initialize the map with empty lists for each month
        for (int i = 0; i < 12; i++) {
            YearMonth month = currentYearMonth.minusMonths(i);
            for (TransactionType type : TransactionType.values()) {
                String key = month.getYear() + "-" + String.format("%02d", month.getMonthValue()) + "-" + type.name();
                categorySpending.put(key, new ArrayList<>());
            }
        }

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
                YearMonth transactionMonth = YearMonth.of(year, month);

                // Only process transactions from the last 12 months
                if (transactionMonth.isAfter(currentYearMonth.minusMonths(12))
                        && !transactionMonth.isAfter(currentYearMonth)) {
                    TransactionType type = transaction.type;
                    if (type != null) {
                        String key = year + "-" + String.format("%02d", month) + "-" + type.name();
                        double amount = Double.parseDouble(transaction.amount);
                        categorySpending.get(key).add(amount);
                    }
                }
            } catch (NumberFormatException e) {
                System.err.println("Amount or date format error - Amount: " + transaction.amount
                        + ", Date: " + transaction.date);
            } catch (Exception e) {
                System.err
                        .println("Error processing transaction: " + transaction.transactionId + " - " + e.getMessage());
            }
        }
        return categorySpending;
    }

    /**
     * Get the monthly income of the user for the last 12 months.
     */
    private Map<String, Double> calculateYearlyIncome(String userName) {
        List<Transaction> incomes = transactionManager.getIncomeTransactionsByUser(userName);
        Map<String, Double> monthlyIncome = new HashMap<>();
        YearMonth currentYearMonth = YearMonth.now();

        // Initialize the map with zeros for each month
        for (int i = 0; i < 12; i++) {
            YearMonth month = currentYearMonth.minusMonths(i);
            String key = month.getYear() + "-" + String.format("%02d", month.getMonthValue());
            monthlyIncome.put(key, 0.0);
        }

        for (Transaction transaction : incomes) {
            try {
                String[] dateParts = transaction.date.split("-");
                if (dateParts.length != 3)
                    continue;
                int year = Integer.parseInt(dateParts[0]);
                int month = Integer.parseInt(dateParts[1]);
                YearMonth transactionMonth = YearMonth.of(year, month);

                // Only process transactions from the last 12 months
                if (transactionMonth.isAfter(currentYearMonth.minusMonths(12))
                        && !transactionMonth.isAfter(currentYearMonth)) {
                    String key = year + "-" + String.format("%02d", month);
                    double amount = Double.parseDouble(transaction.amount);
                    monthlyIncome.merge(key, amount, Double::sum);
                }
            } catch (NumberFormatException e) {
                System.err.println("Amount or date format error - Amount: " + transaction.amount
                        + ", Date: " + transaction.date);
            }
        }
        return monthlyIncome;
    }

    /**
     * Builds the prompt for consumption habit analysis.
     */
    private String buildConsumptionAnalysisPrompt(String userName, Map<String, List<Double>> yearlySpend) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Analyze the consumption habits of user ").append(userName)
                .append(" based on their spending data from the last 12 months.\n");

        // Limit the data to last 3 months to reduce prompt size
        prompt.append("Recent spending by category (last 3 months):\n");
        int count = 0;
        for (Map.Entry<String, List<Double>> entry : yearlySpend.entrySet()) {
            if (count >= 3)
                break;
            String[] parts = entry.getKey().split("-");
            String month = parts[0] + "-" + parts[1];
            String category = parts[2];
            double total = entry.getValue().stream().mapToDouble(Double::doubleValue).sum();
            prompt.append(month).append(" - ").append(category).append(": $")
                    .append(String.format("%.2f", total)).append("\n");
            count++;
        }

        prompt.append("\nPlease analyze the user's consumption habits, including:\n");
        prompt.append("1. Spending structure analysis (which categories have higher proportions)\n");
        prompt.append("2. Consumption patterns (impulse spending, fixed expenses, etc.)\n");
        prompt.append("3. Suggestions for improving consumption habits\n");
        prompt.append(
                "Please provide the analysis in a concise English paragraph. Do not include any markdown formatting.");

        return prompt.toString();
    }

    /**
     * Builds the prompt for savings potential analysis.
     */
    private String buildSavingsAnalysisPrompt(String userName, Map<String, Double> yearlyIncome,
            Map<String, List<Double>> yearlySpend) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Analyze the savings potential of user ").append(userName)
                .append(" based on their income and spending data from the last 12 months.\n");

        // Limit the data to last 3 months to reduce prompt size
        prompt.append("Recent monthly income (last 3 months):\n");
        int count = 0;
        for (Map.Entry<String, Double> entry : yearlyIncome.entrySet()) {
            if (count >= 3)
                break;
            prompt.append(entry.getKey()).append(": $")
                    .append(String.format("%.2f", entry.getValue())).append("\n");
            count++;
        }

        prompt.append("\nRecent spending by category (last 3 months):\n");
        count = 0;
        for (Map.Entry<String, List<Double>> entry : yearlySpend.entrySet()) {
            if (count >= 3)
                break;
            String[] parts = entry.getKey().split("-");
            String month = parts[0] + "-" + parts[1];
            String category = parts[2];
            double total = entry.getValue().stream().mapToDouble(Double::doubleValue).sum();
            prompt.append(month).append(" - ").append(category).append(": $")
                    .append(String.format("%.2f", total)).append("\n");
            count++;
        }

        prompt.append("\nPlease analyze the user's savings potential, including:\n");
        prompt.append("1. Current savings rate analysis\n");
        prompt.append("2. Optimizable expense items\n");
        prompt.append("3. Specific suggestions for increasing savings rate\n");
        prompt.append(
                "Please provide the analysis in a concise English paragraph. Do not include any markdown formatting.");

        return prompt.toString();
    }

    /**
     * Builds the prompt for long-term trend analysis.
     */
    private String buildLongTermAnalysisPrompt(String userName, Map<String, List<Double>> yearlySpend) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Analyze the long-term financial trends of user ").append(userName)
                .append(" based on their spending data from the last 12 months.\n");

        // Limit the data to last 3 months to reduce prompt size
        prompt.append("Recent spending by category (last 3 months):\n");
        int count = 0;
        for (Map.Entry<String, List<Double>> entry : yearlySpend.entrySet()) {
            if (count >= 3)
                break;
            String[] parts = entry.getKey().split("-");
            String month = parts[0] + "-" + parts[1];
            String category = parts[2];
            double total = entry.getValue().stream().mapToDouble(Double::doubleValue).sum();
            prompt.append(month).append(" - ").append(category).append(": $")
                    .append(String.format("%.2f", total)).append("\n");
            count++;
        }

        prompt.append("\nPlease analyze the user's long-term financial trends, including:\n");
        prompt.append("1. Impact of spending structure on long-term financial health\n");
        prompt.append("2. Potential financial risks\n");
        prompt.append("3. Long-term financial planning suggestions\n");
        prompt.append(
                "Please provide the analysis in a concise English paragraph. Do not include any markdown formatting.");

        return prompt.toString();
    }

    private String callDeepSeekAPI(String prompt) {
        try {
            System.out.println("Preparing API request...");
            JsonObject requestBody = new JsonObject();
            requestBody.addProperty("model", "deepseek-v3");
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

            System.out.println("Sending request to API...");
            System.out.println("Request URL: " + DEEPSEEK_API_URL);
            System.out.println("Request body: " + gson.toJson(requestBody));

            Request request = new Request.Builder()
                    .url(DEEPSEEK_API_URL)
                    .addHeader("Authorization", "Bearer " + API_KEY)
                    .addHeader("Content-Type", "application/json")
                    .post(RequestBody.create(gson.toJson(requestBody), JSON))
                    .build();

            System.out.println("Executing API call...");
            try (Response response = httpClient.newCall(request).execute()) {
                System.out.println("Received response from API. Status code: " + response.code());

                if (!response.isSuccessful()) {
                    String errorBody = response.body().string();
                    System.err.println("API call failed: " + response.code() + " - " + errorBody);
                    throw new IOException("API call failed: " + response.code() + " - " + errorBody);
                }

                String responseBody = response.body().string();
                System.out.println("Response body received, length: " + responseBody.length());
                System.out.println("Response body: " + responseBody);

                JsonObject jsonResponse = gson.fromJson(responseBody, JsonObject.class);
                System.out.println("Parsed JSON response");

                JsonArray choices = jsonResponse.getAsJsonArray("choices");
                if (choices != null && choices.size() > 0) {
                    JsonObject firstChoice = choices.get(0).getAsJsonObject();
                    JsonObject message = firstChoice.getAsJsonObject("message");
                    if (message != null && message.has("content")) {
                        String content = message.get("content").getAsString();
                        System.out.println("Successfully extracted content from response");
                        return content;
                    }
                }

                System.err.println("Failed to extract content from response: " + responseBody);
                return "Failed to get advice from AI.";
            }
        } catch (IOException e) {
            System.err.println("IO Exception during API call: " + e.getMessage());
            e.printStackTrace();
            return "Error communicating with AI service: " + e.getMessage();
        } catch (Exception e) {
            System.err.println("Unexpected error during API call: " + e.getMessage());
            e.printStackTrace();
            return "An unexpected error occurred: " + e.getMessage();
        }
    }

    public String getInferredTypeAndDesc(List<String> infos) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("下面是一系列用户的消费报告行为，每个逗号分隔了一个条目，每个条目中用-分隔依次是交易类型，交易对方和商品。")
                .append("现在根据每个条目的信息,从groceries,health,food,rent,entertainment,income,digitalProduct,cosmetics,travel,transportation,education,game中推测出交易类型和描述,严格从上面列举出的几种类型中选择类型,不要出现任何不在上面出现的单词。描述要求用英文输出,每个描述只有一句话。每个条目的类型和描述之间用-分隔，每个条目之间用逗号分隔。除了我要求的内容以外不要添加任何描述性的语句,不要添加任何不符合我上述格式的语句。两个条目之间有且仅有一个逗号进行分割,不要添加额外的空格。\n");
        for (String string : infos) {
        prompt.append(string).append(",");
        }
        String res = callDeepSeekAPI(prompt.toString());
        System.out.println(res);
        return res;
    }
}