
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


class Expense {
    private static int nextId = 1; 
    private int id;
    private double amount;
    private String category;
    private String description;
    private LocalDate date;

  
    public Expense(double amount, String category, String description, LocalDate date) {
        this.id = nextId++;
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.date = date;
    }

   
    public int getId() { return id; }
    public double getAmount() { return amount; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public LocalDate getDate() { return date; }

    /**
     * Overrides the default toString method to provide a formatted string representation of an Expense.
     * This is an example of method overriding (polymorphism).
     * @return A formatted string of the expense details.
     */
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return String.format("ID: %-4d | Date: %s | Amount: %-10.2f | Category: %-12s | Description: %s",
                             id, date.format(formatter), amount, category, description);
    }
}
