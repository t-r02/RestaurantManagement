package org.restaurant.dao.implementations;

import org.restaurant.config.DBConnection;
import org.restaurant.dao.interfaces.BillDAO;
import org.restaurant.models.Bill;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BillDAOImpl implements BillDAO {

    @Override
    public int addBillAndReturnId(Bill bill) {
        String sql = "INSERT INTO Bills (order_id, total_amount, status) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, bill.getOrderId());
            stmt.setDouble(2, bill.getTotalAmount());
            stmt.setString(3, bill.getPaymentStatus());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1); // Return generated bill_id
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Failure
    }

    @Override
    public Bill getBillById(int billId) {
        String sql = "SELECT * FROM Bills WHERE bill_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, billId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extractBillFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Bill> getAllBills() {
        List<Bill> bills = new ArrayList<>();
        String sql = "SELECT * FROM Bills";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                bills.add(extractBillFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bills;
    }

    @Override
    public List<Bill> getBillsByOrderId(int orderId) {
        List<Bill> bills = new ArrayList<>();
        String sql = "SELECT * FROM Bills WHERE order_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                bills.add(extractBillFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bills;
    }

    @Override
    public boolean updateBill(Bill bill) {
        String sql = "UPDATE Bills SET order_id = ?, total_amount = ?, status = ? WHERE bill_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, bill.getOrderId());
            stmt.setDouble(2, bill.getTotalAmount());
            stmt.setString(3, bill.getPaymentStatus());
            stmt.setInt(4, bill.getBillId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    private Bill extractBillFromResultSet(ResultSet rs) throws SQLException {
        return new Bill(
                rs.getInt("bill_id"),
                rs.getInt("order_id"),
                rs.getDouble("total_amount"),
                rs.getString("status")
        );
    }
}
