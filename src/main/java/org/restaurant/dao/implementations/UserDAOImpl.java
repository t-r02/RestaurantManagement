package org.restaurant.dao.implementations;

import org.restaurant.config.DBConnection;
import org.restaurant.dao.interfaces.UserDAO;
import org.restaurant.models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {

    @Override
    public int addUserAndReturnId(User user) {
        String sql = "INSERT INTO Users (name, role, username, password) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, user.getFullName());
            stmt.setString(2, user.getRole());
            stmt.setString(3, user.getUsername());
            stmt.setString(4, user.getPassword());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1); // return generated user_id
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Failure
    }

    @Override
    public User getUserById(int userId) {
        String sql = "SELECT * FROM Users WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extractUserFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM Users WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extractUserFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM Users";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                users.add(extractUserFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public List<User> getUsersByRole(String role) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM Users WHERE role = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, role);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                users.add(extractUserFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public boolean updateUser(User user) {
        String sql = "UPDATE Users SET name = ?, role = ?, username = ?, password = ? WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getFullName());
            stmt.setString(2, user.getRole());
            stmt.setString(3, user.getUsername());
            stmt.setString(4, user.getPassword());
            stmt.setInt(5, user.getUserId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteUser(int userId) {
        String sql = "DELETE FROM Users WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("user_id"),
                rs.getString("name"),
                rs.getString("role"),
                rs.getString("username"),
                rs.getString("password")
        );
    }
}
