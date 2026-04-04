package com.gearrentpro.service;

import com.gearrentpro.dao.EquipmentDAO;
import com.gearrentpro.entity.Equipment;

import java.sql.SQLException;
import java.util.List;

public class EquipmentService {

    private EquipmentDAO equipmentDAO = new EquipmentDAO();

    public List<Equipment> getAllEquipment() throws SQLException {
        return equipmentDAO.findAll();
    }

    public List<Equipment> getEquipmentByBranch(String branchId) throws SQLException {
        return equipmentDAO.findByBranch(branchId);
    }

    public List<Equipment> getAvailableEquipmentByBranch(String branchId) throws SQLException {
        return equipmentDAO.findAvailableByBranch(branchId);
    }

    public List<Equipment> getEquipmentByBranchAndCategory(String branchId, 
                                                            String categoryId) throws SQLException {
        return equipmentDAO.findByBranchAndCategory(branchId, categoryId);
    }

    public Equipment getEquipmentById(String equipmentId) throws SQLException {
        return equipmentDAO.findById(equipmentId);
    }

    public void addEquipment(Equipment equipment) throws SQLException {
        if (equipment.getEquipmentId() == null || equipment.getEquipmentId().isEmpty()) {
            throw new IllegalArgumentException("Equipment ID is required.");
        }
        if (equipment.getBrand() == null || equipment.getBrand().isEmpty()) {
            throw new IllegalArgumentException("Brand is required.");
        }
        if (equipment.getModel() == null || equipment.getModel().isEmpty()) {
            throw new IllegalArgumentException("Model is required.");
        }
        if (equipment.getBaseDailyPrice() == null) {
            throw new IllegalArgumentException("Base daily price is required.");
        }
        if (equipment.getDepositAmount() == null) {
            throw new IllegalArgumentException("Deposit amount is required.");
        }
        if (equipmentDAO.findById(equipment.getEquipmentId()) != null) {
            throw new IllegalArgumentException("Equipment ID already exists.");
        }
        if (equipment.getStatus() == null || equipment.getStatus().isEmpty()) {
            equipment.setStatus("AVAILABLE");
        }
        equipmentDAO.save(equipment);
    }

    public void updateEquipment(Equipment equipment) throws SQLException {
        if (equipment.getBrand() == null || equipment.getBrand().isEmpty()) {
            throw new IllegalArgumentException("Brand is required.");
        }
        if (equipment.getModel() == null || equipment.getModel().isEmpty()) {
            throw new IllegalArgumentException("Model is required.");
        }
        equipmentDAO.update(equipment);
    }

    public void updateStatus(String equipmentId, String status) throws SQLException {
        equipmentDAO.updateStatus(equipmentId, status);
    }

    public void deleteEquipment(String equipmentId) throws SQLException {
        equipmentDAO.delete(equipmentId);
    }
}