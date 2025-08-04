package org.restaurant.dao.implementations;

import org.restaurant.config.DBConnection;
import org.restaurant.dao.interfaces.TableDAO;
import org.restaurant.models.Table;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TableDAOImplementation implements TableDAO {

    @Override
    public boolean addTable(Table table) {
        String sql = "INSERT INTO Tables (capacity, status) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, table.getCapacity());
            stmt.setString(2, table.getStatus());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Table getTableById(int tableId) {
        String sql = "SELECT * FROM Tables WHERE table_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, tableId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extractTableFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Table> getAllTables() {
        List<Table> tables = new ArrayList<>();
        String sql = "SELECT * FROM Tables";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                tables.add(extractTableFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tables;
    }

    @Override
    public List<Table> getTablesByStatus(String status) {
        List<Table> tables = new ArrayList<>();
        String sql = "SELECT * FROM Tables WHERE status = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                tables.add(extractTableFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tables;
    }

    @Override
    public boolean updateTable(Table table) {
        String sql = "UPDATE Tables SET capacity = ?, status = ? WHERE table_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, table.getCapacity());
            stmt.setString(2, table.getStatus());
            stmt.setInt(3, table.getTableId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteTable(int tableId) {
        String sql = "DELETE FROM Tables WHERE table_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, tableId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Table extractTableFromResultSet(ResultSet rs) throws SQLException {
        return new Table(
                rs.getInt("table_id"),
                rs.getInt("capacity"),
                rs.getString("status")
        );
    }
}
