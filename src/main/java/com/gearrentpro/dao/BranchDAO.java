package com.gearrentpro.dao;

import com.gearrentpro.entity.Branch;
import com.gearrentpro.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BranchDAO {

    public List<Branch> findAll() throws SQLException {
        String sql = "SELECT * FROM branch";
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        List<Branch> list = new ArrayList<>();
        while (rs.next()) {
            list.add(mapRow(rs));
        }
        return list;
    }

    public Branch findById(String branchId) throws SQLException {
        String sql = "SELECT * FROM branch WHERE branch_id = ?";
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, branchId);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return mapRow(rs);
        }
        return null;
    }

    public boolean save(Branch branch) throws SQLException {
        String sql = "INSERT INTO branch (branch_id, branch_name, address, contact_phone, contact_email, manager_id) VALUES (?,?,?,?,?,?)";
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, branch.getBranchId());
        stmt.setString(2, branch.getBranchName());
        stmt.setString(3, branch.getAddress());
        stmt.setString(4, branch.getContactPhone());
        stmt.setString(5, branch.getContactEmail());
        stmt.setString(6, branch.getManagerId());
        return stmt.executeUpdate() > 0;
    }

    public boolean update(Branch branch) throws SQLException {
        String sql = "UPDATE branch SET branch_name=?, address=?, contact_phone=?, contact_email=?, manager_id=? WHERE branch_id=?";
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, branch.getBranchName());
        stmt.setString(2, branch.getAddress());
        stmt.setString(3, branch.getContactPhone());
        stmt.setString(4, branch.getContactEmail());
        stmt.setString(5, branch.getManagerId());
        stmt.setString(6, branch.getBranchId());
        return stmt.executeUpdate() > 0;
    }

    public boolean delete(String branchId) throws SQLException {
        String sql = "DELETE FROM branch WHERE branch_id = ?";
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, branchId);
        return stmt.executeUpdate() > 0;
    }

    private Branch mapRow(ResultSet rs) throws SQLException {
        Branch branch = new Branch();
        branch.setBranchId(rs.getString("branch_id"));
        branch.setBranchName(rs.getString("branch_name"));
        branch.setAddress(rs.getString("address"));
        branch.setContactPhone(rs.getString("contact_phone"));
        branch.setContactEmail(rs.getString("contact_email"));
        branch.setManagerId(rs.getString("manager_id"));
        return branch;
    }
}