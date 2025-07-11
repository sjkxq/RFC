# 客户端API文档

## 1. 快速开始
```java
// 1. 创建代理工厂
RpcProxyFactory factory = new RpcProxyFactory("zookeeper://127.0.0.1:2181");

// 2. 创建服务代理
UserService userService = factory.create(UserService.class);

// 3. 调用远程方法
User user = userService.getUserById(123);
```

## 2. 核心API
### RpcProxyFactory
| 方法 | 描述 |
|------|------|
| `create(Class<T> interfaceClass)` | 创建服务代理 |
| `create(Class<T> interfaceClass, String version)` | 创建指定版本的服务代理 |
| `setSerializer(String serializer)` | 设置序列化方式(json/protobuf) |
| `setLoadBalance(String loadBalance)` | 设置负载均衡策略(random/roundRobin) |

## 3. 注解配置
### @RpcReference
```java
public class UserController {
    @RpcReference(version = "1.0.0", timeout = 1000)
    private UserService userService;
}
```

| 参数 | 类型 | 默认值 | 描述 |
|------|------|--------|------|
| version | String | "" | 服务版本 |
| timeout | long | 3000 | 超时时间(ms) |
| retries | int | 3 | 重试次数 |
| fallback | Class | void.class | 降级处理类 |

## 4. 高级特性
### 异步调用
```java
RpcFuture<User> future = userService.asyncGetUserById(123);
future.thenAccept(user -> System.out.println(user));
```

### 泛化调用
```java
GenericService service = factory.createGenericService("com.example.UserService");
Object result = service.invoke("getUserById", new Object[]{123});
```