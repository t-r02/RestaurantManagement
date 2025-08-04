package org.restaurant.dao.interfaces;

import org.restaurant.models.Order;
import java.util.List;

public interface OrderDAO {
    int addOrderAndReturnId(Order order);
    Order getOrderById(int orderId);
    List<Order> getAllOrders();
    List<Order> getOrdersByTableId(int tableId);
    List<Order> getOrdersByWaiterId(int waiterId);
    List<Order> getOrdersByStatus(String status);
    boolean updateOrder(Order order);
    boolean deleteOrder(int orderId);
}
