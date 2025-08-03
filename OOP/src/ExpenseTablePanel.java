
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;


class ExpenseTablePanel extends JPanel {
    private DefaultTableModel tableModel;
    private JTable expenseTable;

    public ExpenseTablePanel() {
        super(new BorderLayout());

        String[] columnNames = {"ID", "Date", "Amount", "Category", "Description"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make cells non-editable
            }
        };
        expenseTable = new JTable(tableModel);
        expenseTable.setFillsViewportHeight(true); // Table takes full height
        JScrollPane scrollPane = new JScrollPane(expenseTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Updates the table with a new list of expenses.
     * Expenses are displayed in the order provided (no internal sorting).
     * @param expenses The list of Expense objects to display.
     */
    public void updateTable(List<Expense> expenses) {
        tableModel.setRowCount(0); // Clear existing rows
        for (Expense expense : expenses) {
            tableModel.addRow(new Object[]{
                expense.getId(),
                expense.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                String.format("%.2f", expense.getAmount()), // Format amount
                expense.getCategory(),
                expense.getDescription()
            });
        }
    }

    /**
     * Returns the currently selected row index in the table.
     * @return The index of the selected row, or -1 if no row is selected.
     */
    public int getSelectedRow() {
        return expenseTable.getSelectedRow();
    }

    /**
     * Returns the value from a specific cell in the table model.
     * @param row The row index.
     * @param column The column index.
     * @return The value at the specified cell.
     */
    public Object getValueAt(int row, int column) {
        return tableModel.getValueAt(row, column);
    }
}
