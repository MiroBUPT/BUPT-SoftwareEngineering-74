package entity;

/**
 * Represents a budget allocation in the system.
 * Defines spending limits for different transaction categories over a specific time period.
 */
public class Budget {
    /** Unique identifier for the budget */
    public String budgetId;
    /** Maximum amount allocated for this budget */
    public String amount;
    /** Category or type of transactions this budget applies to */
    public TransactionType type;
    /** User who owns or is associated with this budget */
    public User owner;
    /** Time period this budget applies to */
    public String date;
}
