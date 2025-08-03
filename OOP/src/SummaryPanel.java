
import java.awt.*;
import javax.swing.*;


class SummaryPanel extends JPanel {
    private JLabel totalExpensesLabel;

    public SummaryPanel() {
        super(new FlowLayout(FlowLayout.RIGHT));
        totalExpensesLabel = new JLabel("Total Expenses: $0.00");
        totalExpensesLabel.setFont(new Font("SansSerif", Font.BOLD, 16)); 
        add(totalExpensesLabel);
    }

    /**
     * Updates the total expenses label with a new amount.
     * @param totalAmount The new total expense amount to display.
     */
    public void updateTotal(double totalAmount) {
        totalExpensesLabel.setText(String.format("Total Expenses: $%.2f", totalAmount));
    }
}
