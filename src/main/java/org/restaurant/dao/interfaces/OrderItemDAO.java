package org.restaurant.dao.interfaces;

import org.restaurant.models.OrderItem;
import java.util.List;

public interface OrderItemDAO {
    boolean addOrderItem(OrderItem orderItem);
    OrderItem getOrderItemById(int orderItemId);
    List<OrderItem> getAllOrderItems();
    List<OrderItem> getOrderItemsByOrderId(int orderId);
    boolean updateOrderItem(OrderItem orderItem);
    boolean deleteOrderItem(int orderItemId);
    boolean deleteOrderItemsByOrderId(int orderId);
}