package com.sjkxq.consumer;


import com.sjkxq.config.RpcConfig;
import common.util.ConfigUtil;

/**
 * @ClassName ConsumerTestConfig
 * @Description 测试配置顶
 */
public class ConsumerTestConfig {
    public static void main(String[] args) {
        RpcConfig rpc = ConfigUtil.loadConfig(RpcConfig.class, "rpc");
        System.out.println(rpc);
    }

}
