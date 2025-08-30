package com.xuelinr.database_mcp_server.service.impl;

import com.xuelinr.database_mcp_server.model.DatabaseConnectionConfig;
import com.xuelinr.database_mcp_server.model.DatabaseOperationResult;
import com.xuelinr.database_mcp_server.service.DatabaseOperationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class DatabaseOperationServiceImpl implements DatabaseOperationService {

    @Override
    public DatabaseOperationResult executeQuery(DatabaseConnectionConfig config, String sql) {
        DatabaseOperationResult result = new DatabaseOperationResult();
        long startTime = System.currentTimeMillis();

        try (Connection connection = DriverManager.getConnection(
                buildJdbcUrl(config), config.getUsername(), config.getPassword());
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            // 将结果集转换为List<Map>
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            List<Map<String, Object>> records = new ArrayList<>();

            while (resultSet.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(metaData.getColumnName(i), resultSet.getObject(i));
                }
                records.add(row);
            }

            result.setSuccess(true);
            result.setData(records);
        } catch (SQLException e) {
            log.error("执行查询失败: {}", e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        } finally {
            long endTime = System.currentTimeMillis();
            result.setExecutionTime(endTime - startTime);
        }

        return result;
    }

    @Override
    public DatabaseOperationResult executeUpdate(DatabaseConnectionConfig config, String sql) {
        DatabaseOperationResult result = new DatabaseOperationResult();
        long startTime = System.currentTimeMillis();

        try (Connection connection = DriverManager.getConnection(
                buildJdbcUrl(config), config.getUsername(), config.getPassword());
             Statement statement = connection.createStatement()) {

            int affectedRows = statement.executeUpdate(sql);
            result.setSuccess(true);
            result.setData("影响行数: " + affectedRows);
        } catch (SQLException e) {
            log.error("执行更新失败: {}", e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        } finally {
            long endTime = System.currentTimeMillis();
            result.setExecutionTime(endTime - startTime);
        }

        return result;
    }

    @Override
    public DatabaseOperationResult testConnection(DatabaseConnectionConfig config) {
        DatabaseOperationResult result = new DatabaseOperationResult();
        long startTime = System.currentTimeMillis();

        try (Connection connection = DriverManager.getConnection(
                buildJdbcUrl(config), config.getUsername(), config.getPassword())) {
            result.setSuccess(true);
            result.setMessage("连接成功");
        } catch (SQLException e) {
            log.error("测试连接失败: {}", e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        } finally {
            long endTime = System.currentTimeMillis();
            result.setExecutionTime(endTime - startTime);
        }

        return result;
    }

    private String buildJdbcUrl(DatabaseConnectionConfig config) {
        if (config.getJdbcUrl() != null && !config.getJdbcUrl().isEmpty()) {
            return config.getJdbcUrl();
        }

        return switch (config.getType().toLowerCase()) {
            case "mysql" -> "jdbc:mysql://" + config.getHost() + ":" + config.getPort() + "/" + config.getDatabase();
            case "postgresql" ->
                    "jdbc:postgresql://" + config.getHost() + ":" + config.getPort() + "/" + config.getDatabase();
            case "oracle" ->
                    "jdbc:oracle:thin:@" + config.getHost() + ":" + config.getPort() + ":" + config.getDatabase();
            default -> throw new IllegalArgumentException("不支持的数据库类型: " + config.getType());
        };
    }
}