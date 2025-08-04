import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A Data Access Object (DAO) for the Expense Tracker application.
 * This class now works with the new CategorizedExpense class.
 */
class ExpenseTrackerDAO {

    // Database connection details
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/OOP";
    private static final String USER = "postgres";
    private static final String PASSWORD = "07510751";

    /**
     * Establishes a connection to the PostgreSQL database.
     * @return A valid Connection object.
     * @throws SQLException if a database access error occurs.
     */
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASSWORD);
    }

    /**
     * Adds a new expense to the database.
     * @param expense The CategorizedExpense object to be added.
     * @return The newly created CategorizedExpense object with its database-generated ID.
     * @throws SQLException if a database access error occurs.
     */
    public CategorizedExpense addExpense(CategorizedExpense expense) throws SQLException {
        String sql = "INSERT INTO expenses (amount, category, description, date) VALUES (?, ?, ?, ?) RETURNING id";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, expense.getAmount());
            pstmt.setString(2, expense.getCategory());
            pstmt.setString(3, expense.getDescription());
            pstmt.setDate(4, Date.valueOf(expense.getDate()));

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    expense.setId(rs.getInt("id"));
                }
            }
        }
        return expense;
    }

    /**
     * Retrieves all expenses from the database.
     * @return A list of all CategorizedExpense objects.
     * @throws SQLException if a database access error occurs.
     */
    public List<CategorizedExpense> getExpenses() throws SQLException {
        List<CategorizedExpense> expenses = new ArrayList<>();
        String sql = "SELECT id, amount, category, description, date FROM expenses ORDER BY id DESC";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                expenses.add(new CategorizedExpense(
                        rs.getInt("id"),
                        rs.getDouble("amount"),
                        rs.getString("category"),
                        rs.getString("description"),
                        rs.getDate("date").toLocalDate()
                ));
            }
        }
        return expenses;
    }

    /**
     * Deletes an expense from the database by its ID.
     * @param id The ID of the expense to delete.
     * @return true if the expense was deleted, false otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public boolean deleteExpense(int id) throws SQLException {
        String sql = "DELETE FROM expenses WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
}
