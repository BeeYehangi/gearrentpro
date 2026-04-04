package com.gearrentpro.dao;

import com.gearrentpro.entity.Membership;
import com.gearrentpro.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MembershipDAO {

    public List<Membership> findAll() throws SQLException {
        String sql = "SELECT * FROM membership";
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        List<Membership> list = new ArrayList<>();
        while (rs.next()) {
            list.add(mapRow(rs));
        }
        return list;
    }

    public Membership findById(String membershipId) throws SQLException {
        String sql = "SELECT * FROM membership WHERE membership_id = ?";
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, membershipId);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return mapRow(rs);
        }
        return null;
    }

    public boolean save(Membership membership) throws SQLException {
        String sql = "INSERT INTO membership (membership_id, level_name, discount_percentage, description) VALUES (?,?,?,?)";
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, membership.getMembershipId());
        stmt.setString(2, membership.getLevelName());
        stmt.setBigDecimal(3, membership.getDiscountPercentage());
        stmt.setString(4, membership.getDescription());
        return stmt.executeUpdate() > 0;
    }

    public boolean update(Membership membership) throws SQLException {
        String sql = "UPDATE membership SET level_name=?, discount_percentage=?, description=? WHERE membership_id=?";
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, membership.getLevelName());
        stmt.setBigDecimal(2, membership.getDiscountPercentage());
        stmt.setString(3, membership.getDescription());
        stmt.setString(4, membership.getMembershipId());
        return stmt.executeUpdate() > 0;
    }

    public boolean delete(String membershipId) throws SQLException {
        String sql = "DELETE FROM membership WHERE membership_id = ?";
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, membershipId);
        return stmt.executeUpdate() > 0;
    }

    private Membership mapRow(ResultSet rs) throws SQLException {
        Membership membership = new Membership();
        membership.setMembershipId(rs.getString("membership_id"));
        membership.setLevelName(rs.getString("level_name"));
        membership.setDiscountPercentage(rs.getBigDecimal("discount_percentage"));
        membership.setDescription(rs.getString("description"));
        return membership;
    }
}