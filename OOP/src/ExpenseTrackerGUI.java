
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter; 
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors; 
import javax.swing.*;


public class ExpenseTrackerGUI extends JFrame {

    private List<Expense> expenses; 
    private CategoryManager categoryManager; 
    private User currentUser; 
    
    
   
    private ExpenseInputPanel inputPanel;
    private ExpenseTablePanel tablePanel;
    private SummaryPanel summaryPanel;
    private JLabel messageLabel; 

    public ExpenseTrackerGUI() {
        super("Expense Tracker"); 

        expenses = new ArrayList<>(); 
        categoryManager = new CategoryManager(); 
        currentUser = new User(1, "User1"); 

      
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        setLayout(new BorderLayout(10, 10)); 

      
        inputPanel = new ExpenseInputPanel(categoryManager.getAllCategories().toArray(new String[0]));
        tablePanel = new ExpenseTablePanel();
        summaryPanel = new SummaryPanel();

      
        messageLabel = new JLabel("Welcome to Expense Tracker!");
        messageLabel.setForeground(Color.BLACK);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER); 

      
        JPanel topSectionPanel = new JPanel(new BorderLayout());
        topSectionPanel.add(inputPanel, BorderLayout.CENTER); 
        topSectionPanel.add(messageLabel, BorderLayout.SOUTH); 

       
        JPanel middleSectionPanel = new JPanel(new BorderLayout());
        middleSectionPanel.add(tablePanel, BorderLayout.CENTER); 

   
        JPanel bottomSectionPanel = new JPanel(new BorderLayout());
        bottomSectionPanel.add(summaryPanel, BorderLayout.NORTH); 

        JPanel globalButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton globalShowSummaryButton = new JButton("Show Summary");
        JButton globalDeleteSelectedButton = new JButton("Delete Selected Expense");
        globalButtonsPanel.add(globalShowSummaryButton);
        globalButtonsPanel.add(globalDeleteSelectedButton);
        bottomSectionPanel.add(globalButtonsPanel, BorderLayout.SOUTH); 

      
        add(topSectionPanel, BorderLayout.NORTH);
        add(middleSectionPanel, BorderLayout.CENTER);
        add(bottomSectionPanel, BorderLayout.SOUTH);


     
        inputPanel.addAddExpenseListener(e -> addExpenseAction());

        inputPanel.addClearFieldsListener(e -> inputPanel.clearFields());

       
        globalShowSummaryButton.addActionListener(e -> displaySummaryReport());

       
        globalDeleteSelectedButton.addActionListener(e -> deleteSelectedExpense());


