package org.restaurant.dao.interfaces;

import org.restaurant.models.Table;
import java.util.List;

public interface TableDAO {
    boolean addTable(Table table);
    Table getTableById(int tableId);
    List<Table> getAllTables();
    List<Table> getTablesByStatus(String status);
    boolean updateTable(Table table);
    boolean deleteTable(int tableId);
}
