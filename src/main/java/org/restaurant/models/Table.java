package org.restaurant.models;

public class Table {
    private int tableId;
    private int capacity;
    private String status; // Available, Reserved, Occupied

    public Table() {}

    public Table(int tableId, int capacity, String status) {
        this.tableId = tableId;
        this.capacity = capacity;
        this.status = status;
    }

    public int getTableId() { return tableId; }
    public void setTableId(int tableId) { this.tableId = tableId; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "Table{" +
                "tableId=" + tableId +
                ", capacity=" + capacity +
                ", status='" + status + '\'' +
                '}';
    }
}
