package com.xuelinr.database_mcp_server.service;

import com.xuelinr.database_mcp_server.model.DatabaseConnectionConfig;
import com.xuelinr.database_mcp_server.model.DatabaseOperationResult;

public interface DatabaseOperationService {
    DatabaseOperationResult executeQuery(DatabaseConnectionConfig config, String sql);
    DatabaseOperationResult executeUpdate(DatabaseConnectionConfig config, String sql);
    DatabaseOperationResult testConnection(DatabaseConnectionConfig config);
}