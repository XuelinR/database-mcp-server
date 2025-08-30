package com.xuelinr.database_mcp_server.service;

import com.xuelinr.database_mcp_server.model.DatabaseConnectionConfig;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@Slf4j
@Service
public class ConnectionManagerService {
    
    private final Map<String, DatabaseConnectionConfig> connectionConfigMap = new ConcurrentHashMap<>();
    
    @PostConstruct
    public void init() {
        log.info("初始化数据库连接管理服务");
    }
    
    public void saveConnectionConfig(DatabaseConnectionConfig config) {
        if (config.getId() == null || config.getId().isEmpty()) {
            config.setId(java.util.UUID.randomUUID().toString());
        }
        connectionConfigMap.put(config.getId(), config);
        log.info("保存数据库连接配置: {}", config.getName());
    }
    
    public DatabaseConnectionConfig getConnectionConfig(String id) {
        return connectionConfigMap.get(id);
    }
    
    public Map<String, DatabaseConnectionConfig> getAllConnectionConfigs() {
        return new ConcurrentHashMap<>(connectionConfigMap);
    }
    
    public void removeConnectionConfig(String id) {
        DatabaseConnectionConfig removed = connectionConfigMap.remove(id);
        if (removed != null) {
            log.info("移除数据库连接配置: {}", removed.getName());
        }
    }
}