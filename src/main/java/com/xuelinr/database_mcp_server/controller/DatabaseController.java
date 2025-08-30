package com.xuelinr.database_mcp_server.controller;

import com.xuelinr.database_mcp_server.model.DatabaseConnectionConfig;
import com.xuelinr.database_mcp_server.model.DatabaseOperationResult;
import com.xuelinr.database_mcp_server.service.DatabaseOperationService;
import com.xuelinr.database_mcp_server.service.ConnectionManagerService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/database")
@CrossOrigin(origins = "*")
@Tag(name = "数据库操作接口", description = "提供数据库连接测试、SQL执行、连接配置管理等功能")
public class DatabaseController {

    private final DatabaseOperationService databaseOperationService;
    private final ConnectionManagerService connectionManagerService;

    public DatabaseController(DatabaseOperationService databaseOperationService, ConnectionManagerService connectionManagerService) {
        this.databaseOperationService = databaseOperationService;
        this.connectionManagerService = connectionManagerService;
    }

    @PostMapping("/test-connection")
    @Operation(summary = "测试数据库连接", description = "测试给定配置的数据库连接是否有效")
    @ApiResponse(responseCode = "200", description = "连接测试结果",
        content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = DatabaseOperationResult.class)))
    public DatabaseOperationResult testConnection(@RequestBody DatabaseConnectionConfig config) {
        return databaseOperationService.testConnection(config);
    }

    @PostMapping("/query")
    @Operation(summary = "执行查询SQL", description = "执行指定的查询SQL语句")
    @ApiResponse(responseCode = "200", description = "查询结果",
        content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = DatabaseOperationResult.class)))
    public DatabaseOperationResult executeQuery(@RequestBody DatabaseConnectionConfig config,
                                               @Parameter(description = "要执行的SQL语句") @RequestParam String sql) {
        return databaseOperationService.executeQuery(config, sql);
    }

    @PostMapping("/update")
    @Operation(summary = "执行更新SQL", description = "执行指定的更新SQL语句")
    @ApiResponse(responseCode = "200", description = "更新结果",
        content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = DatabaseOperationResult.class)))
    public DatabaseOperationResult executeUpdate(@RequestBody DatabaseConnectionConfig config,
                                                @Parameter(description = "要执行的SQL语句") @RequestParam String sql) {
        return databaseOperationService.executeUpdate(config, sql);
    }
    
    @PostMapping("/config")
    @Operation(summary = "保存连接配置", description = "保存数据库连接配置")
    public void saveConnectionConfig(@RequestBody DatabaseConnectionConfig config) {
        connectionManagerService.saveConnectionConfig(config);
    }
    
    @GetMapping("/config/{id}")
    @Operation(summary = "获取连接配置", description = "根据ID获取数据库连接配置")
    @ApiResponse(responseCode = "200", description = "连接配置信息",
        content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = DatabaseConnectionConfig.class)))
    public DatabaseConnectionConfig getConnectionConfig(@Parameter(description = "配置ID") @PathVariable String id) {
        return connectionManagerService.getConnectionConfig(id);
    }
    
    @GetMapping("/configs")
    @Operation(summary = "获取所有连接配置", description = "获取所有保存的数据库连接配置")
    @ApiResponse(responseCode = "200", description = "连接配置映射",
        content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = Map.class)))
    public Map<String, DatabaseConnectionConfig> getAllConnectionConfigs() {
        return connectionManagerService.getAllConnectionConfigs();
    }
    
    @DeleteMapping("/config/{id}")
    @Operation(summary = "删除连接配置", description = "根据ID删除数据库连接配置")
    public void removeConnectionConfig(@Parameter(description = "配置ID") @PathVariable String id) {
        connectionManagerService.removeConnectionConfig(id);
    }
}