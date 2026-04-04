package com.gearrentpro.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Customer {
    private String customerId;
    private String nicPassport;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String membershipId;
    private BigDecimal totalDepositHeld;
    private boolean isActive;
    private LocalDateTime createdDate;

    public Customer() {}

    public Customer(String customerId, String nicPassport, String fullName,
                    String email, String phone, String address,
                    String membershipId, BigDecimal totalDepositHeld,
                    boolean isActive, LocalDateTime createdDate) {
        this.customerId = customerId;
        this.nicPassport = nicPassport;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.membershipId = membershipId;
        this.totalDepositHeld = totalDepositHeld;
        this.isActive = isActive;
        this.createdDate = createdDate;
    }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getNicPassport() { return nicPassport; }
    public void setNicPassport(String nicPassport) { this.nicPassport = nicPassport; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getMembershipId() { return membershipId; }
    public void setMembershipId(String membershipId) { this.membershipId = membershipId; }

    public BigDecimal getTotalDepositHeld() { return totalDepositHeld; }
    public void setTotalDepositHeld(BigDecimal totalDepositHeld) { this.totalDepositHeld = totalDepositHeld; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }

    @Override
    public String toString() { return fullName + " (" + customerId + ")"; }
}