package entity;

public enum TransactionType {
    GROCERIES("Groceries"),
    HEALTH("Health"),
    FOOD("Food"),
    RENT("Rent"),
    ENTERTAINMENT("Entertainment"),
    SHOPPING("Shopping"),
    OTHER("Other");

    private final String description;

    TransactionType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}