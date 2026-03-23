import java.sql.*;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {

        // Use environment variables instead of hardcoding secrets
        String dbPassword = System.getenv("DB_PASSWORD");

        if (dbPassword == null) {
            System.out.println("Database password not set in environment variables.");
            return;
        }

        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "root";

        // try-with-resources to prevent resource leaks
        try (Scanner scanner = new Scanner(System.in);
             Connection conn = DriverManager.getConnection(url, user, dbPassword)) {

            System.out.print("Enter username: ");
            String userInput = scanner.nextLine();

            // Use PreparedStatement to prevent SQL Injection
            String query = "SELECT * FROM users WHERE username = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {

                pstmt.setString(1, userInput);

                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        System.out.println("User found: " + rs.getString("username"));
                    }
                }
            }

        } catch (SQLException e) {
            // Avoid exposing sensitive info
            System.err.println("Database error occurred.");
        }

        // ❌ Removed command execution (Command Injection risk)
        // If needed, validate input strictly before using in OS commands

    }
}
