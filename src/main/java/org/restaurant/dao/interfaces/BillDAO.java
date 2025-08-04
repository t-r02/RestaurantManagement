package org.restaurant.dao.interfaces;

import org.restaurant.models.Bill;
import java.util.List;

public interface BillDAO {
    int addBillAndReturnId(Bill bill);
    Bill getBillById(int billId);
    List<Bill> getAllBills();
    List<Bill> getBillsByOrderId(int orderId);
    boolean updateBill(Bill bill);
}
