package com.gearrentpro.service;

import com.gearrentpro.dao.*;
import com.gearrentpro.entity.*;
import com.gearrentpro.util.DBConnection;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class RentalService {

    private RentalDAO rentalDAO = new RentalDAO();
    private ReservationDAO reservationDAO = new ReservationDAO();
    private EquipmentDAO equipmentDAO = new EquipmentDAO();
    private CustomerDAO customerDAO = new CustomerDAO();
    private CategoryDAO categoryDAO = new CategoryDAO();
    private SystemConfigDAO configDAO = new SystemConfigDAO();
    private DamageDAO damageDAO = new DamageDAO();

    public List<Rental> getAllRentals() throws SQLException {
        return rentalDAO.findAll();
    }

    public List<Rental> getRentalsByBranch(String branchId) throws SQLException {
        return rentalDAO.findByBranch(branchId);
    }

    public List<Rental> getRentalsByCustomer(String customerId) throws SQLException {
        return rentalDAO.findByCustomer(customerId);
    }

    public List<Rental> getOverdueRentals() throws SQLException {
        return rentalDAO.findOverdue();
    }

    public Rental getRentalById(String rentalId) throws SQLException {
        return rentalDAO.findById(rentalId);
    }

    // Calculate rental amount considering weekends and category factor
    public BigDecimal calculateRentalAmount(String equipmentId,
                                             LocalDate startDate,
                                             LocalDate endDate) throws SQLException {
        Equipment equipment = equipmentDAO.findById(equipmentId);
        Category category = categoryDAO.findById(equipment.getCategoryId());

        BigDecimal total = BigDecimal.ZERO;
        LocalDate current = startDate;

        while (!current.isAfter(endDate)) {
            BigDecimal dailyPrice = equipment.getBaseDailyPrice()
                    .multiply(category.getBasePriceFactor());

            DayOfWeek day = current.getDayOfWeek();
            if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) {
                dailyPrice = dailyPrice.multiply(category.getWeekendMultiplier());
            }
            total = total.add(dailyPrice);
            current = current.plusDays(1);
        }
        return total.setScale(2, RoundingMode.HALF_UP);
    }

    // Calculate membership discount
    public BigDecimal calculateMembershipDiscount(String customerId,
                                                   BigDecimal rentalAmount) throws SQLException {
        Customer customer = customerDAO.findById(customerId);
        if (customer.getMembershipId() == null || 
            customer.getMembershipId().equals("REG")) {
            return BigDecimal.ZERO;
        }
        MembershipDAO membershipDAO = new MembershipDAO();
        Membership membership = membershipDAO.findById(customer.getMembershipId());
        if (membership == null) return BigDecimal.ZERO;
        return rentalAmount.multiply(membership.getDiscountPercentage())
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }

    // Calculate long rental discount
    public BigDecimal calculateLongRentalDiscount(LocalDate startDate,
                                                   LocalDate endDate,
                                                   BigDecimal rentalAmount) throws SQLException {
        long days = ChronoUnit.DAYS.between(startDate, endDate);
        int minDays = Integer.parseInt(configDAO.getValue("LONG_RENTAL_DAYS"));
        if (days >= minDays) {
            BigDecimal discountPct = new BigDecimal(
                configDAO.getValue("LONG_RENTAL_DISCOUNT"));
            return rentalAmount.multiply(discountPct)
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO;
    }

    // Create rental with transaction
    public void createRental(Rental rental) throws SQLException {
        // Validate dates
        if (rental.getStartDate() == null || rental.getEndDate() == null) {
            throw new IllegalArgumentException("Start and end dates are required.");
        }
        if (rental.getEndDate().isBefore(rental.getStartDate())) {
            throw new IllegalArgumentException(
                "End date cannot be before start date.");
        }
        long days = ChronoUnit.DAYS.between(
            rental.getStartDate(), rental.getEndDate());
        if (days > 30) {
            throw new IllegalArgumentException(
                "Maximum rental duration is 30 days.");
        }

        // Check overlap
        if (rentalDAO.hasOverlap(
                rental.getEquipmentId(),
                rental.getStartDate().toString(),
                rental.getEndDate().toString(),
                "NONE")) {
            throw new IllegalArgumentException(
                "Equipment is already rented for the selected dates.");
        }

        // Check deposit limit
        BigDecimal deposit = rental.getSecurityDeposit();
        Customer customer = customerDAO.findById(rental.getCustomerId());
        String limitStr = configDAO.getValue("MAX_DEPOSIT_PER_CUSTOMER");
        BigDecimal limit = new BigDecimal(limitStr);
        if (customer.getTotalDepositHeld().add(deposit).compareTo(limit) > 0) {
            throw new IllegalArgumentException(
                "Customer deposit limit (LKR 500,000) would be exceeded.");
        }

        // Use transaction
        Connection conn = DBConnection.getInstance().getConnection();
        try {
            conn.setAutoCommit(false);

            // Save rental
            rentalDAO.save(rental);

            // Update equipment status to RENTED
            equipmentDAO.updateStatus(rental.getEquipmentId(), "RENTED");

            // Update customer deposit held
            BigDecimal newDeposit = customer.getTotalDepositHeld().add(deposit);
            customerDAO.updateDepositHeld(rental.getCustomerId(), newDeposit);

            // If converted from reservation, mark reservation as CONVERTED
            if (rental.getReservationId() != null) {
                reservationDAO.updateStatus(
                    rental.getReservationId(), "CONVERTED");
            }

            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    // Process return with transaction
    public void processReturn(Rental rental, Damage damage) throws SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        try {
            conn.setAutoCommit(false);

            // Calculate late fee
            BigDecimal lateFee = BigDecimal.ZERO;
            if (rental.getActualReturnDate().isAfter(rental.getEndDate())) {
                long daysLate = ChronoUnit.DAYS.between(
                    rental.getEndDate(), rental.getActualReturnDate());
                Equipment equipment = equipmentDAO.findById(rental.getEquipmentId());
                Category category = categoryDAO.findById(equipment.getCategoryId());
                lateFee = category.getLateFeePerDay()
                        .multiply(BigDecimal.valueOf(daysLate));
            }

            BigDecimal damageCharge = damage != null ? 
                damage.getChargeAmount() : BigDecimal.ZERO;
            BigDecimal totalCharges = lateFee.add(damageCharge);
            BigDecimal deposit = rental.getSecurityDeposit();

            rental.setLateFee(lateFee);
            rental.setDamageCharges(damageCharge);
            rental.setRentalStatus("RETURNED");
            rental.setPaymentStatus(
                totalCharges.compareTo(deposit) <= 0 ? "PAID" : "UNPAID");

            // Update rental
            rentalDAO.update(rental);

            // Save damage record if any
            if (damage != null && damageCharge.compareTo(BigDecimal.ZERO) > 0) {
                damageDAO.save(damage);
                equipmentDAO.updateStatus(
                    rental.getEquipmentId(), "UNDER_MAINTENANCE");
            } else {
                equipmentDAO.updateStatus(rental.getEquipmentId(), "AVAILABLE");
            }

            // Update customer deposit held
            Customer customer = customerDAO.findById(rental.getCustomerId());
            BigDecimal newDeposit = customer.getTotalDepositHeld()
                    .subtract(deposit);
            if (newDeposit.compareTo(BigDecimal.ZERO) < 0) {
                newDeposit = BigDecimal.ZERO;
            }
            customerDAO.updateDepositHeld(rental.getCustomerId(), newDeposit);

            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }
}