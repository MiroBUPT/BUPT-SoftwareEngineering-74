package entity;

public class Transaction {
    public String transactionId;
    public String date;
    public String amount;
    public String description;
    public TransactionType type;
    public User owner;
    public boolean isIncome;
    public String location;
}
