package org.restaurant.models;

import java.time.LocalDateTime;

public class Booking {
    private int bookingId;
    private int tableId;
    private int customerId;
    private LocalDateTime bookingDatetime;
    private String status; // Reserved, Cancelled, Completed

    public Booking() {}

    public Booking(int bookingId, int tableId, int customerId, LocalDateTime bookingDatetime, String status) {
        this.bookingId = bookingId;
        this.tableId = tableId;
        this.customerId = customerId;
        this.bookingDatetime = bookingDatetime;
        this.status = status;
    }

    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }

    public int getTableId() { return tableId; }
    public void setTableId(int tableId) { this.tableId = tableId; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public LocalDateTime getBookingDatetime() { return bookingDatetime; }
    public void setBookingDatetime(LocalDateTime bookingDatetime) { this.bookingDatetime = bookingDatetime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "Booking{" +
                "bookingId=" + bookingId +
                ", tableId=" + tableId +
                ", customerId=" + customerId +
                ", bookingDatetime=" + bookingDatetime +
                ", status='" + status + '\'' +
                '}';
    }
}
