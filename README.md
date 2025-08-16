# RFC - 高性能Java RPC框架

## 项目简介

RFC是一个基于Netty实现的高性能Java RPC框架，专为微服务架构设计。该框架提供了服务注册发现、负载均衡、容错重试等核心功能，帮助开发者快速构建分布式应用。

## 核心特性

- **高性能网络通信**：基于Netty 4.1.51.Final实现高并发、低延迟的网络通信
- **多种序列化协议**：支持JSON、Hessian、Kryo、Protostuff等多种序列化方式
- **服务注册与发现**：集成ZooKeeper实现服务注册与发现机制
- **负载均衡策略**：提供随机、轮询、一致性哈希等多种负载均衡算法
- **服务治理能力**：
  - 熔断机制：防止故障扩散，提高系统稳定性
  - 限流控制：保护系统不被流量冲垮
- **容错重试机制**：支持方法级别的重试注解配置
- **分布式链路追踪**：集成Zipkin实现调用链监控

## 技术栈

- **核心语言**：Java 17
- **构建工具**：Maven 3.x
- **网络通信**：Netty 4.1.51.Final
- **序列化**：FastJSON、Hessian、Kryo、Protostuff
- **服务注册**：ZooKeeper 5.1.0
- **分布式追踪**：Zipkin 3.4.0
- **其他依赖**：
  - Spring Boot 3.3.5
  - Lombok 1.18.30
  - Guava 33.2.1-jre

## 项目架构

项目采用模块化设计，包含以下核心模块：

```
rfc
├── api          # 定义RPC接口和数据传输对象
├── common       # 通用组件，包括异常处理、消息格式、序列化器等
├── core         # 核心逻辑实现，包括客户端、服务端、配置管理等
├── consumer     # 客户端测试模块
└── provider     # 服务端实现及测试模块
```

### 模块详解

1. **api模块**：定义RPC接口和数据传输对象（DTO）
   - [UserService](src/api/src/main/java/com/sjkxq/service/UserService.java)：用户服务接口
   - [User](src/api/src/main/java/com/sjkxq/pojo/User.java)：用户实体类

2. **common模块**：包含通用工具和组件
   - 异常处理机制
   - 消息格式定义（请求/响应）
   - 多种序列化器实现
   - SPI加载器

3. **core模块**：RPC框架核心实现
   - 客户端代理和服务发现
   - 服务端处理逻辑
   - 网络通信（Netty实现）
   - 配置管理
   - 服务治理（熔断、限流）

4. **consumer模块**：客户端使用示例
   - [ConsumerTest](src/consumer/src/test/java/com/sjkxq/consumer/ConsumerTest.java)：客户端测试类

5. **provider模块**：服务端实现示例
   - [UserServiceImpl](src/provider/src/main/java/com/sjkxq/provider/impl/UserServiceImpl.java)：用户服务实现
   - [ProviderTest](src/provider/src/test/java/com/sjkxq/provider/ProviderTest.java)：服务端测试类

## 快速开始

### 环境要求

- JDK 17 或更高版本
- Maven 3.x
- ZooKeeper（用于服务注册发现）

### 构建项目

```bash
# 克隆项目
git clone <repository-url>
cd RFC

# 编译和安装所有模块
mvn clean install
```

### 运行示例

1. 启动ZooKeeper服务

2. 启动服务提供者：
   ```bash
   cd src/provider
   mvn exec:java -Dexec.mainClass="com.sjkxq.provider.ProviderTest"
   ```

3. 启动服务消费者：
   ```bash
   cd src/consumer
   mvn exec:java -Dexec.mainClass="com.sjkxq.consumer.ConsumerTest"
   ```

## 核心设计

### 架构模式

- **客户端-服务端模式**：RPC调用基于客户端-服务端模型
- **服务注册与发现模式**：基于ZooKeeper实现动态服务注册与发现
- **责任链模式**：用于处理RPC请求和响应流程
- **工厂模式**：用于创建序列化器、负载均衡器等组件
- **代理模式**：客户端通过代理发起远程调用

### 服务治理

1. **熔断机制**：
   - 默认失败阈值：3次
   - 熔断重置时间：5000毫秒
   - 失败率阈值：50%

2. **限流机制**：
   - 默认每秒允许请求数：100
   - 预热期：10秒

### 序列化支持

框架支持多种序列化方式，默认使用Protostuff：
- JSON（FastJSON）
- Hessian
- Kryo
- Protostuff

## 配置说明

框架支持通过配置文件自定义行为，默认配置如下：

```java
RpcConfig config = RpcConfig.builder()
    .name("RPC")                    // 框架名称
    .port(9999)                     // 服务端口
    .host("localhost")              // 主机地址
    .version("1.0.0")               // 版本号
    .registry("ZKServiceRegister")  // 注册中心
    .serializer("Protostuff")       // 序列化器
    .loadBalance("ConsistencyHash") // 负载均衡策略
    .build();
```

## 使用示例

### 定义服务接口（api模块）

```java
public interface UserService {
    @Retryable
    User getUserByUserId(Integer id);
    
    @Retryable
    Integer insertUserId(User user);
}
```

### 实现服务接口（provider模块）

```java
public class UserServiceImpl implements UserService {
    @Override
    public User getUserByUserId(Integer id) {
        // 实现业务逻辑
        return user;
    }

    @Override
    public Integer insertUserId(User user) {
        // 实现业务逻辑
        return userId;
    }
}
```

### 客户端调用（consumer模块）

```java
// 创建客户端代理
ClientProxy clientProxy = new ClientProxy();
UserService proxy = clientProxy.getProxy(UserService.class);

// 调用远程方法
User user = proxy.getUserByUserId(1);
```

## 扩展性设计

1. **SPI机制**：通过SPI机制支持序列化器、负载均衡等组件的动态扩展
2. **插件化设计**：各核心组件均可替换和扩展
3. **配置驱动**：通过配置文件灵活调整框架行为

## 性能优化

1. **Netty高性能网络通信**：基于NIO的异步通信模型
2. **对象池化**：减少对象创建和GC压力
3. **连接复用**：客户端连接池管理
4. **零拷贝技术**：减少数据传输过程中的拷贝操作