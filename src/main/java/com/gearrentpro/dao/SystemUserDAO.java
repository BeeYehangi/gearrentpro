package com.gearrentpro.dao;

import com.gearrentpro.entity.SystemUser;
import com.gearrentpro.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SystemUserDAO {

    public SystemUser findByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM system_user WHERE username = ? AND is_active = 1";
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, username);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return mapRow(rs);
        }
        return null;
    }

    public SystemUser findById(String userId) throws SQLException {
        String sql = "SELECT * FROM system_user WHERE user_id = ?";
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, userId);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return mapRow(rs);
        }
        return null;
    }

    public List<SystemUser> findAll() throws SQLException {
        String sql = "SELECT * FROM system_user";
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        List<SystemUser> list = new ArrayList<>();
        while (rs.next()) {
            list.add(mapRow(rs));
        }
        return list;
    }

    public boolean save(SystemUser user) throws SQLException {
        String sql = "INSERT INTO system_user (user_id, username, password, full_name, role, branch_id, email, phone, is_active) VALUES (?,?,?,?,?,?,?,?,?)";
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, user.getUserId());
        stmt.setString(2, user.getUsername());
        stmt.setString(3, user.getPassword());
        stmt.setString(4, user.getFullName());
        stmt.setString(5, user.getRole());
        stmt.setString(6, user.getBranchId());
        stmt.setString(7, user.getEmail());
        stmt.setString(8, user.getPhone());
        stmt.setBoolean(9, user.isActive());
        return stmt.executeUpdate() > 0;
    }

    public boolean update(SystemUser user) throws SQLException {
        String sql = "UPDATE system_user SET username=?, password=?, full_name=?, role=?, branch_id=?, email=?, phone=?, is_active=? WHERE user_id=?";
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, user.getUsername());
        stmt.setString(2, user.getPassword());
        stmt.setString(3, user.getFullName());
        stmt.setString(4, user.getRole());
        stmt.setString(5, user.getBranchId());
        stmt.setString(6, user.getEmail());
        stmt.setString(7, user.getPhone());
        stmt.setBoolean(8, user.isActive());
        stmt.setString(9, user.getUserId());
        return stmt.executeUpdate() > 0;
    }

    public boolean delete(String userId) throws SQLException {
        String sql = "UPDATE system_user SET is_active = 0 WHERE user_id = ?";
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, userId);
        return stmt.executeUpdate() > 0;
    }

    private SystemUser mapRow(ResultSet rs) throws SQLException {
        SystemUser user = new SystemUser();
        user.setUserId(rs.getString("user_id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setFullName(rs.getString("full_name"));
        user.setRole(rs.getString("role"));
        user.setBranchId(rs.getString("branch_id"));
        user.setEmail(rs.getString("email"));
        user.setPhone(rs.getString("phone"));
        user.setActive(rs.getBoolean("is_active"));
        return user;
    }
}