        updateExpenseTable();
        updateTotalExpensesLabel();

      
        setVisible(true);
    }

  
    private void clearInputFields() {
        inputPanel.clearFields();
        messageLabel.setText(""); 
    }

   
    private void addExpenseAction() {
        try {
            double amount = Double.parseDouble(inputPanel.getAmountText());
            String categoryName = inputPanel.getSelectedCategory();
            String description = inputPanel.getDescriptionText();

           
            addExpense(amount, categoryName, description);

            messageLabel.setText("Expense added successfully!");
            messageLabel.setForeground(Color.BLUE);
            inputPanel.clearFields(); 
            updateExpenseTable(); 
            updateTotalExpensesLabel(); 

        } catch (NumberFormatException ex) {
            messageLabel.setText("Error: Invalid amount. Please enter a number.");
            messageLabel.setForeground(Color.RED);
            DialogHelper.showError(this, "Please enter a valid number for amount.", "Input Error");
        } catch (InvalidExpenseException ex) {
            messageLabel.setText("Error: " + ex.getMessage());
            messageLabel.setForeground(Color.RED);
            DialogHelper.showError(this, ex.getMessage(), "Input Error");
        }
    }

    /**
     * Overloaded method: Adds a new expense with individual parameters.
     * This method creates an Expense object internally.
     * Demonstrates method overloading (Polymorphism).
     * @param amount The amount of the expense.
     * @param categoryName The category of the expense (as String).
     * @param description The description of the expense.
     * @return The added Expense object.
     * @throws InvalidExpenseException If the expense amount is negative.
     */
    public Expense addExpense(double amount, String categoryName, String description) throws InvalidExpenseException {
        if (amount < 0) {
            throw new InvalidExpenseException("Expense amount cannot be negative.");
        }
       
        Expense newExpense = new Expense(amount, categoryName, description, LocalDate.now());
        expenses.add(newExpense); 
        return newExpense;
    }

    /**
     * Overloaded method: Adds an already constructed Expense object to the list.
     * Demonstrates method overloading (Polymorphism).
     * @param expense The Expense object to add.
     * @throws InvalidExpenseException If the amount of the provided expense is negative.
     */
    public Expense addExpense(Expense expense) throws InvalidExpenseException {
        if (expense.getAmount() < 0) {
            throw new InvalidExpenseException("Expense amount cannot be negative.");
        }
        expenses.add(expense); 
        return expense;
    }

   
    private void deleteSelectedExpense() {
        int selectedRow = tablePanel.getSelectedRow();
        if (selectedRow == -1) {
            messageLabel.setText("Please select an expense to delete.");
            messageLabel.setForeground(Color.ORANGE);
            DialogHelper.showWarning(this, "Please select an expense from the table to delete.", "No Selection");
            return;
        }

        int confirm = DialogHelper.showConfirm(this,
                "Are you sure you want to delete this expense?", "Confirm Delete");

        if (confirm == JOptionPane.YES_OPTION) {
           
            int expenseId = (int) tablePanel.getValueAt(selectedRow, 0);

         
            boolean removed = expenses.removeIf(e -> e.getId() == expenseId);

            if (removed) {
                messageLabel.setText("Expense ID " + expenseId + " deleted successfully.");
                messageLabel.setForeground(Color.BLUE);
                updateExpenseTable(); 
                updateTotalExpensesLabel(); 
            } else {
                messageLabel.setText("Failed to delete expense ID " + expenseId + " (not found).");
                messageLabel.setForeground(Color.RED);
            }
        }
    }

    
    private void updateExpenseTable() {
        tablePanel.updateTable(expenses); 
    }

   
    private void updateTotalExpensesLabel() {
        double total = expenses.stream()
                               .mapToDouble(Expense::getAmount)
                               .sum();
        summaryPanel.updateTotal(total); 
    }

    private void displaySummaryReport() {
        double total = expenses.stream()
                               .mapToDouble(Expense::getAmount)
                               .sum();

        Set<String> uniqueCategories = expenses.stream()
                                               .map(Expense::getCategory)
                                               .collect(Collectors.toCollection(HashSet::new));

        StringBuilder report = new StringBuilder();
        report.append("--- Expense Summary Report ---\n");
        report.append("Generated On: ").append(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)).append("\n");
        report.append("----------------------------\n\n");

        report.append(String.format("Total Expenses: $%.2f\n\n", total));

        report.append("Expenses by Category:\n");
        if (uniqueCategories.isEmpty()) {
            report.append("  No categorized expenses.\n");
        } else {
           
            for (String cat : uniqueCategories) {
                double categoryTotal = expenses.stream()
                                               .filter(e -> e.getCategory().equals(cat))
                                               .mapToDouble(Expense::getAmount)
                                               .sum();
                report.append(String.format("  - %-15s: $%.2f\n", cat, categoryTotal));
            }
        }

        report.append("\n----------------------------\n");
        report.append("Detailed Expenses (Order of Entry):\n");
        if (expenses.isEmpty()) {
            report.append("  No detailed expenses.\n");
        } else {
            expenses.forEach(expense -> {
                report.append("  ").append(expense.toString()).append("\n");
            });
        }
        report.append("----------------------------\n");

        DialogHelper.showInfo(this, report.toString(), "Expense Summary");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ExpenseTrackerGUI());
    }
}
