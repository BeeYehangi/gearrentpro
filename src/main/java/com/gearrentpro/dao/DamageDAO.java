package com.gearrentpro.dao;

import com.gearrentpro.entity.Damage;
import com.gearrentpro.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DamageDAO {

    public List<Damage> findByRental(String rentalId) throws SQLException {
        String sql = "SELECT * FROM damage WHERE rental_id = ?";
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, rentalId);
        ResultSet rs = stmt.executeQuery();
        List<Damage> list = new ArrayList<>();
        while (rs.next()) {
            list.add(mapRow(rs));
        }
        return list;
    }

    public boolean save(Damage damage) throws SQLException {
        String sql = "INSERT INTO damage (rental_id, equipment_id, description, charge_amount, reported_by) VALUES (?,?,?,?,?)";
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, damage.getRentalId());
        stmt.setString(2, damage.getEquipmentId());
        stmt.setString(3, damage.getDescription());
        stmt.setBigDecimal(4, damage.getChargeAmount());
        stmt.setString(5, damage.getReportedBy());
        return stmt.executeUpdate() > 0;
    }

    private Damage mapRow(ResultSet rs) throws SQLException {
        Damage damage = new Damage();
        damage.setDamageId(rs.getInt("damage_id"));
        damage.setRentalId(rs.getString("rental_id"));
        damage.setEquipmentId(rs.getString("equipment_id"));
        damage.setDescription(rs.getString("description"));
        damage.setChargeAmount(rs.getBigDecimal("charge_amount"));
        damage.setReportedBy(rs.getString("reported_by"));
        return damage;
    }
}