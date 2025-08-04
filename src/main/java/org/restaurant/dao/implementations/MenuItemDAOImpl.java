package org.restaurant.dao.implementations;

import org.restaurant.config.DBConnection;
import org.restaurant.dao.interfaces.MenuItemDAO;
import org.restaurant.models.MenuItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuItemDAOImpl implements MenuItemDAO {

    @Override
    public boolean addMenuItem(MenuItem menuItem) {
        String sql = "INSERT INTO MenuItems (name, category, price) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, menuItem.getName());
            stmt.setString(2, menuItem.getCategory());
            stmt.setDouble(3, menuItem.getPrice());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public MenuItem getMenuItemById(int itemId) {
        String sql = "SELECT * FROM MenuItems WHERE item_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, itemId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extractMenuItemFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<MenuItem> getAllMenuItems() {
        List<MenuItem> menuItems = new ArrayList<>();
        String sql = "SELECT * FROM MenuItems";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                menuItems.add(extractMenuItemFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return menuItems;
    }

    @Override
    public List<MenuItem> getMenuItemsByCategory(String category) {
        List<MenuItem> menuItems = new ArrayList<>();
        String sql = "SELECT * FROM MenuItems WHERE category = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, category);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                menuItems.add(extractMenuItemFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return menuItems;
    }

    @Override
    public boolean updateMenuItem(MenuItem menuItem) {
        String sql = "UPDATE MenuItems SET name = ?, category = ?, price = ? WHERE item_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, menuItem.getName());
            stmt.setString(2, menuItem.getCategory());
            stmt.setDouble(3, menuItem.getPrice());
            stmt.setInt(4, menuItem.getItemId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteMenuItem(int itemId) {
        String sql = "DELETE FROM MenuItems WHERE item_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, itemId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private MenuItem extractMenuItemFromResultSet(ResultSet rs) throws SQLException {
        return new MenuItem(
                rs.getInt("item_id"),
                rs.getString("name"),
                rs.getString("category"),
                rs.getDouble("price")
        );
    }
}
