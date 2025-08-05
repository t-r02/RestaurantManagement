package org.restaurant;

import org.restaurant.dao.implementations.BillDAOImplementation;
import org.restaurant.dao.implementations.OrderDAOImplementation;
import org.restaurant.dao.implementations.OrderItemDAOImplementation;
import org.restaurant.models.*;
import org.restaurant.services.*;

import java.time.LocalDateTime;
import java.util.*;

public class Main {

    private static final Scanner sc = new Scanner(System.in);

    // Services
    private static final UserService userService = new UserService();
    private static final TableService tableService = new TableService();
    private static final BookingService bookingService = new BookingService();
    private static final MenuService menuService = new MenuService();
    private static final OrderService orderService = new OrderService(
            new OrderDAOImplementation(),
            new OrderItemDAOImplementation()
    );
    private static final BillService billService = new BillService();
    private static final PaymentService paymentService = new PaymentService();
    private static final ReportService reportService = new ReportService();

    public static void main(String[] args) {
        initializeDefaults();
        while (true) {
            System.out.println("\n=== Restaurant Management System ===");
            System.out.println("1. Login");
            System.out.println("2. Register as Customer");
            System.out.println("0. Exit");
            System.out.print("Choose option: ");
            int choice = getInt();
            if (choice == 1) {
                login();
            } else if (choice == 2) {
                registerCustomer();
            } else if (choice == 0) {
                System.out.println("Goodbye!");
                break;
            }
        }
    }

    private static void initializeDefaults() {
        // Ensure only 1 admin & 1 manager
        if (userService.getUsersByRole("Admin").isEmpty()) {
            userService.registerUser("System Admin", "Admin", "admin", "admin123");
        }
        if (userService.getUsersByRole("Manager").isEmpty()) {
            userService.registerUser("Branch Manager", "Manager", "manager", "man123");
        }
        // Kitchen Staff
        if (userService.getUsersByRole("Kitchen").size() < 2) {
            userService.registerUser("Kitchen Staff 1", "Kitchen", "kitchen1", "kit123");
            userService.registerUser("Kitchen Staff 2", "Kitchen", "kitchen2", "kit234");
        }
        // Waiters
        if (userService.getUsersByRole("Waiter").size() < 2) {
            userService.registerUser("Waiter One", "Waiter", "waiter1", "wait123");
            userService.registerUser("Waiter Two", "Waiter", "waiter2", "wait234");
        }
        // Tables
        if (tableService.getAllTables().isEmpty()) {
            tableService.addTable(2, "Available");
            tableService.addTable(2, "Available");
            tableService.addTable(4, "Available");
            tableService.addTable(4, "Available");
        }
        // Menu Items
        if (menuService.getAllMenuItems().isEmpty()) {
            menuService.addMenuItem("Margherita Pizza", "Main Course", 250.00);
            menuService.addMenuItem("Caesar Salad", "Starter", 150.00);
            menuService.addMenuItem("Pasta Alfredo", "Main Course", 300.00);
        }
    }

    private static void login() {
        System.out.print("Username: ");
        String uname = sc.nextLine();
        System.out.print("Password: ");
        String pass = sc.nextLine();

        User u = userService.loginUser(uname, pass);
        if (u == null) return;

        switch (u.getRole()) {
            case "Customer" -> customerMenu(u);
            case "Waiter" -> waiterMenu(u);
            case "Kitchen" -> kitchenMenu();
            case "Manager" -> managerMenu();
            case "Admin" -> adminMenu();
            default -> System.out.println("Role not recognized.");
        }
    }

    private static void registerCustomer() {
        System.out.print("Full Name: ");
        String name = sc.nextLine();
        System.out.print("Username: ");
        String uname = sc.nextLine();
        System.out.print("Password: ");
        String pass = sc.nextLine();

        int id = userService.registerUser(name, "Customer", uname, pass);
        if (id > 0) {
            System.out.println("✅ Registered as Customer. ID: " + id);
        }
    }

