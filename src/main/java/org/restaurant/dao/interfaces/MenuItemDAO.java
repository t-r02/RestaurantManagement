package org.restaurant.dao.interfaces;

import org.restaurant.models.MenuItem;
import java.util.List;

public interface MenuItemDAO {
    boolean addMenuItem(MenuItem menuItem);
    MenuItem getMenuItemById(int itemId);
    List<MenuItem> getAllMenuItems();
    List<MenuItem> getMenuItemsByCategory(String category);
    boolean updateMenuItem(MenuItem menuItem);
    boolean deleteMenuItem(int itemId);
}
