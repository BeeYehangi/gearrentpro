package com.gearrentpro.service;

import com.gearrentpro.dao.SystemUserDAO;
import com.gearrentpro.entity.SystemUser;
import com.gearrentpro.util.SessionManager;

import java.sql.SQLException;

public class AuthService {

    private SystemUserDAO systemUserDAO = new SystemUserDAO();

    public SystemUser login(String username, String password) throws SQLException {
        SystemUser user = systemUserDAO.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found.");
        }
        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("Incorrect password.");
        }
        if (!user.isActive()) {
            throw new IllegalArgumentException("User account is inactive.");
        }
        SessionManager.getInstance().setLoggedInUser(user);
        return user;
    }

    public void logout() {
        SessionManager.getInstance().logout();
    }
}