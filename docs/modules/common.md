# Common 模块功能实现方案

## 1. 模块概述
common模块提供项目公共组件，包括：
- 序列化/反序列化
- 消息协议
- SPI机制
- 工具类

## 2. 核心功能实现

### 2.1 序列化组件
支持多种序列化方式：
- JSON
- Hessian
- Kryo
- Protostuff

序列化接口：
```java
public interface Serializer {
    <T> byte[] serialize(T obj);
    <T> T deserialize(byte[] bytes, Class<T> clazz);
}
```

### 2.2 消息协议
定义RPC通信协议：
- `RpcRequest`: 请求消息体
- `RpcResponse`: 响应消息体
- `MessageType`: 消息类型枚举

### 2.3 SPI机制
基于Java SPI扩展：
- `SpiLoader`: SPI加载器
- 支持按需加载实现类
- 支持优先级排序

### 2.4 工具类
- `ConfigUtil`: 配置工具
- `TraceContext`: 链路追踪上下文

## 3. 关键类说明
- `Serializer`: 序列化接口
- `RpcRequest/RpcResponse`: 消息协议
- `SpiLoader`: SPI加载器