package com.gearrentpro.dao;

import com.gearrentpro.entity.Category;
import com.gearrentpro.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    public List<Category> findAll() throws SQLException {
        String sql = "SELECT * FROM category";
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        List<Category> list = new ArrayList<>();
        while (rs.next()) {
            list.add(mapRow(rs));
        }
        return list;
    }

    public List<Category> findAllActive() throws SQLException {
        String sql = "SELECT * FROM category WHERE is_active = 1";
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        List<Category> list = new ArrayList<>();
        while (rs.next()) {
            list.add(mapRow(rs));
        }
        return list;
    }

    public Category findById(String categoryId) throws SQLException {
        String sql = "SELECT * FROM category WHERE category_id = ?";
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, categoryId);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return mapRow(rs);
        }
        return null;
    }

    public boolean save(Category category) throws SQLException {
        String sql = "INSERT INTO category (category_id, category_name, description, base_price_factor, weekend_multiplier, late_fee_per_day, is_active) VALUES (?,?,?,?,?,?,?)";
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, category.getCategoryId());
        stmt.setString(2, category.getCategoryName());
        stmt.setString(3, category.getDescription());
        stmt.setBigDecimal(4, category.getBasePriceFactor());
        stmt.setBigDecimal(5, category.getWeekendMultiplier());
        stmt.setBigDecimal(6, category.getLateFeePerDay());
        stmt.setBoolean(7, category.isActive());
        return stmt.executeUpdate() > 0;
    }

    public boolean update(Category category) throws SQLException {
        String sql = "UPDATE category SET category_name=?, description=?, base_price_factor=?, weekend_multiplier=?, late_fee_per_day=?, is_active=? WHERE category_id=?";
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, category.getCategoryName());
        stmt.setString(2, category.getDescription());
        stmt.setBigDecimal(3, category.getBasePriceFactor());
        stmt.setBigDecimal(4, category.getWeekendMultiplier());
        stmt.setBigDecimal(5, category.getLateFeePerDay());
        stmt.setBoolean(6, category.isActive());
        stmt.setString(7, category.getCategoryId());
        return stmt.executeUpdate() > 0;
    }

    public boolean delete(String categoryId) throws SQLException {
        String sql = "UPDATE category SET is_active = 0 WHERE category_id = ?";
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, categoryId);
        return stmt.executeUpdate() > 0;
    }

    private Category mapRow(ResultSet rs) throws SQLException {
        Category category = new Category();
        category.setCategoryId(rs.getString("category_id"));
        category.setCategoryName(rs.getString("category_name"));
        category.setDescription(rs.getString("description"));
        category.setBasePriceFactor(rs.getBigDecimal("base_price_factor"));
        category.setWeekendMultiplier(rs.getBigDecimal("weekend_multiplier"));
        category.setLateFeePerDay(rs.getBigDecimal("late_fee_per_day"));
        category.setActive(rs.getBoolean("is_active"));
        return category;
    }
}