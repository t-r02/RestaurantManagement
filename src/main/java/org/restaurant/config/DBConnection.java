package org.restaurant.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // Change these to match your PostgreSQL setup
    private static final String URL = "jdbc:postgresql://localhost:5432/restaurant_db";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "root";

    static {
        try {
            // Load PostgreSQL JDBC Driver
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("PostgreSQL JDBC Driver not found!");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    // Optional: to test connection quickly
    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            if (conn != null) {
                conn.setAutoCommit(true);
                System.out.println("Connected to PostgreSQL successfully!");
            } else {
                System.out.println("Connection failed!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
