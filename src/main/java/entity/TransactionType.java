package entity;

/**
 * Enum representing different types of transactions in the financial management system.
 * Each type represents a category of financial activity.
 */
public enum TransactionType {
    /** Default or unspecified transaction type */
    none,
    /** Transactions related to grocery shopping */
    groceries,
    /** Transactions related to healthcare and medical expenses */
    health,
    /** Transactions related to food and dining */
    food,
    /** Transactions related to housing rent */
    rent,
    /** Transactions related to entertainment and leisure activities */
    entertainment,
    /** Transactions representing income or earnings */
    income,
    /** Transactions related to digital products and services */
    digitalProduct,
    /** Transactions related to cosmetics and personal care */
    cosmetics,
    /** Transactions related to travel expenses */
    travel,
    /** Transactions related to transportation and commuting */
    transportation,
    /** Transactions related to education and learning */
    education,
    /** Transactions related to gaming and digital entertainment */
    game;
}
