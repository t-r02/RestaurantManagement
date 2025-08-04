package org.restaurant.dao.interfaces;

import org.restaurant.models.Booking;
import java.util.List;

public interface BookingDAO {
    int addBookingAndReturnId(Booking booking); // ✅ new version
    Booking getBookingById(int bookingId);
    List<Booking> getAllBookings();
    List<Booking> getBookingsByCustomerId(int customerId);
    List<Booking> getBookingsByTableId(int tableId);
    List<Booking> getBookingsByDate(String date);
    boolean updateBooking(Booking booking);
    boolean deleteBooking(int bookingId);
}
