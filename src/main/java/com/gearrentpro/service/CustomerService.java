package com.gearrentpro.service;

import com.gearrentpro.dao.CustomerDAO;
import com.gearrentpro.dao.SystemConfigDAO;
import com.gearrentpro.entity.Customer;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class CustomerService {

    private CustomerDAO customerDAO = new CustomerDAO();
    private SystemConfigDAO configDAO = new SystemConfigDAO();

    public List<Customer> getAllCustomers() throws SQLException {
        return customerDAO.findAll();
    }

    public Customer getCustomerById(String customerId) throws SQLException {
        return customerDAO.findById(customerId);
    }

    public void addCustomer(Customer customer) throws SQLException {
        if (customer.getCustomerId() == null || customer.getCustomerId().isEmpty()) {
            throw new IllegalArgumentException("Customer ID is required.");
        }
        if (customer.getFullName() == null || customer.getFullName().isEmpty()) {
            throw new IllegalArgumentException("Full name is required.");
        }
        if (customer.getNicPassport() == null || customer.getNicPassport().isEmpty()) {
            throw new IllegalArgumentException("NIC/Passport is required.");
        }
        if (customer.getPhone() == null || customer.getPhone().isEmpty()) {
            throw new IllegalArgumentException("Phone is required.");
        }
        if (customerDAO.findById(customer.getCustomerId()) != null) {
            throw new IllegalArgumentException("Customer ID already exists.");
        }
        if (customerDAO.findByNic(customer.getNicPassport()) != null) {
            throw new IllegalArgumentException("NIC/Passport already registered.");
        }
        if (customer.getTotalDepositHeld() == null) {
            customer.setTotalDepositHeld(BigDecimal.ZERO);
        }
        customer.setActive(true);
        customerDAO.save(customer);
    }

    public void updateCustomer(Customer customer) throws SQLException {
        if (customer.getFullName() == null || customer.getFullName().isEmpty()) {
            throw new IllegalArgumentException("Full name is required.");
        }
        if (customer.getPhone() == null || customer.getPhone().isEmpty()) {
            throw new IllegalArgumentException("Phone is required.");
        }
        customerDAO.update(customer);
    }

    public void deactivateCustomer(String customerId) throws SQLException {
        customerDAO.delete(customerId);
    }

    public boolean checkDepositLimit(String customerId, 
                                      BigDecimal newDeposit) throws SQLException {
        String limitStr = configDAO.getValue("MAX_DEPOSIT_PER_CUSTOMER");
        BigDecimal limit = new BigDecimal(limitStr);
        Customer customer = customerDAO.findById(customerId);
        BigDecimal currentDeposit = customer.getTotalDepositHeld();
        return currentDeposit.add(newDeposit).compareTo(limit) <= 0;
    }
}