package com.gearrentpro.dao;

import com.gearrentpro.entity.Reservation;
import com.gearrentpro.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {

    public List<Reservation> findAll() throws SQLException {
        String sql = "SELECT * FROM reservation";
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        List<Reservation> list = new ArrayList<>();
        while (rs.next()) {
            list.add(mapRow(rs));
        }
        return list;
    }

    public List<Reservation> findByBranch(String branchId) throws SQLException {
        String sql = "SELECT * FROM reservation WHERE branch_id = ?";
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, branchId);
        ResultSet rs = stmt.executeQuery();
        List<Reservation> list = new ArrayList<>();
        while (rs.next()) {
            list.add(mapRow(rs));
        }
        return list;
    }

    public List<Reservation> findByCustomer(String customerId) throws SQLException {
        String sql = "SELECT * FROM reservation WHERE customer_id = ?";
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, customerId);
        ResultSet rs = stmt.executeQuery();
        List<Reservation> list = new ArrayList<>();
        while (rs.next()) {
            list.add(mapRow(rs));
        }
        return list;
    }

    public Reservation findById(String reservationId) throws SQLException {
        String sql = "SELECT * FROM reservation WHERE reservation_id = ?";
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, reservationId);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return mapRow(rs);
        }
        return null;
    }

    public boolean hasOverlap(String equipmentId, String startDate, 
                               String endDate, String excludeId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM reservation WHERE equipment_id = ? " +
                     "AND status NOT IN ('CANCELLED','CONVERTED') " +
                     "AND reservation_id != ? " +
                     "AND NOT (end_date < ? OR start_date > ?)";
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, equipmentId);
        stmt.setString(2, excludeId);
        stmt.setString(3, startDate);
        stmt.setString(4, endDate);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
        return false;
    }

    public boolean save(Reservation reservation) throws SQLException {
        String sql = "INSERT INTO reservation (reservation_id, equipment_id, customer_id, branch_id, start_date, end_date, status, created_by) VALUES (?,?,?,?,?,?,?,?)";
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, reservation.getReservationId());
        stmt.setString(2, reservation.getEquipmentId());
        stmt.setString(3, reservation.getCustomerId());
        stmt.setString(4, reservation.getBranchId());
        stmt.setDate(5, Date.valueOf(reservation.getStartDate()));
        stmt.setDate(6, Date.valueOf(reservation.getEndDate()));
        stmt.setString(7, reservation.getStatus());
        stmt.setString(8, reservation.getCreatedBy());
        return stmt.executeUpdate() > 0;
    }

    public boolean updateStatus(String reservationId, String status) throws SQLException {
        String sql = "UPDATE reservation SET status = ? WHERE reservation_id = ?";
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, status);
        stmt.setString(2, reservationId);
        return stmt.executeUpdate() > 0;
    }

    private Reservation mapRow(ResultSet rs) throws SQLException {
        Reservation reservation = new Reservation();
        reservation.setReservationId(rs.getString("reservation_id"));
        reservation.setEquipmentId(rs.getString("equipment_id"));
        reservation.setCustomerId(rs.getString("customer_id"));
        reservation.setBranchId(rs.getString("branch_id"));
        reservation.setStartDate(rs.getDate("start_date").toLocalDate());
        reservation.setEndDate(rs.getDate("end_date").toLocalDate());
        reservation.setStatus(rs.getString("status"));
        reservation.setCreatedBy(rs.getString("created_by"));
        return reservation;
    }
}