    // === Menus per role ===
    private static void customerMenu(User customer) {
        while (true) {
            System.out.println("\n=== Customer Menu ===");
            System.out.println("1. Book Table");
            System.out.println("2. View My Bookings");
            System.out.println("3. Cancel Booking");
            System.out.println("0. Logout");
            System.out.print("Choose: ");
            int ch = getInt();

            if (ch == 1) {
                System.out.println("Available Tables:");
                tableService.getAllTables().stream()
                        .filter(t -> t.getStatus().equalsIgnoreCase("Available"))
                        .forEach(System.out::println);

                System.out.print("Table ID: ");
                int tid = getInt();
                int bid = bookingService.bookTable(tid, customer.getUserId(), LocalDateTime.now());
                if (bid > 0) {
                    System.out.println("✅ Booking confirmed. ID: " + bid);
                } else {
                    System.out.println("❌ Booking failed. Table may not be available.");
                }
            }
            else if (ch == 2) {
                var bookings = bookingService.getBookingsByCustomer(customer.getUserId());
                if (bookings.isEmpty()) {
                    System.out.println("No bookings found.");
                } else {
                    System.out.println("\n📅 Your Bookings:");
                    bookings.forEach(System.out::println);
                }
            }
            else if (ch == 3) {
                var bookings = bookingService.getBookingsByCustomer(customer.getUserId());
                if (bookings.isEmpty()) {
                    System.out.println("❌ You have no bookings to cancel.");
                    continue;
                }

                System.out.println("\n📅 Your Bookings:");
                bookings.forEach(System.out::println);

                System.out.print("Enter Booking ID to cancel: ");
                int bookingId = getInt();

                boolean ownsBooking = bookings.stream().anyMatch(b -> b.getBookingId() == bookingId);
                if (!ownsBooking) {
                    System.out.println("❌ Invalid Booking ID. You can only cancel your own bookings.");
                    continue;
                }

                if (bookingService.cancelBooking(bookingId)) {
                    System.out.println("✅ Booking cancelled successfully.");
                } else {
                    System.out.println("❌ Failed to cancel booking.");
                }
            }
            else if (ch == 0) break;
        }
    }


    private static void waiterMenu(User waiter) {
        while (true) {
            System.out.println("\n=== Waiter Menu ===");
            System.out.println("1. Place Order");
            System.out.println("0. Logout");
            int ch = getInt();
            if (ch == 1) {
                System.out.print("Table ID: ");
                int tid = getInt();
                // Check if table has at least one booking with status "Reserved"
                var reservedBookings = bookingService.getBookingsByTableId(tid).stream()
                        .filter(b -> "Reserved".equalsIgnoreCase(b.getStatus()))
                        .toList();
                if (reservedBookings.isEmpty()) {
                    System.out.println("Cannot place order. The table is not currently reserved.");
                    continue; // Skip order placement
                }

                List<MenuItem> menu = menuService.getAllMenuItems();
                List<OrderItem> items = new ArrayList<>();

                while (true) {
                    menu.forEach(System.out::println);
                    System.out.print("Menu Item ID (0 to finish): ");
                    int mid = getInt();
                    if (mid == 0) break;
                    System.out.print("Quantity: ");
                    int qty = getInt();
                    OrderItem oi = new OrderItem();
                    oi.setItemId(mid);
                    oi.setQuantity(qty);
                    items.add(oi);
                }
                if (orderService.placeOrder(tid, waiter.getUserId(), items))
                    System.out.println("✅ Order placed.");
            } else if (ch == 0) break;
        }
    }

    private static void kitchenMenu() {
        while (true) {
            System.out.println("\n=== Kitchen Menu ===");
            orderService.showPendingOrders();
            System.out.println("1. Mark Order as Prepared");
            System.out.println("0. Logout");
            int ch = getInt();
            if (ch == 1) {
                System.out.print("Order ID: ");
                int oid = getInt();
                orderService.markOrderAsPrepared(oid);
            } else if (ch == 0) break;
        }
    }

