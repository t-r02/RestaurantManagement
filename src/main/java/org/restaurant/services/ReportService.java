package org.restaurant.services;

import org.restaurant.config.DBConnection;
import java.sql.*;

public class ReportService {
    public void generateDailySalesReport(String date) {
        String sql = """
        SELECT b.bill_id, b.order_id, b.total_amount, b.status AS payment_status
        FROM Bills b
        WHERE DATE((SELECT MIN(p.payment_datetime) 
                    FROM Payments p WHERE p.bill_id = b.bill_id)) = ?
           OR (b.status != 'Paid' AND DATE(b.created_at) = ?)
        ORDER BY b.bill_id;
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            java.sql.Date sqlDate = java.sql.Date.valueOf(date);
            stmt.setDate(1, sqlDate);
            stmt.setDate(2, sqlDate);

            ResultSet rs = stmt.executeQuery();

            double totalSales = 0;
            int totalOrders = 0;

            System.out.println("\n📅 Daily Sales Report for " + date);
            System.out.printf("%-8s %-8s %-12s %-10s%n", "Bill ID", "OrderID", "Amount", "Status");
            System.out.println("-------------------------------------------------");

            while (rs.next()) {
                int billId = rs.getInt("bill_id");
                int orderId = rs.getInt("order_id");
                double amount = rs.getDouble("total_amount");
                String status = rs.getString("payment_status");

                totalOrders++;
                if ("Paid".equalsIgnoreCase(status)) {
                    totalSales += amount;
                }

                System.out.printf("%-8d %-8d %-12.2f %-10s%n", billId, orderId, amount, status);
            }
            System.out.println("-------------------------------------------------");
            System.out.printf("Total Orders: %d | Total Sales (Paid only): ₹%.2f%n", totalOrders, totalSales);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void generateAllReports() {
        String sql = """
        SELECT DATE(b.created_at) AS report_date,
               COUNT(b.bill_id) AS total_orders,
               SUM(CASE WHEN b.status='Paid' THEN b.total_amount ELSE 0 END) AS total_sales
        FROM Bills b
        GROUP BY DATE(b.created_at)
        ORDER BY report_date DESC;
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            System.out.println("\n📊 All Sales Reports");
            System.out.printf("%-12s %-15s %-15s%n", "Date", "Total Orders", "Total Sales (Paid)");
            System.out.println("-----------------------------------------------------");

            while (rs.next()) {
                String date = rs.getString("report_date");
                int orders = rs.getInt("total_orders");
                double sales = rs.getDouble("total_sales");
                System.out.printf("%-12s %-15d %-15.2f%n", date, orders, sales);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
