package entity;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Transaction {
    public String transactionId;
    private LocalDate date;  // 确保date是LocalDate类型
    public String amount;
    public String description;
    public String location;
    public TransactionType type;
    public User owner;
    public boolean isIncome;

    public Transaction(TransactionType type, BigDecimal amount, String description) {
        this.type = type;
        this.amount = String.valueOf(amount);
        this.description = description;
    }

    // Getter 和 Setter 方法
    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public LocalDate getDate() {  // 返回LocalDate类型
        return date;
    }

    public void setDate(String date) {  // 接受String类型参数，内部转换为LocalDate
        this.date = LocalDate.parse(date);
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = String.valueOf(amount);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public boolean isIncome() {
        return isIncome;
    }

    public void setIncome(boolean income) {
        isIncome = income;
    }
}