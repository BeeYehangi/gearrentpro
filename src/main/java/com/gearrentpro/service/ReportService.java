package com.gearrentpro.service;

import com.gearrentpro.dao.RentalDAO;
import com.gearrentpro.dao.EquipmentDAO;
import com.gearrentpro.entity.Rental;
import com.gearrentpro.entity.Equipment;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class ReportService {

    private RentalDAO rentalDAO = new RentalDAO();
    private EquipmentDAO equipmentDAO = new EquipmentDAO();

    // Branch-wise revenue report
    public Map<String, Object> getBranchRevenueReport(String branchId,
                                                       LocalDate from,
                                                       LocalDate to) throws SQLException {
        List<Rental> rentals = rentalDAO.findByBranch(branchId);
        Map<String, Object> report = new LinkedHashMap<>();

        BigDecimal totalRentalIncome = BigDecimal.ZERO;
        BigDecimal totalLateFees = BigDecimal.ZERO;
        BigDecimal totalDamageCharges = BigDecimal.ZERO;
        int rentalCount = 0;

        for (Rental rental : rentals) {
            if (!rental.getStartDate().isBefore(from) && 
                !rental.getStartDate().isAfter(to)) {
                totalRentalIncome = totalRentalIncome.add(
                    rental.getFinalPayableAmount());
                totalLateFees = totalLateFees.add(rental.getLateFee());
                totalDamageCharges = totalDamageCharges.add(
                    rental.getDamageCharges());
                rentalCount++;
            }
        }

        report.put("branchId", branchId);
        report.put("fromDate", from);
        report.put("toDate", to);
        report.put("totalRentalIncome", totalRentalIncome);
        report.put("totalLateFees", totalLateFees);
        report.put("totalDamageCharges", totalDamageCharges);
        report.put("totalRentals", rentalCount);
        report.put("grandTotal", totalRentalIncome
            .add(totalLateFees).add(totalDamageCharges));

        return report;
    }

    // Equipment utilization report
    public List<Map<String, Object>> getEquipmentUtilizationReport(
            String branchId, LocalDate from, LocalDate to) throws SQLException {

        List<Equipment> equipmentList = equipmentDAO.findByBranch(branchId);
        List<Rental> rentals = rentalDAO.findByBranch(branchId);
        long totalDays = ChronoUnit.DAYS.between(from, to) + 1;

        List<Map<String, Object>> report = new ArrayList<>();

        for (Equipment equipment : equipmentList) {
            long rentedDays = 0;

            for (Rental rental : rentals) {
                if (rental.getEquipmentId().equals(equipment.getEquipmentId())
                        && !rental.getRentalStatus().equals("CANCELLED")) {
                    LocalDate rStart = rental.getStartDate().isBefore(from) ? 
                        from : rental.getStartDate();
                    LocalDate rEnd = rental.getEndDate().isAfter(to) ? 
                        to : rental.getEndDate();
                    if (!rStart.isAfter(rEnd)) {
                        rentedDays += ChronoUnit.DAYS.between(rStart, rEnd) + 1;
                    }
                }
            }

            double utilization = totalDays > 0 ? 
                (rentedDays * 100.0 / totalDays) : 0;

            Map<String, Object> row = new LinkedHashMap<>();
            row.put("equipmentId", equipment.getEquipmentId());
            row.put("brand", equipment.getBrand());
            row.put("model", equipment.getModel());
            row.put("rentedDays", rentedDays);
            row.put("availableDays", totalDays - rentedDays);
            row.put("utilizationPct", String.format("%.1f%%", utilization));
            report.add(row);
        }
        return report;
    }
}