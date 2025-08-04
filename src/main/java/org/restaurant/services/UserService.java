package org.restaurant.services;

import org.restaurant.dao.interfaces.UserDAO;
import org.restaurant.dao.implementations.UserDAOImplementation;
import org.restaurant.models.User;

import java.util.List;

public class UserService {
    private final UserDAO userDAO;
    public UserService() {
        this.userDAO = new UserDAOImplementation();
    }

    // Register new user
    public int registerUser(String name, String role, String username, String password) {
        if (userDAO.getUserByUsername(username) != null) {
            System.out.println("Username already exists!");
            return -1;
        }

        User user = new User();
        user.setName(name);
        user.setRole(role);
        user.setUsername(username);
        user.setPassword(password);

        int userId = userDAO.addUserAndReturnId(user);
        if (userId > 0) {
            System.out.println("User registered successfully! ID: " + userId);
        } else {
            System.out.println("Registration failed!");
        }
        return userId;
    }

    // Login existing user
    public User loginUser(String username, String password) {
        User user = userDAO.getUserByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            System.out.println("Login successful! Welcome, " + user.getFullName());
            return user;
        }
        System.out.println("Invalid username or password.");
        return null;
    }

    // Get user by ID
    public User getUserById(int userId) {
        return userDAO.getUserById(userId);
    }

    // Get all users
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    // Get users by role
    public List<User> getUsersByRole(String role) {
        return userDAO.getUsersByRole(role);
    }

    // Update user details
    public boolean updateUser(User user) {
        return userDAO.updateUser(user);
    }

    // Delete user
    public boolean deleteUser(int userId) {
        return userDAO.deleteUser(userId);
    }
}
