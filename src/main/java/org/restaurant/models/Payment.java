package org.restaurant.models;

import java.time.LocalDateTime;

public class Payment {
    private int paymentId;
    private int billId;
    private double amountPaid; // matches "amount_paid" in SQL
    private String paymentMethod; // matches "payment_method" in SQL
    private LocalDateTime paymentDatetime;


    public Payment() {}

    public Payment(int paymentId, int billId, double amountPaid, String paymentMethod, LocalDateTime paymentDatetime) {
        this.paymentId = paymentId;
        this.billId = billId;
        this.amountPaid = amountPaid;
        this.paymentMethod = paymentMethod;
        this.paymentDatetime = paymentDatetime;
    }

    public int getPaymentId() { return paymentId; }
    public void setPaymentId(int paymentId) { this.paymentId = paymentId; }

    public int getBillId() { return billId; }
    public void setBillId(int billId) { this.billId = billId; }

    public double getAmountPaid() { return amountPaid; }
    public void setAmountPaid(double amount) { this.amountPaid = amount; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String method) { this.paymentMethod = method; }

    public LocalDateTime getPaymentDatetime() { return paymentDatetime; }
    public void setPaymentDatetime(LocalDateTime paymentDatetime) { this.paymentDatetime = paymentDatetime; }

    @Override
    public String toString() {
        return "Payment{" +
                "paymentId=" + paymentId +
                ", billId=" + billId +
                ", amount=" + amountPaid +
                ", method='" + paymentMethod + '\'' +
                ", paymentDatetime=" + paymentDatetime +
                '}';
    }
}
