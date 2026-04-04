package com.gearrentpro.service;

import com.gearrentpro.dao.SystemConfigDAO;
import com.gearrentpro.entity.SystemConfig;

import java.sql.SQLException;
import java.util.List;

public class SystemConfigService {

    private SystemConfigDAO configDAO = new SystemConfigDAO();

    public List<SystemConfig> getAllConfigs() throws SQLException {
        return configDAO.findAll();
    }

    public String getValue(String key) throws SQLException {
        return configDAO.getValue(key);
    }

    public void updateConfig(SystemConfig config) throws SQLException {
        if (config.getConfigKey() == null || config.getConfigKey().isEmpty()) {
            throw new IllegalArgumentException("Config key is required.");
        }
        if (config.getConfigValue() == null || config.getConfigValue().isEmpty()) {
            throw new IllegalArgumentException("Config value is required.");
        }
        configDAO.update(config);
    }
}