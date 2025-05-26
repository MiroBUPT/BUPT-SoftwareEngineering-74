# Personal Finance Management System

A comprehensive personal finance management system that combines traditional budgeting tools with AI-powered financial insights.

## Features

### Transaction Management
- Query transactions by date, owner, location, and type
- Modify transaction types
- View detailed transaction information
- Import transaction data from CSV files

### Budget Management
- Set and modify budgets for different categories
- Visual budget tracking with real-time updates
- Budget execution analysis
- Flexible budget categories

### Data Analysis
- Budget vs. Actual spending comparison
- Expense analysis with interactive charts
- Trend analysis for long-term financial planning
- Visual representation of financial data

### AI-Powered Financial Advice
- General financial advice
- Budget optimization suggestions
- Consumption pattern analysis
- Savings recommendations
- Long-term financial planning
- Holiday spending advice

## Technical Architecture

### Backend
- Four core controllers for business logic:
  - UserManager: User account management
  - BudgetManager: Budget management
  - TransactionManager: Transaction handling
  - SavingManager: Data persistence

### Data Storage
- CSV-based data storage for:
  - User information (user.csv)
  - Budget data (budget.csv)
  - Transaction records (transaction.csv)

### Frontend
- Java Swing-based GUI
- Interactive data visualization
- User-friendly interface
- Real-time updates

## Prerequisites

- Java Development Kit (JDK) 8 or higher
- Maven for dependency management
- Required dependencies:
  - JFreeChart for data visualization
  - OkHttp for API integration
  - Gson for JSON processing

## Installation

1. Clone the repository:
```bash
git clone [repository-url]
```

2. Navigate to the project directory:
```bash
cd personal-finance-management
```

3. Build the project:
```bash
mvn clean install
```

4. Run the application:
```bash
java -jar target/personal-finance-management.jar
```

## Usage

1. Launch the application
2. Log in with your credentials
3. Navigate through different panels:
   - Transaction Query Panel
   - Budget Setup Panel
   - Budget Analysis Panel
   - AI Advice Panel

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   ├── boundary/      # GUI components
│   │   ├── control/       # Business logic controllers
│   │   ├── entity/        # Data models
│   │   └── resources/     # Configuration files
│   └── resources/
│       └── *.csv          # Data files
```

## Contributing

1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

- JFreeChart for visualization components
- OkHttp for API integration
- Gson for JSON processing