package entity;

/**
 * Represents a financial transaction in the system.
 * Contains all relevant information about a single transaction including its type, amount, and metadata.
 */
public class Transaction {
    /** Unique identifier for the transaction */
    public String transactionId;
    /** Date when the transaction occurred */
    public String date;
    /** Amount of money involved in the transaction */
    public String amount;
    /** Detailed description of the transaction */
    public String description;
    /** Category or type of the transaction */
    public TransactionType type;
    /** User who owns or is associated with this transaction */
    public User owner;
    /** Flag indicating whether this is an income transaction (true) or expense (false) */
    public boolean isIncome;
    /** Location where the transaction occurred */
    public String location;
}