    private static void managerMenu() {
        while (true) {
            System.out.println("\n=== Manager Menu ===");
            System.out.println("1. Generate Bill");
            System.out.println("2. Process Payment");
            System.out.println("0. Logout");
            int ch = getInt();

            switch (ch) {
                case 0 -> {
                    return; // Exit menu
                }
                case 1 -> {
                    // Show orders without bills
                    var allOrders = new OrderDAOImplementation().getAllOrders();
                    var allBills = new BillDAOImplementation().getAllBills();

                    var billedOrderIds = allBills.stream()
                            .map(Bill::getOrderId)
                            .toList();

                    var ordersWithoutBills = allOrders.stream()
                            .filter(o -> !billedOrderIds.contains(o.getOrderId()))
                            .toList();

                    if (ordersWithoutBills.isEmpty()) {
                        System.out.println("✅ All orders already have bills.");
                        break;
                    }

                    System.out.println("\n📋 Orders without a bill:");
                    ordersWithoutBills.forEach(o -> {
                        User waiter = userService.getUserById(o.getWaiterId());
                        Table table = tableService.getTableById(o.getTableId());
                        System.out.printf("Order ID: %d | Table: %d | Waiter: %s | Status: %s%n",
                                o.getOrderId(), o.getTableId(),
                                waiter != null ? waiter.getFullName() : "Unknown",
                                o.getStatus());
                    });

                    System.out.print("\nEnter Order ID to generate bill: ");
                    int oid = getInt();
                    int billId = billService.createBill(oid, "Unpaid");
                    if (billId > 0) showBillDetails(billId);
                }
                case 2 -> {
                    // Show unpaid bills
                    var unpaidBills = new BillDAOImplementation().getAllBills().stream()
                            .filter(b -> !"Paid".equalsIgnoreCase(b.getPaymentStatus()))
                            .toList();

                    if (unpaidBills.isEmpty()) {
                        System.out.println("✅ No unpaid bills found.");
                        break;
                    }

                    System.out.println("\n💰 Unpaid Bills:");
                    unpaidBills.forEach(b -> {
                        Order order = new OrderDAOImplementation().getOrderById(b.getOrderId());
                        User customer = null;
                        if (order != null) {
                            var bookings = bookingService.getAllBookings().stream()
                                    .filter(book -> book.getTableId() == order.getTableId())
                                    .toList();
                            if (!bookings.isEmpty()) {
                                customer = userService.getUserById(bookings.get(0).getCustomerId());
                            }
                        }
                        System.out.printf("Bill ID: %d | Order ID: %d | Amount: ₹%.2f | Status: %s | Customer: %s%n",
                                b.getBillId(), b.getOrderId(),
                                b.getTotalAmount(), b.getPaymentStatus(),
                                customer != null ? customer.getFullName() : "Unknown");
                    });

                    System.out.print("\nEnter Bill ID to process payment: ");
                    int bid = getInt();
                    System.out.print("Payment Method: ");
                    String method = sc.nextLine();
                    if (paymentService.processPayment(bid, method)) {
                        showBillDetails(bid);
                    }
                }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }


    private static void adminMenu() {
        while (true) {
            System.out.println("\n=== Admin Menu ===");
            System.out.println("1. Daily Sales Report");
            System.out.println("2. View All Reports");
            System.out.println("0. Logout");
            int ch = getInt();
            switch(ch) {
                case 1 -> {
                    System.out.print("Enter Date (YYYY-MM-DD): ");
                    String date = sc.nextLine();
                    reportService.generateDailySalesReport(date);
                }
                case 2 -> {
                    reportService.generateAllReports();
                }
                case 0 -> { return; }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    // Display bill with details
    private static void showBillDetails(int billId) {
        Bill bill = billService.getBillById(billId);
        if (bill == null) {
            System.out.println("Bill not found!");
            return;
        }

        // Get customer who paid
        Order order = new OrderDAOImplementation().getOrderById(bill.getOrderId());
        String customerName = "Unknown";
        if (order != null) {
            Booking booking = bookingService.getAllBookings().stream()
                    .filter(b -> b.getTableId() == order.getTableId())
                    .findFirst()
                    .orElse(null);
            if (booking != null) {
                User customer = userService.getUserById(booking.getCustomerId());
                if (customer != null) customerName = customer.getFullName();
            }
        }

        System.out.println("\n=== Bill Details ===");
        System.out.printf("Bill ID: %d | Order ID: %d | Status: %s%n",
                bill.getBillId(), bill.getOrderId(), bill.getPaymentStatus());
        System.out.println("Customer : " + customerName);
        System.out.println("---------------------------------------------------");
        System.out.printf("%-20s %-10s %-10s %-10s%n", "Item", "Qty", "Price", "Total");
        System.out.println("---------------------------------------------------");

        List<OrderItem> orderItems = new OrderItemDAOImplementation()
                .getOrderItemsByOrderId(bill.getOrderId());
        double grandTotal = 0;
        for (OrderItem oi : orderItems) {
            MenuItem mi = menuService.getMenuItemById(oi.getItemId());
            double total = mi.getPrice() * oi.getQuantity();
            grandTotal += total;
            System.out.printf("%-20s %-10d %-10.2f %-10.2f%n",
                    mi.getName(), oi.getQuantity(), mi.getPrice(), total);
        }
        System.out.println("---------------------------------------------------");
        System.out.printf("%-20s %-10s %-10s %-10.2f%n", "Grand Total", "", "", grandTotal);
    }



    private static int getInt() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Enter a valid number: ");
            }
        }
    }
}
