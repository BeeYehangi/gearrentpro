package com.gearrentpro.service;

import com.gearrentpro.dao.ReservationDAO;
import com.gearrentpro.dao.RentalDAO;
import com.gearrentpro.entity.Reservation;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class ReservationService {

    private ReservationDAO reservationDAO = new ReservationDAO();
    private RentalDAO rentalDAO = new RentalDAO();
    private CustomerService customerService = new CustomerService();

    public List<Reservation> getAllReservations() throws SQLException {
        return reservationDAO.findAll();
    }

    public List<Reservation> getReservationsByBranch(String branchId) throws SQLException {
        return reservationDAO.findByBranch(branchId);
    }

    public List<Reservation> getReservationsByCustomer(String customerId) throws SQLException {
        return reservationDAO.findByCustomer(customerId);
    }

    public Reservation getReservationById(String reservationId) throws SQLException {
        return reservationDAO.findById(reservationId);
    }

    public void createReservation(Reservation reservation, 
                                   java.math.BigDecimal depositAmount) throws SQLException {
        // Validate dates
        if (reservation.getStartDate() == null || reservation.getEndDate() == null) {
            throw new IllegalArgumentException("Start and end dates are required.");
        }
        if (reservation.getStartDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Start date cannot be in the past.");
        }
        if (reservation.getEndDate().isBefore(reservation.getStartDate())) {
            throw new IllegalArgumentException("End date cannot be before start date.");
        }

        // Validate duration
        long days = ChronoUnit.DAYS.between(
            reservation.getStartDate(), reservation.getEndDate());
        if (days > 30) {
            throw new IllegalArgumentException("Maximum rental duration is 30 days.");
        }

        // Check overlap with existing reservations
        if (reservationDAO.hasOverlap(
                reservation.getEquipmentId(),
                reservation.getStartDate().toString(),
                reservation.getEndDate().toString(),
                "NONE")) {
            throw new IllegalArgumentException(
                "Equipment is already reserved for the selected dates.");
        }

        // Check overlap with existing rentals
        if (rentalDAO.hasOverlap(
                reservation.getEquipmentId(),
                reservation.getStartDate().toString(),
                reservation.getEndDate().toString(),
                "NONE")) {
            throw new IllegalArgumentException(
                "Equipment is already rented for the selected dates.");
        }

        // Check deposit limit
        if (!customerService.checkDepositLimit(
                reservation.getCustomerId(), depositAmount)) {
            throw new IllegalArgumentException(
                "Customer deposit limit (LKR 500,000) would be exceeded.");
        }

        reservation.setStatus("PENDING");
        reservationDAO.save(reservation);
    }

    public void cancelReservation(String reservationId) throws SQLException {
        Reservation reservation = reservationDAO.findById(reservationId);
        if (reservation == null) {
            throw new IllegalArgumentException("Reservation not found.");
        }
        if (reservation.getStatus().equals("CONVERTED")) {
            throw new IllegalArgumentException(
                "Cannot cancel a converted reservation.");
        }
        reservationDAO.updateStatus(reservationId, "CANCELLED");
    }
}