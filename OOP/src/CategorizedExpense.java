import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * A concrete class representing a categorized expense.
 * It now implements both the Summarizable and Printable interfaces.
 */
public class CategorizedExpense extends BaseExpense implements Summarizable, Printable {
    private String category;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Constructor for creating a new categorized expense.
     */
    public CategorizedExpense(double amount, String category, String description, LocalDate date) {
        super(amount, description, date);
        this.category = category;
    }

    /**
     * Constructor for creating an expense object from a database record.
     */
    public CategorizedExpense(int id, double amount, String category, String description, LocalDate date) {
        super(id, amount, description, date);
        this.category = category;
    }

    public String getCategory() 
    { return category; }

    
    /**
     * Implementation of the getSummary() method from the Summarizable interface.
     */
    @Override
    public String getSummary() {
        return String.format("Category: %s, Amount: $%.2f, Description: %s, Date: %s",
                category, amount, description, date.format(FORMATTER));
    }

    
    public String getSummary(String currency) {
        return String.format("Category: %s, Amount: %.2f (%s), Description: %s, Date: %s",
                category, amount, currency, description, date.format(FORMATTER));
    }

    /**
     * 
     * Implementation of the printDetails() method from the Printable interface.
     * This will print to the console for demonstration purposes.
     */
    @Override
    public void printDetails() {
        System.out.printf("Printing details for Expense ID %d:%n", this.id);
        System.out.printf("  Amount: $%.2f%n", this.amount);
        System.out.printf("  Category: %s%n", this.category);
        System.out.printf("  Description: %s%n", this.description);
        System.out.printf("  Date: %s%n", this.date.format(FORMATTER));
    }

    @Override
    public String toString() {
        return String.format("ID: %d, Amount: $%.2f, Category: %s, Description: %s, Date: %s",
                id, amount, category, description, date);
    }
}
