package com.gearrentpro.entity;

import java.math.BigDecimal;

public class Equipment {
    private String equipmentId;
    private String categoryId;
    private String branchId;
    private String brand;
    private String model;
    private String serialNumber;
    private int purchaseYear;
    private BigDecimal baseDailyPrice;
    private BigDecimal depositAmount;
    private String status;
    private String notes;

    public Equipment() {}

    public Equipment(String equipmentId, String categoryId, String branchId,
                     String brand, String model, String serialNumber,
                     int purchaseYear, BigDecimal baseDailyPrice,
                     BigDecimal depositAmount, String status, String notes) {
        this.equipmentId = equipmentId;
        this.categoryId = categoryId;
        this.branchId = branchId;
        this.brand = brand;
        this.model = model;
        this.serialNumber = serialNumber;
        this.purchaseYear = purchaseYear;
        this.baseDailyPrice = baseDailyPrice;
        this.depositAmount = depositAmount;
        this.status = status;
        this.notes = notes;
    }

    public String getEquipmentId() { return equipmentId; }
    public void setEquipmentId(String equipmentId) { this.equipmentId = equipmentId; }

    public String getCategoryId() { return categoryId; }
    public void setCategoryId(String categoryId) { this.categoryId = categoryId; }

    public String getBranchId() { return branchId; }
    public void setBranchId(String branchId) { this.branchId = branchId; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getSerialNumber() { return serialNumber; }
    public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }

    public int getPurchaseYear() { return purchaseYear; }
    public void setPurchaseYear(int purchaseYear) { this.purchaseYear = purchaseYear; }

    public BigDecimal getBaseDailyPrice() { return baseDailyPrice; }
    public void setBaseDailyPrice(BigDecimal baseDailyPrice) { this.baseDailyPrice = baseDailyPrice; }

    public BigDecimal getDepositAmount() { return depositAmount; }
    public void setDepositAmount(BigDecimal depositAmount) { this.depositAmount = depositAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    @Override
    public String toString() { return brand + " " + model + " (" + equipmentId + ")"; }
}