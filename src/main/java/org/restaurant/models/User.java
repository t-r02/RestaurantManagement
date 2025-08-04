package org.restaurant.models;

public class User {
    private int userId;
    private String name;
    private String role; // Customer, Waiter, KitchenStaff, Manager, Admin
    private String username;
    private String password;

    public User() {}

    public User(int userId, String name, String role, String username, String password) {
        this.userId = userId;
        this.name = name;
        this.role = role;
        this.username = username;
        this.password = password;
    }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getFullName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", role='" + role + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
