package com.gearrentpro.dao;

import com.gearrentpro.entity.Equipment;
import com.gearrentpro.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EquipmentDAO {

    public List<Equipment> findAll() throws SQLException {
        String sql = "SELECT * FROM equipment";
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        List<Equipment> list = new ArrayList<>();
        while (rs.next()) {
            list.add(mapRow(rs));
        }
        return list;
    }

    public List<Equipment> findByBranch(String branchId) throws SQLException {
        String sql = "SELECT * FROM equipment WHERE branch_id = ?";
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, branchId);
        ResultSet rs = stmt.executeQuery();
        List<Equipment> list = new ArrayList<>();
        while (rs.next()) {
            list.add(mapRow(rs));
        }
        return list;
    }

    public List<Equipment> findAvailableByBranch(String branchId) throws SQLException {
        String sql = "SELECT * FROM equipment WHERE branch_id = ? AND status = 'AVAILABLE'";
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, branchId);
        ResultSet rs = stmt.executeQuery();
        List<Equipment> list = new ArrayList<>();
        while (rs.next()) {
            list.add(mapRow(rs));
        }
        return list;
    }

    public List<Equipment> findByBranchAndCategory(String branchId, String categoryId) throws SQLException {
        String sql = "SELECT * FROM equipment WHERE branch_id = ? AND category_id = ?";
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, branchId);
        stmt.setString(2, categoryId);
        ResultSet rs = stmt.executeQuery();
        List<Equipment> list = new ArrayList<>();
        while (rs.next()) {
            list.add(mapRow(rs));
        }
        return list;
    }

    public Equipment findById(String equipmentId) throws SQLException {
        String sql = "SELECT * FROM equipment WHERE equipment_id = ?";
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, equipmentId);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return mapRow(rs);
        }
        return null;
    }

    public boolean save(Equipment equipment) throws SQLException {
        String sql = "INSERT INTO equipment (equipment_id, category_id, branch_id, brand, model, serial_number, purchase_year, base_daily_price, deposit_amount, status, notes) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, equipment.getEquipmentId());
        stmt.setString(2, equipment.getCategoryId());
        stmt.setString(3, equipment.getBranchId());
        stmt.setString(4, equipment.getBrand());
        stmt.setString(5, equipment.getModel());
        stmt.setString(6, equipment.getSerialNumber());
        stmt.setInt(7, equipment.getPurchaseYear());
        stmt.setBigDecimal(8, equipment.getBaseDailyPrice());
        stmt.setBigDecimal(9, equipment.getDepositAmount());
        stmt.setString(10, equipment.getStatus());
        stmt.setString(11, equipment.getNotes());
        return stmt.executeUpdate() > 0;
    }

    public boolean update(Equipment equipment) throws SQLException {
        String sql = "UPDATE equipment SET category_id=?, branch_id=?, brand=?, model=?, serial_number=?, purchase_year=?, base_daily_price=?, deposit_amount=?, status=?, notes=? WHERE equipment_id=?";
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, equipment.getCategoryId());
        stmt.setString(2, equipment.getBranchId());
        stmt.setString(3, equipment.getBrand());
        stmt.setString(4, equipment.getModel());
        stmt.setString(5, equipment.getSerialNumber());
        stmt.setInt(6, equipment.getPurchaseYear());
        stmt.setBigDecimal(7, equipment.getBaseDailyPrice());
        stmt.setBigDecimal(8, equipment.getDepositAmount());
        stmt.setString(9, equipment.getStatus());
        stmt.setString(10, equipment.getNotes());
        stmt.setString(11, equipment.getEquipmentId());
        return stmt.executeUpdate() > 0;
    }

    public boolean updateStatus(String equipmentId, String status) throws SQLException {
        String sql = "UPDATE equipment SET status = ? WHERE equipment_id = ?";
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, status);
        stmt.setString(2, equipmentId);
        return stmt.executeUpdate() > 0;
    }

    public boolean delete(String equipmentId) throws SQLException {
        String sql = "DELETE FROM equipment WHERE equipment_id = ?";
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, equipmentId);
        return stmt.executeUpdate() > 0;
    }

    private Equipment mapRow(ResultSet rs) throws SQLException {
        Equipment equipment = new Equipment();
        equipment.setEquipmentId(rs.getString("equipment_id"));
        equipment.setCategoryId(rs.getString("category_id"));
        equipment.setBranchId(rs.getString("branch_id"));
        equipment.setBrand(rs.getString("brand"));
        equipment.setModel(rs.getString("model"));
        equipment.setSerialNumber(rs.getString("serial_number"));
        equipment.setPurchaseYear(rs.getInt("purchase_year"));
        equipment.setBaseDailyPrice(rs.getBigDecimal("base_daily_price"));
        equipment.setDepositAmount(rs.getBigDecimal("deposit_amount"));
        equipment.setStatus(rs.getString("status"));
        equipment.setNotes(rs.getString("notes"));
        return equipment;
    }
}