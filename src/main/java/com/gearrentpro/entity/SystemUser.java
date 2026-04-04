package com.gearrentpro.entity;

public class SystemUser {
    private String userId;
    private String username;
    private String password;
    private String fullName;
    private String role;
    private String branchId;
    private String email;
    private String phone;
    private boolean isActive;

    public SystemUser() {}

    public SystemUser(String userId, String username, String password,
                      String fullName, String role, String branchId,
                      String email, String phone, boolean isActive) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.role = role;
        this.branchId = branchId;
        this.email = email;
        this.phone = phone;
        this.isActive = isActive;
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getBranchId() { return branchId; }
    public void setBranchId(String branchId) { this.branchId = branchId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    @Override
    public String toString() { return fullName + " (" + role + ")"; }
}