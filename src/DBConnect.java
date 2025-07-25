import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect {

    private static final String JDBC_URL = "jdbc:oracle:thin:@localhost:1521/XEPDB1";
    private static final String USER = "Ayesha";
    private static final String PASSWORD = "Ayesha123";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
    }
    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            System.out.println("✅ Connected successfully to Oracle!");

            var stmt = conn.createStatement();
            var rs = stmt.executeQuery("SELECT COUNT(*) FROM balls");
            if (rs.next()) {
                System.out.println("✅ 'balls' table has " + rs.getInt(1) + " rows.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Connection or query failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
