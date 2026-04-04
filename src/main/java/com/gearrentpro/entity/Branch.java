package com.gearrentpro.entity;

public class Branch {
    private String branchId;
    private String branchName;
    private String address;
    private String contactPhone;
    private String contactEmail;
    private String managerId;

    public Branch() {}

    public Branch(String branchId, String branchName, String address,
                  String contactPhone, String contactEmail, String managerId) {
        this.branchId = branchId;
        this.branchName = branchName;
        this.address = address;
        this.contactPhone = contactPhone;
        this.contactEmail = contactEmail;
        this.managerId = managerId;
    }

    public String getBranchId() { return branchId; }
    public void setBranchId(String branchId) { this.branchId = branchId; }

    public String getBranchName() { return branchName; }
    public void setBranchName(String branchName) { this.branchName = branchName; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }

    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }

    public String getManagerId() { return managerId; }
    public void setManagerId(String managerId) { this.managerId = managerId; }

    @Override
    public String toString() { return branchName; }
}