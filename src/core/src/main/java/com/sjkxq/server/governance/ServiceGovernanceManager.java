package com.sjkxq.server.governance;

import com.sjkxq.config.ServiceGovernanceConfig;
import com.sjkxq.client.circuitbreaker.CircuitBreakerProvider;
import com.sjkxq.server.ratelimit.provider.RateLimitProvider;

/**
 * 统一服务治理管理器
 */
public class ServiceGovernanceManager {
    private static volatile ServiceGovernanceManager instance;
    private ServiceGovernanceConfig config;

    private ServiceGovernanceManager() {}

    public static ServiceGovernanceManager getInstance() {
        if (instance == null) {
            synchronized (ServiceGovernanceManager.class) {
                if (instance == null) {
                    instance = new ServiceGovernanceManager();
                }
            }
        }
        return instance;
    }

    public void init(ServiceGovernanceConfig config) {
        this.config = config;
        // 初始化熔断器
        if (config.getCircuitBreaker() != null && config.getCircuitBreaker().isEnabled()) {
            CircuitBreakerProvider.init(config.getCircuitBreaker());
        }
        // 初始化限流器
        if (config.getRateLimit() != null && config.getRateLimit().isEnabled()) {
            RateLimitProvider.init(config.getRateLimit());
        }
    }

    public void updateConfig(ServiceGovernanceConfig newConfig) {
        this.config = newConfig;
        init(newConfig);
    }

    public ServiceGovernanceConfig getConfig() {
        return config;
    }
}