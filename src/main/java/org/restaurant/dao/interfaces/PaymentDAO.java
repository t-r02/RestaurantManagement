package org.restaurant.dao.interfaces;

import org.restaurant.models.Payment;
import java.util.List;

public interface PaymentDAO {
    boolean addPayment(Payment payment);
    Payment getPaymentById(int paymentId);
    Payment getPaymentByBillId(int billId);
    List<Payment> getAllPayments();
    boolean updatePayment(Payment payment);
    boolean deletePayment(int paymentId);
}