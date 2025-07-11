package com.sjkxq.config;

/**
 * 熔断器配置类
 */
public class CircuitBreakerConfig {
    private boolean enabled = true;
    private int failureThreshold = 3;
    private long resetTimeout = 5000; // 毫秒

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getFailureThreshold() {
        return failureThreshold;
    }

    public void setFailureThreshold(int failureThreshold) {
        this.failureThreshold = failureThreshold;
    }

    public long getResetTimeout() {
        return resetTimeout;
    }

    public void setResetTimeout(long resetTimeout) {
        this.resetTimeout = resetTimeout;
    }

    /**
     * 获取失败率阈值(0-1之间的小数)
     * @return 失败率阈值
     */
    public double getFailureRateThreshold() {
        return 0.5; // 默认50%失败率
    }

    /**
     * 获取熔断超时时间(毫秒)
     * @return 超时时间
     */
    public long getTimeout() {
        return getResetTimeout();
    }
}