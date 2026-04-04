package com.gearrentpro.dao;

import com.gearrentpro.entity.SystemConfig;
import com.gearrentpro.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SystemConfigDAO {

    public String getValue(String key) throws SQLException {
        String sql = "SELECT config_value FROM system_config WHERE config_key = ?";
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, key);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getString("config_value");
        }
        return null;
    }

    public List<SystemConfig> findAll() throws SQLException {
        String sql = "SELECT * FROM system_config";
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        List<SystemConfig> list = new ArrayList<>();
        while (rs.next()) {
            list.add(mapRow(rs));
        }
        return list;
    }

    public boolean update(SystemConfig config) throws SQLException {
        String sql = "UPDATE system_config SET config_value = ? WHERE config_key = ?";
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, config.getConfigValue());
        stmt.setString(2, config.getConfigKey());
        return stmt.executeUpdate() > 0;
    }

    private SystemConfig mapRow(ResultSet rs) throws SQLException {
        SystemConfig config = new SystemConfig();
        config.setConfigKey(rs.getString("config_key"));
        config.setConfigValue(rs.getString("config_value"));
        config.setDescription(rs.getString("description"));
        return config;
    }
}