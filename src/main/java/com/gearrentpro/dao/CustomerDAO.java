package com.gearrentpro.dao;

import com.gearrentpro.entity.Customer;
import com.gearrentpro.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    public List<Customer> findAll() throws SQLException {
        String sql = "SELECT * FROM customer";
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        List<Customer> list = new ArrayList<>();
        while (rs.next()) {
            list.add(mapRow(rs));
        }
        return list;
    }

    public Customer findById(String customerId) throws SQLException {
        String sql = "SELECT * FROM customer WHERE customer_id = ?";
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, customerId);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return mapRow(rs);
        }
        return null;
    }

    public Customer findByNic(String nic) throws SQLException {
        String sql = "SELECT * FROM customer WHERE nic_passport = ?";
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, nic);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return mapRow(rs);
        }
        return null;
    }

    public boolean save(Customer customer) throws SQLException {
        String sql = "INSERT INTO customer (customer_id, nic_passport, full_name, email, phone, address, membership_id, total_deposit_held, is_active) VALUES (?,?,?,?,?,?,?,?,?)";
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, customer.getCustomerId());
        stmt.setString(2, customer.getNicPassport());
        stmt.setString(3, customer.getFullName());
        stmt.setString(4, customer.getEmail());
        stmt.setString(5, customer.getPhone());
        stmt.setString(6, customer.getAddress());
        stmt.setString(7, customer.getMembershipId());
        stmt.setBigDecimal(8, customer.getTotalDepositHeld());
        stmt.setBoolean(9, customer.isActive());
        return stmt.executeUpdate() > 0;
    }

    public boolean update(Customer customer) throws SQLException {
        String sql = "UPDATE customer SET nic_passport=?, full_name=?, email=?, phone=?, address=?, membership_id=?, total_deposit_held=?, is_active=? WHERE customer_id=?";
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, customer.getNicPassport());
        stmt.setString(2, customer.getFullName());
        stmt.setString(3, customer.getEmail());
        stmt.setString(4, customer.getPhone());
        stmt.setString(5, customer.getAddress());
        stmt.setString(6, customer.getMembershipId());
        stmt.setBigDecimal(7, customer.getTotalDepositHeld());
        stmt.setBoolean(8, customer.isActive());
        stmt.setString(9, customer.getCustomerId());
        return stmt.executeUpdate() > 0;
    }

    public boolean updateDepositHeld(String customerId, java.math.BigDecimal amount) throws SQLException {
        String sql = "UPDATE customer SET total_deposit_held = ? WHERE customer_id = ?";
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setBigDecimal(1, amount);
        stmt.setString(2, customerId);
        return stmt.executeUpdate() > 0;
    }

    public boolean delete(String customerId) throws SQLException {
        String sql = "UPDATE customer SET is_active = 0 WHERE customer_id = ?";
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, customerId);
        return stmt.executeUpdate() > 0;
    }

    private Customer mapRow(ResultSet rs) throws SQLException {
        Customer customer = new Customer();
        customer.setCustomerId(rs.getString("customer_id"));
        customer.setNicPassport(rs.getString("nic_passport"));
        customer.setFullName(rs.getString("full_name"));
        customer.setEmail(rs.getString("email"));
        customer.setPhone(rs.getString("phone"));
        customer.setAddress(rs.getString("address"));
        customer.setMembershipId(rs.getString("membership_id"));
        customer.setTotalDepositHeld(rs.getBigDecimal("total_deposit_held"));
        customer.setActive(rs.getBoolean("is_active"));
        Timestamp ts = rs.getTimestamp("created_date");
        if (ts != null) {
            customer.setCreatedDate(ts.toLocalDateTime());
        }
        return customer;
    }
}