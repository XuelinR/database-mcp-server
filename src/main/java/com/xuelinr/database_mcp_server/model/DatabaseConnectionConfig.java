package com.xuelinr.database_mcp_server.model;

import lombok.Data;

@Data
public class DatabaseConnectionConfig {
    private String id;
    private String name;
    private String type; // 数据库类型 (MySQL, PostgreSQL, etc.)
    private String host;
    private int port;
    private String database;
    private String username;
    private String password;
    private String jdbcUrl;
}