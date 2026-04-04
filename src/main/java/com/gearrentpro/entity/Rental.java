package com.gearrentpro.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Rental {
    private String rentalId;
    private String reservationId;
    private String equipmentId;
    private String customerId;
    private String branchId;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate actualReturnDate;
    private BigDecimal calculatedRentalAmount;
    private BigDecimal securityDeposit;
    private BigDecimal membershipDiscount;
    private BigDecimal longRentalDiscount;
    private BigDecimal finalPayableAmount;
    private BigDecimal lateFee;
    private BigDecimal damageCharges;
    private String paymentStatus;
    private String rentalStatus;
    private String createdBy;
    private String returnedBy;

    public Rental() {}

    public String getRentalId() { return rentalId; }
    public void setRentalId(String rentalId) { this.rentalId = rentalId; }

    public String getReservationId() { return reservationId; }
    public void setReservationId(String reservationId) { this.reservationId = reservationId; }

    public String getEquipmentId() { return equipmentId; }
    public void setEquipmentId(String equipmentId) { this.equipmentId = equipmentId; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getBranchId() { return branchId; }
    public void setBranchId(String branchId) { this.branchId = branchId; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public LocalDate getActualReturnDate() { return actualReturnDate; }
    public void setActualReturnDate(LocalDate actualReturnDate) { this.actualReturnDate = actualReturnDate; }

    public BigDecimal getCalculatedRentalAmount() { return calculatedRentalAmount; }
    public void setCalculatedRentalAmount(BigDecimal calculatedRentalAmount) { this.calculatedRentalAmount = calculatedRentalAmount; }

    public BigDecimal getSecurityDeposit() { return securityDeposit; }
    public void setSecurityDeposit(BigDecimal securityDeposit) { this.securityDeposit = securityDeposit; }

    public BigDecimal getMembershipDiscount() { return membershipDiscount; }
    public void setMembershipDiscount(BigDecimal membershipDiscount) { this.membershipDiscount = membershipDiscount; }

    public BigDecimal getLongRentalDiscount() { return longRentalDiscount; }
    public void setLongRentalDiscount(BigDecimal longRentalDiscount) { this.longRentalDiscount = longRentalDiscount; }

    public BigDecimal getFinalPayableAmount() { return finalPayableAmount; }
    public void setFinalPayableAmount(BigDecimal finalPayableAmount) { this.finalPayableAmount = finalPayableAmount; }

    public BigDecimal getLateFee() { return lateFee; }
    public void setLateFee(BigDecimal lateFee) { this.lateFee = lateFee; }

    public BigDecimal getDamageCharges() { return damageCharges; }
    public void setDamageCharges(BigDecimal damageCharges) { this.damageCharges = damageCharges; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public String getRentalStatus() { return rentalStatus; }
    public void setRentalStatus(String rentalStatus) { this.rentalStatus = rentalStatus; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public String getReturnedBy() { return returnedBy; }
    public void setReturnedBy(String returnedBy) { this.returnedBy = returnedBy; }

    @Override
    public String toString() { return rentalId; }
}