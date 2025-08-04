import java.time.LocalDate;

/**
 * An abstract base class for expenses.
 * It contains common fields and methods for all expense types.
 */
public abstract class BaseExpense {
    protected int id;
    protected double amount;
    protected String description;
    protected LocalDate date;

    // Constructor for new expenses (no ID)
    public BaseExpense(double amount, String description, LocalDate date) {
        this.amount = amount;
        this.description = description;
        this.date = date;
    }

    // Constructor for expenses retrieved from the database (with ID)
    public BaseExpense(int id, double amount, String description, LocalDate date) {
        this(amount, description, date);
        this.id = id;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public double getAmount() { return amount; }
    public String getDescription() { return description; }
    public LocalDate getDate() { return date; }

    /**
     * An abstract method that will be implemented by subclasses to provide a summary.
     * @return A string summary of the expense.
     */
    public abstract String getSummary();
}
