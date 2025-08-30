package com.xuelinr.database_mcp_server.model;

import lombok.Data;

@Data
public class DatabaseOperationResult {
    private boolean success;
    private String message;
    private Object data;
    private long executionTime;
}