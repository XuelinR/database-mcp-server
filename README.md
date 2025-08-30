# Database MCP Server

Database MCP Server 是一个数据库管理服务，提供统一的数据库操作接口。

## 环境配置

本项目支持多种环境配置:
- 开发环境 (dev)
- 生产环境 (prod)

### 激活特定环境

可以通过以下方式激活特定环境:

1. 通过配置文件设置:
   ```yaml
   spring:
     profiles:
       active: prod
   ```

2. 通过命令行参数:
   ```bash
   java -jar database-mcp-server-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
   ```

3. 通过环境变量:
   ```bash
   export SPRING_PROFILES_ACTIVE=prod
   ```

### 环境差异

- **开发环境 (dev)**:
  - 启用详细日志记录
  - 更小的连接池
  - 启用调试选项
  - 默认使用H2内存数据库

- **生产环境 (prod)**:
  - 最小化日志记录以提高性能
  - 更大的连接池
  - 启用日志文件轮转
  - 关闭调试选项

## 数据库配置

本应用不依赖于固定的数据库连接，数据库连接信息通过API动态传入。

如果需要默认数据库连接，可以在对应环境配置文件中配置:
- MySQL
- PostgreSQL

配置示例请参考 `application-dev.yml` 和 `application-prod.yml` 文件中的注释。

## 构建和运行

### 使用Gradle运行

```bash
./gradlew bootRun
```

### 构建JAR文件

```bash
./gradlew build
```

### 运行JAR文件

```bash
java -jar build/libs/database-mcp-server-0.0.1-SNAPSHOT.jar
```

## API接口

- `POST /api/database/test-connection` - 测试数据库连接
- `POST /api/database/query?sql={SQL语句}` - 执行查询
- `POST /api/database/update?sql={SQL语句}` - 执行更新
- `POST /api/database/config` - 保存连接配置
- `GET /api/database/config/{id}` - 获取特定连接配置
- `GET /api/database/configs` - 获取所有连接配置
- `DELETE /api/database/config/{id}` - 删除连接配置

## 使用示例

### 测试数据库连接

```
POST /api/database/test-connection
Content-Type: application/json

{
  "name": "test_mysql",
  "type": "mysql",
  "host": "localhost",
  "port": 3306,
  "database": "test_db",
  "username": "root",
  "password": "password"
}
```

## 配置说明

项目配置文件位于 `src/main/resources/application.properties`：

- `server.port`: 服务端口，默认8080
- 日志级别配置
- 数据库连接池配置

## 许可证

详见 [LICENSE](LICENSE) 文件。