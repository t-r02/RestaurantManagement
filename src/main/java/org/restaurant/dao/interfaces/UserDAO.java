package org.restaurant.dao.interfaces;

import org.restaurant.models.User;
import java.util.List;

public interface UserDAO {
    int addUserAndReturnId(User user); // ✅ NEW
    User getUserById(int userId);
    User getUserByUsername(String username);
    List<User> getAllUsers();
    List<User> getUsersByRole(String role);
    boolean updateUser(User user);
    boolean deleteUser(int userId);
}
