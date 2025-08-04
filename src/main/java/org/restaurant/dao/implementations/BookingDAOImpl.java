package org.restaurant.dao.implementations;

import org.restaurant.config.DBConnection;
import org.restaurant.dao.interfaces.BookingDAO;
import org.restaurant.models.Booking;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAOImpl implements BookingDAO {

    @Override
    public int addBookingAndReturnId(Booking booking) {
        String sql = "INSERT INTO Bookings (table_id, customer_id, booking_datetime, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, booking.getTableId());
            stmt.setInt(2, booking.getCustomerId());
            stmt.setTimestamp(3, Timestamp.valueOf(booking.getBookingDatetime()));
            stmt.setString(4, booking.getStatus());

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
        return -1; // failure
    }

    @Override
    public Booking getBookingById(int bookingId) {
        String sql = "SELECT * FROM Bookings WHERE booking_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookingId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractBookingFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM Bookings";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                bookings.add(extractBookingFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    @Override
    public List<Booking> getBookingsByCustomerId(int customerId) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM Bookings WHERE customer_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                bookings.add(extractBookingFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    @Override
    public List<Booking> getBookingsByTableId(int tableId) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM Bookings WHERE table_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, tableId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                bookings.add(extractBookingFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    @Override
    public List<Booking> getBookingsByDate(String date) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM Bookings WHERE DATE(booking_datetime) = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(date));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                bookings.add(extractBookingFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    @Override
    public boolean updateBooking(Booking booking) {
        String sql = "UPDATE Bookings SET table_id = ?, customer_id = ?, booking_datetime = ?, status = ? WHERE booking_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, booking.getTableId());
            stmt.setInt(2, booking.getCustomerId());
            stmt.setTimestamp(3, Timestamp.valueOf(booking.getBookingDatetime()));
            stmt.setString(4, booking.getStatus());
            stmt.setInt(5, booking.getBookingId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteBooking(int bookingId) {
        String sql = "DELETE FROM Bookings WHERE booking_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookingId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Booking extractBookingFromResultSet(ResultSet rs) throws SQLException {
        return new Booking(
                rs.getInt("booking_id"),
                rs.getInt("table_id"),
                rs.getInt("customer_id"),
                rs.getTimestamp("booking_datetime").toLocalDateTime(),
                rs.getString("status")
        );
    }
}
