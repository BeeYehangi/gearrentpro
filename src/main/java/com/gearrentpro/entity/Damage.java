package com.gearrentpro.entity;

import java.math.BigDecimal;

public class Damage {
    private int damageId;
    private String rentalId;
    private String equipmentId;
    private String description;
    private BigDecimal chargeAmount;
    private String reportedBy;

    public Damage() {}

    public Damage(int damageId, String rentalId, String equipmentId,
                  String description, BigDecimal chargeAmount, String reportedBy) {
        this.damageId = damageId;
        this.rentalId = rentalId;
        this.equipmentId = equipmentId;
        this.description = description;
        this.chargeAmount = chargeAmount;
        this.reportedBy = reportedBy;
    }

    public int getDamageId() { return damageId; }
    public void setDamageId(int damageId) { this.damageId = damageId; }

    public String getRentalId() { return rentalId; }
    public void setRentalId(String rentalId) { this.rentalId = rentalId; }

    public String getEquipmentId() { return equipmentId; }
    public void setEquipmentId(String equipmentId) { this.equipmentId = equipmentId; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getChargeAmount() { return chargeAmount; }
    public void setChargeAmount(BigDecimal chargeAmount) { this.chargeAmount = chargeAmount; }

    public String getReportedBy() { return reportedBy; }
    public void setReportedBy(String reportedBy) { this.reportedBy = reportedBy; }

    @Override
    public String toString() { return "Damage #" + damageId + " - " + description; }
}