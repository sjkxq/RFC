# RPC-Java - 高性能Java RPC框架

## 项目简介
RPC-Java是一个基于Netty实现的高性能Java RPC框架，专为微服务架构设计。它提供了服务注册发现、负载均衡、容错重试等分布式系统核心功能，帮助开发者快速构建高性能的分布式应用。

**设计目标**：
- 简单易用的API
- 高性能、低延迟
- 高可扩展性
- 生产环境就绪

## 主要特性
- 基于Netty的高性能网络通信
- 支持ZooKeeper服务注册与发现
- 多种负载均衡策略（随机、轮询、一致性哈希）
- 支持同步调用
- 可扩展的序列化协议（JSON/Protobuf/Hessian）
- 服务治理功能（熔断、限流）

## 文档
完整文档请参考[文档中心](docs/README.md)，包含：
- [架构设计](docs/design/architecture.md)
- [API文档](docs/api/client-api.md)
- [部署指南](docs/guides/deployment.md)
- [性能基准](docs/reference/benchmarks.md)
- [FAQ](docs/misc/FAQ.md)