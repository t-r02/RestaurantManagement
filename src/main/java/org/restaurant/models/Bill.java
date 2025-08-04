package org.restaurant.models;

public class Bill {
    private int billId;
    private int orderId;
    private double totalAmount;
    private String status; // Unpaid, Paid

    public Bill() {}

    public Bill(int billId, int orderId, double totalAmount, String status) {
        this.billId = billId;
        this.orderId = orderId;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public int getBillId() { return billId; }
    public void setBillId(int billId) { this.billId = billId; }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public String getPaymentStatus() { return status; }
    public void setPaymentStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "Bill{" +
                "billId=" + billId +
                ", orderId=" + orderId +
                ", totalAmount=" + totalAmount +
                ", status='" + status + '\'' +
                '}';
    }
}
