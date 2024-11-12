package backend;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;

public class FacialRecognition {

    // Database URL
    private static final String URL = " ";

    // connect to the database
    public Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    // create the users table if it doesn't already exist
    public void createTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS users (" +
                                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                "name TEXT NOTNULL, " +
                                "email TEXT UNIQUE NOT NULL)";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
            System.out.println("Table created or already exists.");
        } catch (SQLException e) {
            System.out.println("Error creating table: " + e.getMessage());
        }
    }

    // insert data into the users table
    public void insertData(String name, String email) {
        String insertSQL = "INSERT INTO users (name, email) VALUES ('" + name + "', '" + email + "')";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(insertSQL);
            System.out.println("Data inserted: " + name + ", " + email);
        } catch (SQLException e) {
            System.out.println("Error inserting data: " + e.getMessage());
        }
    }
}