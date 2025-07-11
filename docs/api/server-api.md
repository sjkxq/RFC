# 服务端API文档

## 1. 服务定义与实现
### 定义服务接口
```java
public interface UserService {
    User getUserById(long id);
}
```

### 实现服务
```java
@RpcService(interfaceClass = UserService.class, version = "1.0.0")
public class UserServiceImpl implements UserService {
    @Override
    public User getUserById(long id) {
        return userRepository.findById(id);
    }
}
```

## 2. 服务注册配置
### 注解方式
```java
@Configuration
@RpcComponentScan(basePackages = "com.example.service")
public class RpcServerConfig {
    @Bean
    public RpcServer rpcServer() {
        return new RpcServer(8080, "zookeeper://127.0.0.1:2181");
    }
}
```

### 编程方式
```java
RpcServer server = new RpcServer(8080, "zookeeper://127.0.0.1:2181");
server.registerService(UserService.class, new UserServiceImpl());
server.start();
```

## 3. 服务端注解
### @RpcService
```java
@RpcService(
    interfaceClass = UserService.class,
    version = "1.0.0",
    weight = 100,
    group = "production"
)
```

| 参数 | 类型 | 默认值 | 描述 |
|------|------|--------|------|
| interfaceClass | Class | - | 服务接口 |
| version | String | "" | 服务版本 |
| weight | int | 100 | 服务权重 |
| group | String | "" | 服务分组 |

## 4. 过滤器与拦截器
### 自定义过滤器
```java
public class AuthFilter implements RpcFilter {
    @Override
    public boolean filter(RpcRequest request) {
        // 验证token等逻辑
        return isValid(request.getToken());
    }
}
```

### 注册过滤器
```java
server.addFilter(new AuthFilter());
```

## 5. 高级配置
### 线程池配置
```properties
# 核心线程数
rpc.server.threadPool.coreSize=20
# 最大线程数
rpc.server.threadPool.maxSize=100
# 队列容量
rpc.server.threadPool.queueCapacity=1000
```

### 序列化配置
```java
server.setSerializer(new ProtobufSerializer());
```