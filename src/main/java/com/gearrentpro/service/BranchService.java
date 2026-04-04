package com.gearrentpro.service;

import com.gearrentpro.dao.BranchDAO;
import com.gearrentpro.entity.Branch;

import java.sql.SQLException;
import java.util.List;

public class BranchService {

    private BranchDAO branchDAO = new BranchDAO();

    public List<Branch> getAllBranches() throws SQLException {
        return branchDAO.findAll();
    }

    public Branch getBranchById(String branchId) throws SQLException {
        return branchDAO.findById(branchId);
    }

    public void addBranch(Branch branch) throws SQLException {
        if (branch.getBranchId() == null || branch.getBranchId().isEmpty()) {
            throw new IllegalArgumentException("Branch ID is required.");
        }
        if (branch.getBranchName() == null || branch.getBranchName().isEmpty()) {
            throw new IllegalArgumentException("Branch name is required.");
        }
        if (branch.getAddress() == null || branch.getAddress().isEmpty()) {
            throw new IllegalArgumentException("Address is required.");
        }
        if (branchDAO.findById(branch.getBranchId()) != null) {
            throw new IllegalArgumentException("Branch ID already exists.");
        }
        branchDAO.save(branch);
    }

    public void updateBranch(Branch branch) throws SQLException {
        if (branch.getBranchName() == null || branch.getBranchName().isEmpty()) {
            throw new IllegalArgumentException("Branch name is required.");
        }
        if (branch.getAddress() == null || branch.getAddress().isEmpty()) {
            throw new IllegalArgumentException("Address is required.");
        }
        branchDAO.update(branch);
    }

    public void deleteBranch(String branchId) throws SQLException {
        branchDAO.delete(branchId);
    }
}