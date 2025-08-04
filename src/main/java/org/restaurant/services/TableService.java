package org.restaurant.services;

import org.restaurant.dao.interfaces.TableDAO;
import org.restaurant.dao.implementations.TableDAOImpl;
import org.restaurant.models.Table;

import java.util.List;

public class TableService {

    private final TableDAO tableDAO;

    public TableService() {
        this.tableDAO = new TableDAOImpl();
    }

    public boolean addTable(int capacity, String status) {
        Table table = new Table();
        table.setCapacity(capacity);
        table.setStatus(status);
        return tableDAO.addTable(table);
    }

    public Table getTableById(int tableId) {
        return tableDAO.getTableById(tableId);
    }

    public List<Table> getAllTables() {
        return tableDAO.getAllTables();
    }

    public boolean updateTable(Table table) {
        return tableDAO.updateTable(table);
    }

    public boolean deleteTable(int tableId) {
        return tableDAO.deleteTable(tableId);
    }
}