
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
                return false; 
            }
        };
        expenseTable = new JTable(tableModel);
        expenseTable.setFillsViewportHeight(true); 
        JScrollPane scrollPane = new JScrollPane(expenseTable);
        add(scrollPane, BorderLayout.CENTER);
    }

   
    public void updateTable(List<Expense> expenses) {
        tableModel.setRowCount(0); 
        for (Expense expense : expenses) {
            tableModel.addRow(new Object[]{
                expense.getId(),
                expense.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                String.format("%.2f", expense.getAmount()), 
                expense.getCategory(),
                expense.getDescription()
            });
        }
    }

   
    public int getSelectedRow() {
        return expenseTable.getSelectedRow();
    }

  
    public Object getValueAt(int row, int column) {
        return tableModel.getValueAt(row, column);
    }
}
