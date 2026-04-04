package com.gearrentpro.service;

import com.gearrentpro.dao.CategoryDAO;
import com.gearrentpro.entity.Category;

import java.sql.SQLException;
import java.util.List;

public class CategoryService {

    private CategoryDAO categoryDAO = new CategoryDAO();

    public List<Category> getAllCategories() throws SQLException {
        return categoryDAO.findAll();
    }

    public List<Category> getActiveCategories() throws SQLException {
        return categoryDAO.findAllActive();
    }

    public Category getCategoryById(String categoryId) throws SQLException {
        return categoryDAO.findById(categoryId);
    }

    public void addCategory(Category category) throws SQLException {
        if (category.getCategoryId() == null || category.getCategoryId().isEmpty()) {
            throw new IllegalArgumentException("Category ID is required.");
        }
        if (category.getCategoryName() == null || category.getCategoryName().isEmpty()) {
            throw new IllegalArgumentException("Category name is required.");
        }
        if (category.getBasePriceFactor() == null) {
            throw new IllegalArgumentException("Base price factor is required.");
        }
        if (category.getWeekendMultiplier() == null) {
            throw new IllegalArgumentException("Weekend multiplier is required.");
        }
        if (categoryDAO.findById(category.getCategoryId()) != null) {
            throw new IllegalArgumentException("Category ID already exists.");
        }
        categoryDAO.save(category);
    }

    public void updateCategory(Category category) throws SQLException {
        if (category.getCategoryName() == null || category.getCategoryName().isEmpty()) {
            throw new IllegalArgumentException("Category name is required.");
        }
        categoryDAO.update(category);
    }

    public void deactivateCategory(String categoryId) throws SQLException {
        categoryDAO.delete(categoryId);
    }
}