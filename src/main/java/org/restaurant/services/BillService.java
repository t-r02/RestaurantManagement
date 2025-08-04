package org.restaurant.services;

import org.restaurant.dao.interfaces.BillDAO;
import org.restaurant.dao.interfaces.OrderItemDAO;
import org.restaurant.dao.interfaces.MenuItemDAO;
import org.restaurant.dao.implementations.BillDAOImpl;
import org.restaurant.dao.implementations.OrderItemDAOImpl;
import org.restaurant.dao.implementations.MenuItemDAOImpl;
import org.restaurant.models.Bill;
import org.restaurant.models.OrderItem;
import org.restaurant.models.MenuItem;

import java.util.List;

public class BillService {

    private final BillDAO billDAO;
    private final OrderItemDAO orderItemDAO;
    private final MenuItemDAO menuItemDAO;

    public BillService() {
        this.billDAO = new BillDAOImpl();
        this.orderItemDAO = new OrderItemDAOImpl();
        this.menuItemDAO = new MenuItemDAOImpl();
    }

    public int createBill(int orderId, String status) {
        // Prevent duplicate bill for same order
        if (!billDAO.getBillsByOrderId(orderId).isEmpty()) {
            System.out.println("Bill already exists for this order!");
            return -1;
        }

        // Calculate total amount from order items
        List<OrderItem> orderItems = orderItemDAO.getOrderItemsByOrderId(orderId);
        double totalAmount = 0.0;

        for (OrderItem item : orderItems) {
            MenuItem menuItem = menuItemDAO.getMenuItemById(item.getItemId());
            if (menuItem != null) {
                totalAmount += menuItem.getPrice() * item.getQuantity();
            }
        }

        if (totalAmount == 0.0) {
            System.out.println("No items found for this order. Bill not created.");
            return -1;
        }

        Bill bill = new Bill();
        bill.setOrderId(orderId);
        bill.setTotalAmount(totalAmount);
        bill.setPaymentStatus(status);

        return billDAO.addBillAndReturnId(bill);
    }

    public Bill getBillById(int billId) {
        return billDAO.getBillById(billId);
    }

    public boolean cancelBill(int billId) {
        Bill bill = billDAO.getBillById(billId);
        if (bill != null && "Unpaid".equalsIgnoreCase(bill.getPaymentStatus())) {
            bill.setPaymentStatus("Cancelled");
            return billDAO.updateBill(bill);
        }
        System.out.println("Cannot cancel a paid bill.");
        return false;
    }
}
