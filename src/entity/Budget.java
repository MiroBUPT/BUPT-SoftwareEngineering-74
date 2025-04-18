package entity;

public class Budget {
    public String budgetId;
    public String amount;
    public String date;
    public TransactionType type;
    public User owner;


    public Budget(String budgetId, String amount, TransactionType type, User owner, String date) {
        this.budgetId = budgetId;
        this.amount = amount;
        this.type = type;
        this.owner = owner;
        this.date = date;
    }

    public String getBudgetId() {
        return budgetId;
    }

    public String getAmount() {
        return amount;
    }

    public TransactionType getType() {
        return type;
    }

    public User getOwner() {
        return owner;
    }

    public String getDate() {
        return date;
    }
}
