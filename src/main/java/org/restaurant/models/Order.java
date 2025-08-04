package org.restaurant.models;

public class Order {
    private int orderId;
    private int tableId;
    private int waiterId;
    private String status; // Pending, Prepared, Served

    public Order() {}

    public Order(int orderId, int tableId, int waiterId, String status) {
        this.orderId = orderId;
        this.tableId = tableId;
        this.waiterId = waiterId;
        this.status = status;
    }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public int getTableId() { return tableId; }
    public void setTableId(int tableId) { this.tableId = tableId; }

    public int getWaiterId() { return waiterId; }
    public void setWaiterId(int waiterId) { this.waiterId = waiterId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", tableId=" + tableId +
                ", waiterId=" + waiterId +
                ", status='" + status + '\'' +
                '}';
    }
}
