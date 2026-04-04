package com.gearrentpro.controller;

import com.gearrentpro.entity.SystemUser;
import com.gearrentpro.service.AuthService;

import java.sql.SQLException;

public class AuthController {

    private AuthService authService = new AuthService();

    public SystemUser login(String username, String password) throws SQLException {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username is required.");
        }
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password is required.");
        }
        return authService.login(username, password);
    }

    public void logout() {
        authService.logout();
    }
}