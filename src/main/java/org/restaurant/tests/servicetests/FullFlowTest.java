package org.restaurant.tests.servicetests;

import org.restaurant.models.OrderItem;
import org.restaurant.models.User;
import org.restaurant.services.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FullFlowTest {
    public static void main(String[] args) {

        // Initialize services
        UserService userService = new UserService();
        TableService tableService = new TableService();
        BookingService bookingService = new BookingService();
        MenuService menuService = new MenuService();
        OrderService orderService = new OrderService(
                new org.restaurant.dao.implementations.OrderDAOImpl(),
                new org.restaurant.dao.implementations.OrderItemDAOImpl()
        );
        BillService billService = new BillService();
        PaymentService paymentService = new PaymentService();

        System.out.println("\n=== 1️⃣ Register Users ===");
        int customerId = userService.registerUser("Alice Johnson", "Customer", "alice", "pass123");
        int waiterId = userService.registerUser("Bob Williams", "Waiter", "bob", "pass123");

        // In case they already exist from a previous run
        if (customerId == -1) {
            User existingCustomer = userService.getUsersByRole("Customer").get(0);
            customerId = existingCustomer.getUserId();
        }
        if (waiterId == -1) {
            User existingWaiter = userService.getUsersByRole("Waiter").get(0);
            waiterId = existingWaiter.getUserId();
        }

        System.out.println("Customer ID: " + customerId);
        System.out.println("Waiter ID: " + waiterId);

        System.out.println("\n=== 2️⃣ Add Table ===");
        tableService.addTable(4, "Available");
        System.out.println("Table added successfully with capacity 4.");

        System.out.println("\n=== 3️⃣ Customer Books Table ===");
        int bookingId = bookingService.bookTable(1, customerId, LocalDateTime.now());
        System.out.println("Booking ID: " + bookingId);

        System.out.println("\n=== 4️⃣ Admin Adds Menu Item ===");
        menuService.addMenuItem("Margherita Pizza", "Main Course", 250.00);
        menuService.addMenuItem("Caesar Salad", "Starter", 150.00);

        System.out.println("\n=== 5️⃣ Waiter Places Order ===");
        List<OrderItem> items = new ArrayList<>();
        OrderItem item = new OrderItem();
        item.setItemId(1); // first menu item
        item.setQuantity(2);
        items.add(item);

        boolean orderPlaced = orderService.placeOrder(1, waiterId, items);
        System.out.println("Order placed: " + orderPlaced);

        System.out.println("\n=== 6️⃣ Kitchen Prepares Order ===");
        orderService.showPendingOrders();
        orderService.markOrderAsPrepared(1);

        System.out.println("\n=== 7️⃣ Manager Generates Bill ===");
        int billId = billService.createBill(1, "Unpaid"); // ✅ Only orderId + status
        System.out.println("Bill ID: " + billId);

        System.out.println("\n=== 8️⃣ Process Payment ===");
        boolean paymentDone = paymentService.processPayment(billId, "Card"); // ✅ No amount needed
        System.out.println("Payment Done: " + paymentDone);

        System.out.println("\n=== 9️⃣ Cancel Booking ===");
        boolean bookingCancelled = bookingService.cancelBooking(bookingId);
        if (bookingCancelled) {
            System.out.println("Booking cancelled successfully.");
        } else {
            System.out.println("Failed to cancel booking.");
        }

        System.out.println("Daily report:");
        ReportService reportService = new ReportService();
        reportService.generateDailySalesReport(LocalDate.now().toString());


    }
}
