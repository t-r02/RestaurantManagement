package org.restaurant.services;

import org.restaurant.dao.implementations.TableDAOImplementation;
import org.restaurant.dao.interfaces.BillDAO;
import org.restaurant.dao.interfaces.OrderDAO;
import org.restaurant.dao.interfaces.PaymentDAO;
import org.restaurant.dao.implementations.BillDAOImplementation;
import org.restaurant.dao.implementations.OrderDAOImplementation;
import org.restaurant.dao.implementations.PaymentDAOImplementation;
import org.restaurant.models.Bill;
import org.restaurant.models.Booking;
import org.restaurant.models.Order;
import org.restaurant.models.Payment;

import java.time.LocalDateTime;
import java.util.List;

public class PaymentService {

    private final PaymentDAO paymentDAO;
    private final BillDAO billDAO;
    private final OrderDAO orderDAO;

    public PaymentService() {
        this.paymentDAO = new PaymentDAOImplementation();
        this.billDAO = new BillDAOImplementation();
        this.orderDAO = new OrderDAOImplementation();
    }

    public boolean processPayment(int billId, String method) {
        Bill bill = billDAO.getBillById(billId);
        if (bill == null) {
            System.out.println("Bill not found!");
            return false;
        }

        if ("Paid".equalsIgnoreCase(bill.getPaymentStatus())) {
            System.out.println("Bill is already paid!");
            return false;
        }

        double amount = bill.getTotalAmount(); // ✅ Automatically get amount from bill

        // Create payment record
        Payment payment = new Payment();
        payment.setBillId(billId);
        payment.setAmountPaid(amount);
        payment.setPaymentMethod(method);
        payment.setPaymentDatetime(LocalDateTime.now());

        boolean paymentSuccess = paymentDAO.addPayment(payment);

        if (paymentSuccess) {
            // Mark bill as paid
            bill.setPaymentStatus("Paid");
            billDAO.updateBill(bill);

            // Mark order as served
            Order order = orderDAO.getOrderById(bill.getOrderId());
            if (order != null) {
                order.setStatus("Served");
                orderDAO.updateOrder(order);

                org.restaurant.dao.interfaces.TableDAO tableDAO = new TableDAOImplementation();
                org.restaurant.models.Table table = tableDAO.getTableById(order.getTableId());
                if (table != null) {
                    table.setStatus("Available");
                    tableDAO.updateTable(table);
                }

                // Update booking status to "Completed"
                BookingService bookingService = new BookingService();
                List<Booking> bookings = bookingService.getBookingsByTableId(order.getTableId()).stream()
                        .filter(b -> "Reserved".equalsIgnoreCase(b.getStatus()))
                        .toList();

                if (!bookings.isEmpty()) {
                    Booking booking = bookings.get(0);
                    booking.setStatus("Completed");
                    bookingService.updateBooking(booking);
                }
            }

            System.out.println("Payment processed successfully. Bill marked as Paid, Order marked as Served, Table now Available, Booking marked as Completed.");
            return true;
        }
        else {
            System.out.println("Failed to process payment.");
            return false;
        }
    }
}
