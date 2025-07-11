package com.sjkxq.server.ratelimit;


/**
 * @InterfaceName RateLimit
 * @Description 限流接口
 */

public interface RateLimit {
    //获取访问许可
    boolean getToken();
}
