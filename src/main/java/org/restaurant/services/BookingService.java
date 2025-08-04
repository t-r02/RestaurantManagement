package org.restaurant.services;

import org.restaurant.dao.interfaces.BookingDAO;
import org.restaurant.dao.interfaces.TableDAO;
import org.restaurant.dao.implementations.BookingDAOImplementation;
import org.restaurant.dao.implementations.TableDAOImplementation;
import org.restaurant.models.Booking;
import org.restaurant.models.Table;

import java.time.LocalDateTime;
import java.util.List;

public class BookingService {

    private final BookingDAO bookingDAO;
    private final TableDAO tableDAO;

    public BookingService() {
        this.bookingDAO = new BookingDAOImplementation();
        this.tableDAO = new TableDAOImplementation();
    }

    public int bookTable(int tableId, int customerId, LocalDateTime bookingDateTime) {
        Table table = tableDAO.getTableById(tableId);
        if (table == null || !"Available".equalsIgnoreCase(table.getStatus())) {
            return -1; // table not available
        }

        Booking booking = new Booking();
        booking.setTableId(tableId);
        booking.setCustomerId(customerId);
        booking.setBookingDatetime(bookingDateTime);
        booking.setStatus("Reserved");

        int bookingId = bookingDAO.addBookingAndReturnId(booking);
        if (bookingId > 0) {
            table.setStatus("Reserved");
            tableDAO.updateTable(table);
        }
        return bookingId;
    }

    public boolean cancelBooking(int bookingId) {
        Booking booking = bookingDAO.getBookingById(bookingId);
        if (booking == null) return false;

        Table table = tableDAO.getTableById(booking.getTableId());
        if (table != null) {
            table.setStatus("Available");
            tableDAO.updateTable(table);
        }

        return bookingDAO.deleteBooking(bookingId);
    }

    public Booking getBookingById(int bookingId) {
        return bookingDAO.getBookingById(bookingId);
    }

    public List<Booking> getBookingsByCustomer(int customerId) {
        return bookingDAO.getBookingsByCustomerId(customerId);
    }

    public List<Booking> getAllBookings() {
        return bookingDAO.getAllBookings();
    }
}
