package org.restaurant.services;

import org.restaurant.dao.interfaces.MenuItemDAO;
import org.restaurant.dao.implementations.MenuItemDAOImpl;
import org.restaurant.models.MenuItem;

import java.util.List;

public class MenuService {
    private final MenuItemDAO menuItemDAO;

    public MenuService() {
        this.menuItemDAO = new MenuItemDAOImpl();
    }

    public boolean addMenuItem(String name, String category, double price) {
        MenuItem item = new MenuItem();
        item.setName(name);
        item.setCategory(category);
        item.setPrice(price);
        return menuItemDAO.addMenuItem(item);
    }

    public MenuItem getMenuItemById(int id) {
        return menuItemDAO.getMenuItemById(id);
    }

    public List<MenuItem> getAllMenuItems() {
        return menuItemDAO.getAllMenuItems();
    }

    public boolean updateMenuItem(MenuItem item) {
        return menuItemDAO.updateMenuItem(item);
    }

    public boolean deleteMenuItem(int id) {
        return menuItemDAO.deleteMenuItem(id);
    }
}
