package com.gearrentpro.entity;

import java.math.BigDecimal;

public class Membership {
    private String membershipId;
    private String levelName;
    private BigDecimal discountPercentage;
    private String description;

    public Membership() {}

    public Membership(String membershipId, String levelName,
                      BigDecimal discountPercentage, String description) {
        this.membershipId = membershipId;
        this.levelName = levelName;
        this.discountPercentage = discountPercentage;
        this.description = description;
    }

    public String getMembershipId() { return membershipId; }
    public void setMembershipId(String membershipId) { this.membershipId = membershipId; }

    public String getLevelName() { return levelName; }
    public void setLevelName(String levelName) { this.levelName = levelName; }

    public BigDecimal getDiscountPercentage() { return discountPercentage; }
    public void setDiscountPercentage(BigDecimal discountPercentage) { this.discountPercentage = discountPercentage; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() { return levelName; }
}