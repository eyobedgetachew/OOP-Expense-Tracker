import java.sql.*;
import java.util.ArrayList;
import java.util.List;


class ExpenseTrackerDAO {

    // Database connection details
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/OOP";
    private static final String USER = "postgres";
    private static final String PASSWORD = "07510751";

   
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASSWORD);
    }

   
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
