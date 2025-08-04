package org.restaurant.dao.implementations;

import org.restaurant.config.DBConnection;
import org.restaurant.dao.interfaces.OrderDAO;
import org.restaurant.models.Order;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAOImpl implements OrderDAO {

    @Override
    public int addOrderAndReturnId(Order order) {
        String sql = "INSERT INTO Orders (table_id, waiter_id, status) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, order.getTableId());
            stmt.setInt(2, order.getWaiterId());
            stmt.setString(3, order.getStatus());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Failure
    }


    @Override
    public Order getOrderById(int orderId) {
        String sql = "SELECT * FROM Orders WHERE order_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extractOrderFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM Orders";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                orders.add(extractOrderFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    @Override
    public List<Order> getOrdersByTableId(int tableId) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM Orders WHERE table_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, tableId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                orders.add(extractOrderFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    @Override
    public List<Order> getOrdersByWaiterId(int waiterId) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM Orders WHERE waiter_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, waiterId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                orders.add(extractOrderFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    @Override
    public List<Order> getOrdersByStatus(String status) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM Orders WHERE status = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                orders.add(extractOrderFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    @Override
    public boolean updateOrder(Order order) {
        String sql = "UPDATE Orders SET table_id = ?, waiter_id = ?, status = ? WHERE order_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, order.getTableId());
            stmt.setInt(2, order.getWaiterId());
            stmt.setString(3, order.getStatus());
            stmt.setInt(4, order.getOrderId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteOrder(int orderId) {
        String sql = "DELETE FROM Orders WHERE order_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Order extractOrderFromResultSet(ResultSet rs) throws SQLException {
        return new Order(
                rs.getInt("order_id"),
                rs.getInt("table_id"),
                rs.getInt("waiter_id"),
                rs.getString("status")
        );
    }
}
