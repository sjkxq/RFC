package com.sjkxq.server.ratelimit.provider;

import com.sjkxq.config.RateLimitConfig;
import com.sjkxq.server.ratelimit.RateLimit;
import com.sjkxq.server.ratelimit.impl.TokenBucketRateLimitImpl;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName RateLimitProvider
 * @Description 提供限流器
 */
@Slf4j
public class RateLimitProvider {
    private static final Map<String, RateLimit> rateLimitMap = new ConcurrentHashMap<>();
    private static RateLimitConfig config;

    public static void init(RateLimitConfig config) {
        RateLimitProvider.config = config;
        log.info("限流器配置初始化完成: {}", config);
    }

    // 提供限流实例
    public static RateLimit getRateLimit(String interfaceName) {
        return rateLimitMap.computeIfAbsent(interfaceName, key -> {
            RateLimit rateLimit = new TokenBucketRateLimitImpl(
                config.getPermitsPerSecond(),
                config.getWarmupPeriod()
            );
            log.info("为接口 [{}] 创建了新的限流策略: {}", interfaceName, rateLimit);
            return rateLimit;
        });
    }
}