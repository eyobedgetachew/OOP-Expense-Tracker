import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.swing.*;

/**
 * Main class for the Expense Tracker application.
 * This class now includes functionality to save the summary report to a file.
 */
public class ExpenseTrackerGUI extends JFrame {

    // Database access object
    private ExpenseTrackerDAO expenseDao;
    private List<CategorizedExpense> expenses;

    // GUI Components
    private ExpenseInputPanel inputPanel;
    private ExpenseTablePanel tablePanel;
    private SummaryPanel summaryPanel;
    private JLabel messageLabel;

    /**
     * Constructor for the ExpenseTrackerGUI.
     * Initializes the GUI and connects to the database.
     */
    public ExpenseTrackerGUI() {
        super("Expense Tracker");

        // Initialize DAO and load initial data
        try {
            // Ensure the JDBC driver is available
            Class.forName("org.postgresql.Driver");
            expenseDao = new ExpenseTrackerDAO();
            loadExpensesFromDatabase();
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this,
                    "PostgreSQL JDBC Driver not found. Please add the JAR to your classpath.",
                    "Driver Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Database connection error: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        // Initialize GUI components
        CategoryManager categoryManager = new CategoryManager();

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
        JButton globalSaveSummaryButton = new JButton("Save Summary");

        globalButtonsPanel.add(globalShowSummaryButton);
        globalButtonsPanel.add(globalDeleteSelectedButton);
        globalButtonsPanel.add(globalSaveSummaryButton);
        
        bottomSectionPanel.add(globalButtonsPanel, BorderLayout.SOUTH);

        add(topSectionPanel, BorderLayout.NORTH);
        add(middleSectionPanel, BorderLayout.CENTER);
        add(bottomSectionPanel, BorderLayout.SOUTH);

        // Add action listeners
        inputPanel.addAddExpenseListener(e -> addExpenseAction());
        inputPanel.addClearFieldsListener(e -> inputPanel.clearFields());
        globalShowSummaryButton.addActionListener(e -> displaySummaryReport());
        globalDeleteSelectedButton.addActionListener(e -> deleteSelectedExpense());
        globalSaveSummaryButton.addActionListener(e -> saveSummaryToFile());

        // Initial UI update
        updateExpenseTable();
        updateTotalExpensesLabel();

        setVisible(true);
    }

    /**
     * Loads all expenses from the database and updates the local list.
     */
    private void loadExpensesFromDatabase() throws SQLException {
        expenses = expenseDao.getExpenses();
    }

    /**
     * Handles the "Add Expense" button click.
     * Validates input, adds the expense to the database, and refreshes the UI.
     */
    private void addExpenseAction() {
        try {
            double amount = Double.parseDouble(inputPanel.getAmountText());
            String categoryName = inputPanel.getSelectedCategory();
            String description = inputPanel.getDescriptionText();

            // Create a new CategorizedExpense object
            CategorizedExpense newExpense = new CategorizedExpense(amount, categoryName, description, LocalDate.now());

            // Add the expense to the database and get the ID back
            expenseDao.addExpense(newExpense);

            messageLabel.setText("Expense added successfully!");
            messageLabel.setForeground(Color.BLUE);
            inputPanel.clearFields();
            loadExpensesFromDatabase();
            updateExpenseTable();
            updateTotalExpensesLabel();

        } catch (NumberFormatException ex) {
            messageLabel.setText("Error: Invalid amount. Please enter a number.");
            messageLabel.setForeground(Color.RED);
            DialogHelper.showError(this, "Please enter a valid number for amount.", "Input Error");
        } catch (SQLException ex) {
            messageLabel.setText("Error: Failed to save to database. " + ex.getMessage());
            messageLabel.setForeground(Color.RED);
            DialogHelper.showError(this, "Failed to save expense: " + ex.getMessage(), "Database Error");
        }
    }

    /**
     * Handles the "Delete Selected Expense" button click.
     * Deletes the selected expense from the database and refreshes the UI.
     */
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
            try {
                int expenseId = (int) tablePanel.getValueAt(selectedRow, 0);
                boolean deleted = expenseDao.deleteExpense(expenseId);

                if (deleted) {
                    messageLabel.setText("Expense ID " + expenseId + " deleted successfully.");
                    messageLabel.setForeground(Color.BLUE);
                    loadExpensesFromDatabase();
                    updateExpenseTable();
                    updateTotalExpensesLabel();
                } else {
                    messageLabel.setText("Failed to delete expense ID " + expenseId + " (not found).");
                    messageLabel.setForeground(Color.RED);
                }
            } catch (SQLException e) {
                messageLabel.setText("Error: Failed to delete from database. " + e.getMessage());
                messageLabel.setForeground(Color.RED);
                DialogHelper.showError(this, "Failed to delete expense: " + e.getMessage(), "Database Error");
            }
        }
    }

    /**
     * Updates the table with the current list of expenses.
     */
    private void updateExpenseTable() {
        tablePanel.updateTable(expenses);
    }

    /**
     * Updates the total expenses label in the summary panel.
     */
    private void updateTotalExpensesLabel() {
        double total = expenses.stream()
                .mapToDouble(CategorizedExpense::getAmount)
                .sum();
        summaryPanel.updateTotal(total);
    }

    /**
     * Generates the summary report as a string.
     * This method is now used by both displaySummaryReport() and saveSummaryToFile().
     * @return A string containing the formatted summary report.
     */
    private String generateSummaryReport() {
        double total = expenses.stream()
                .mapToDouble(CategorizedExpense::getAmount)
                .sum();

        Set<String> uniqueCategories = expenses.stream()
                .map(CategorizedExpense::getCategory)
                .collect(Collectors.toCollection(HashSet::new));

        StringBuilder report = new StringBuilder();
        report.append("--- Expense Summary Report ---\n");
        report.append("Generated On: ").append(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)).append("\n");
        report.append("----------------------------\n\n");

        report.append(String.format("Total Expenses: $%.2f\n\n", total));

        report.append("Expenses by Category:\n");
        if (uniqueCategories.isEmpty()) {
            report.append("   No categorized expenses.\n");
        } else {
            for (String cat : uniqueCategories) {
                double categoryTotal = expenses.stream()
                        .filter(e -> e.getCategory().equals(cat))
                        .mapToDouble(CategorizedExpense::getAmount)
                        .sum();
                report.append(String.format("   - %-15s: $%.2f\n", cat, categoryTotal));
            }
        }

        report.append("\n----------------------------\n");
        report.append("Detailed Expenses (Using new interfaces):\n");
        if (expenses.isEmpty()) {
            report.append("   No detailed expenses.\n");
        } else {
            expenses.forEach(expense -> {
                // Using the getSummary() method from the Summarizable interface
                report.append("   - ").append(expense.getSummary()).append("\n");

                // Using the printDetails() method from the Printable interface (prints to console)
                expense.printDetails();
            });
        }
        report.append("----------------------------\n");
        
        return report.toString();
    }

    /**
     * Displays a summary report of all expenses in a dialog box.
     */
    private void displaySummaryReport() {
        String report = generateSummaryReport();
        DialogHelper.showInfo(this, report, "Expense Summary");
    }

    /**
     * Saves the summary report to a file selected by the user.
     * Uses a BufferedWriter for efficient writing.
     */
    private void saveSummaryToFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Expense Summary Report");
        fileChooser.setSelectedFile(new File("ExpenseSummaryReport.txt"));

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {
                writer.write(generateSummaryReport());
                messageLabel.setText("Summary saved to " + fileToSave.getAbsolutePath());
                messageLabel.setForeground(Color.BLUE);
            } catch (IOException ex) {
                messageLabel.setText("Error saving file: " + ex.getMessage());
                messageLabel.setForeground(Color.RED);
                DialogHelper.showError(this, "Failed to save file: " + ex.getMessage(), "File Save Error");
            }
        }
    }

    /**
     * Main entry point for the application.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(ExpenseTrackerGUI::new);
    }
}
