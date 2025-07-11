package com.sjkxq.client.circuitbreaker;

import com.sjkxq.config.CircuitBreakerConfig;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName CircuitBreakerState
 * @Description 提供熔断器
 */
@Slf4j
public class CircuitBreakerProvider {
    // 使用线程安全的 ConcurrentHashMap
    private static final Map<String, CircuitBreaker> circuitBreakerMap = new ConcurrentHashMap<>();
    private static CircuitBreakerConfig config;

    public static void init(CircuitBreakerConfig config) {
        CircuitBreakerProvider.config = config;
        log.info("熔断器配置初始化完成: {}", config);
    }

    public static synchronized CircuitBreaker getCircuitBreaker(String serviceName) {
        // 使用 computeIfAbsent，避免手动同步
        return circuitBreakerMap.computeIfAbsent(serviceName, key -> {
            log.info("服务 [{}] 不存在熔断器，创建新的熔断器实例", serviceName);
            // 使用配置参数创建熔断器
            return new CircuitBreaker(
                config.getFailureThreshold(),
                config.getFailureRateThreshold(),
                config.getTimeout()
            );
        });
    }
}