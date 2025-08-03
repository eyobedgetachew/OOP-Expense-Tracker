
import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;


class ExpenseInputPanel extends JPanel {
    private JTextField amountField;
    private JComboBox<String> categoryComboBox;
    private JTextField descriptionField;
    private JButton addExpenseButton;
    private JButton clearFieldsButton;

    public ExpenseInputPanel(String[] categories) {
        super(new GridBagLayout());
        setBorder(BorderFactory.createTitledBorder("Add New Expense"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); 
        gbc.fill = GridBagConstraints.HORIZONTAL;

        
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Amount:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        amountField = new JTextField(15);
        add(amountField, gbc);

        
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Category:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        categoryComboBox = new JComboBox<>(categories);
        add(categoryComboBox, gbc);

        
        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Description:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        descriptionField = new JTextField(20);
        add(descriptionField, gbc);

       
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        addExpenseButton = new JButton("Add Expense");
        clearFieldsButton = new JButton("Clear Fields");
        buttonPanel.add(addExpenseButton);
        buttonPanel.add(clearFieldsButton);

        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2; 
        add(buttonPanel, gbc);
    }

    
    public String getAmountText() {
        return amountField.getText();
    }

    public String getSelectedCategory() {
        return (String) categoryComboBox.getSelectedItem();
    }

    public String getDescriptionText() {
        return descriptionField.getText();
    }

    
    public void addAddExpenseListener(ActionListener listener) {
        addExpenseButton.addActionListener(listener);
    }

    public void addClearFieldsListener(ActionListener listener) {
        clearFieldsButton.addActionListener(listener);
    }

    
    public void clearFields() {
        amountField.setText("");
        categoryComboBox.setSelectedIndex(0); 
        descriptionField.setText("");
    }
}
