package com.sjkxq.config;

/**
 * 统一服务治理配置类
 */
public class ServiceGovernanceConfig {
    private CircuitBreakerConfig circuitBreaker = new CircuitBreakerConfig();
    private RateLimitConfig rateLimit = new RateLimitConfig();

    public CircuitBreakerConfig getCircuitBreaker() {
        return circuitBreaker;
    }

    public void setCircuitBreaker(CircuitBreakerConfig circuitBreaker) {
        this.circuitBreaker = circuitBreaker;
    }

    public RateLimitConfig getRateLimit() {
        return rateLimit;
    }

    public void setRateLimit(RateLimitConfig rateLimit) {
        this.rateLimit = rateLimit;
    }
}