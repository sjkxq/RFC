package com.sjkxq.config;

/**
 * 限流器配置类
 */
public class RateLimitConfig {
    private boolean enabled = true;
    private int permitsPerSecond = 100;
    private int warmupPeriod = 10; // 秒

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getPermitsPerSecond() {
        return permitsPerSecond;
    }

    public void setPermitsPerSecond(int permitsPerSecond) {
        this.permitsPerSecond = permitsPerSecond;
    }

    public int getWarmupPeriod() {
        return warmupPeriod;
    }

    public void setWarmupPeriod(int warmupPeriod) {
        this.warmupPeriod = warmupPeriod;
    }
}