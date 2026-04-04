package com.gearrentpro.dao;

import com.gearrentpro.entity.Rental;
import com.gearrentpro.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RentalDAO {

    public List<Rental> findAll() throws SQLException {
        String sql = "SELECT * FROM rental";
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        List<Rental> list = new ArrayList<>();
        while (rs.next()) {
            list.add(mapRow(rs));
        }
        return list;
    }

    public List<Rental> findByBranch(String branchId) throws SQLException {
        String sql = "SELECT * FROM rental WHERE branch_id = ?";
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, branchId);
        ResultSet rs = stmt.executeQuery();
        List<Rental> list = new ArrayList<>();
        while (rs.next()) {
            list.add(mapRow(rs));
        }
        return list;
    }

    public List<Rental> findByCustomer(String customerId) throws SQLException {
        String sql = "SELECT * FROM rental WHERE customer_id = ?";
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, customerId);
        ResultSet rs = stmt.executeQuery();
        List<Rental> list = new ArrayList<>();
        while (rs.next()) {
            list.add(mapRow(rs));
        }
        return list;
    }

    public List<Rental> findOverdue() throws SQLException {
        String sql = "SELECT * FROM rental WHERE rental_status = 'ACTIVE' AND end_date < CURDATE()";
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        List<Rental> list = new ArrayList<>();
        while (rs.next()) {
            list.add(mapRow(rs));
        }
        return list;
    }

    public Rental findById(String rentalId) throws SQLException {
        String sql = "SELECT * FROM rental WHERE rental_id = ?";
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, rentalId);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return mapRow(rs);
        }
        return null;
    }

    public boolean hasOverlap(String equipmentId, String startDate,
                               String endDate, String excludeId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM rental WHERE equipment_id = ? " +
                     "AND rental_status NOT IN ('RETURNED','CANCELLED') " +
                     "AND rental_id != ? " +
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

    public boolean save(Rental rental) throws SQLException {
        String sql = "INSERT INTO rental (rental_id, reservation_id, equipment_id, customer_id, branch_id, start_date, end_date, calculated_rental_amount, security_deposit, membership_discount, long_rental_discount, final_payable_amount, late_fee, damage_charges, payment_status, rental_status, created_by) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, rental.getRentalId());
        stmt.setString(2, rental.getReservationId());
        stmt.setString(3, rental.getEquipmentId());
        stmt.setString(4, rental.getCustomerId());
        stmt.setString(5, rental.getBranchId());
        stmt.setDate(6, Date.valueOf(rental.getStartDate()));
        stmt.setDate(7, Date.valueOf(rental.getEndDate()));
        stmt.setBigDecimal(8, rental.getCalculatedRentalAmount());
        stmt.setBigDecimal(9, rental.getSecurityDeposit());
        stmt.setBigDecimal(10, rental.getMembershipDiscount());
        stmt.setBigDecimal(11, rental.getLongRentalDiscount());
        stmt.setBigDecimal(12, rental.getFinalPayableAmount());
        stmt.setBigDecimal(13, rental.getLateFee());
        stmt.setBigDecimal(14, rental.getDamageCharges());
        stmt.setString(15, rental.getPaymentStatus());
        stmt.setString(16, rental.getRentalStatus());
        stmt.setString(17, rental.getCreatedBy());
        return stmt.executeUpdate() > 0;
    }

    public boolean update(Rental rental) throws SQLException {
        String sql = "UPDATE rental SET actual_return_date=?, late_fee=?, damage_charges=?, payment_status=?, rental_status=?, returned_by=? WHERE rental_id=?";
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setDate(1, rental.getActualReturnDate() != null ? Date.valueOf(rental.getActualReturnDate()) : null);
        stmt.setBigDecimal(2, rental.getLateFee());
        stmt.setBigDecimal(3, rental.getDamageCharges());
        stmt.setString(4, rental.getPaymentStatus());
        stmt.setString(5, rental.getRentalStatus());
        stmt.setString(6, rental.getReturnedBy());
        stmt.setString(7, rental.getRentalId());
        return stmt.executeUpdate() > 0;
    }

    private Rental mapRow(ResultSet rs) throws SQLException {
        Rental rental = new Rental();
        rental.setRentalId(rs.getString("rental_id"));
        rental.setReservationId(rs.getString("reservation_id"));
        rental.setEquipmentId(rs.getString("equipment_id"));
        rental.setCustomerId(rs.getString("customer_id"));
        rental.setBranchId(rs.getString("branch_id"));
        rental.setStartDate(rs.getDate("start_date").toLocalDate());
        rental.setEndDate(rs.getDate("end_date").toLocalDate());
        Date returnDate = rs.getDate("actual_return_date");
        if (returnDate != null) {
            rental.setActualReturnDate(returnDate.toLocalDate());
        }
        rental.setCalculatedRentalAmount(rs.getBigDecimal("calculated_rental_amount"));
        rental.setSecurityDeposit(rs.getBigDecimal("security_deposit"));
        rental.setMembershipDiscount(rs.getBigDecimal("membership_discount"));
        rental.setLongRentalDiscount(rs.getBigDecimal("long_rental_discount"));
        rental.setFinalPayableAmount(rs.getBigDecimal("final_payable_amount"));
        rental.setLateFee(rs.getBigDecimal("late_fee"));
        rental.setDamageCharges(rs.getBigDecimal("damage_charges"));
        rental.setPaymentStatus(rs.getString("payment_status"));
        rental.setRentalStatus(rs.getString("rental_status"));
        rental.setCreatedBy(rs.getString("created_by"));
        rental.setReturnedBy(rs.getString("returned_by"));
        return rental;
    }
}