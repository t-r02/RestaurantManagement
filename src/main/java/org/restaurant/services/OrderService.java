package org.restaurant.services;

import org.restaurant.dao.interfaces.OrderDAO;
import org.restaurant.dao.interfaces.OrderItemDAO;
import org.restaurant.dao.implementations.OrderDAOImpl;
import org.restaurant.dao.implementations.OrderItemDAOImpl;
import org.restaurant.models.Order;
import org.restaurant.models.OrderItem;

import java.util.List;

public class OrderService {

    private final OrderDAO orderDAO;
    private final OrderItemDAO orderItemDAO;

    public OrderService(OrderDAO orderDAO, OrderItemDAO orderItemDAO) {
        this.orderDAO = orderDAO;
        this.orderItemDAO = orderItemDAO;
    }

    // Place a new order and send to kitchen
    public boolean placeOrder(int tableId, int waiterId, List<OrderItem> orderItems) {
        // Step 1: Create order with status "Pending"
        Order order = new Order();
        order.setTableId(tableId);
        order.setWaiterId(waiterId);
        order.setStatus("Pending");

        int orderId = orderDAO.addOrderAndReturnId(order);
        if (orderId <= 0) {
            System.out.println("Failed to create order.");
            return false;
        }

        // Step 2: Add all items to the order
        boolean allItemsAdded = true;
        for (OrderItem item : orderItems) {
            item.setOrderId(orderId);
            if (!orderItemDAO.addOrderItem(item)) {
                allItemsAdded = false;
            }
        }

        if (allItemsAdded) {
            System.out.println("Order placed successfully and sent to kitchen.");
            return true;
        } else {
            System.out.println("Order created, but some items failed to add.");
            return false;
        }
    }

    // Kitchen updates status to Prepared
    public boolean markOrderAsPrepared(int orderId) {
        Order order = orderDAO.getOrderById(orderId);
        if (order == null) {
            System.out.println("Order not found.");
            return false;
        }
        order.setStatus("Prepared");
        return orderDAO.updateOrder(order);
    }

    // Kitchen can fetch all pending orders
    public void showPendingOrders() {
        List<Order> pendingOrders = orderDAO.getOrdersByStatus("Pending");
        if (pendingOrders.isEmpty()) {
            System.out.println("No pending orders.");
        } else {
            System.out.println("Pending orders:");
            for (Order o : pendingOrders) {
                System.out.println(o);
            }
        }
    }
}
