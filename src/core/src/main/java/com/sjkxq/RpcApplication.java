package com.sjkxq;

import com.sjkxq.config.RpcConfig;
import com.sjkxq.config.RpcConstant;
import com.sjkxq.server.governance.ServiceGovernanceManager;
import common.util.ConfigUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName RpcApplication
 * @Description 测试配置顶，学习更多参考Dubbo
 **/
@Slf4j
public class RpcApplication {
    private static volatile RpcConfig RpcConfigInstance;

    public static void initialize(RpcConfig customRpcConfig) {
        RpcConfigInstance = customRpcConfig;
        // 初始化服务治理
        ServiceGovernanceManager.getInstance().init(customRpcConfig.getGovernance());
        log.info("RPC 框架初始化，配置 = {}", customRpcConfig);
    }

    public static void initialize() {
        RpcConfig customRpcConfig;
        try {
            customRpcConfig = ConfigUtil.loadConfig(RpcConfig.class, RpcConstant.CONFIG_FILE_PREFIX);
            log.info("成功加载配置文件，配置文件名称 = {}", RpcConstant.CONFIG_FILE_PREFIX); // 添加成功加载的日志
        } catch (Exception e) {
            // 配置加载失败，使用默认配置
            customRpcConfig = new RpcConfig();
            log.warn("配置加载失败，使用默认配置");
        }
        initialize(customRpcConfig);
    }

    public static RpcConfig getRpcConfig() {
        if (RpcConfigInstance == null) {
            synchronized (RpcApplication.class) {
                if (RpcConfigInstance == null) {
                    initialize();  // 确保在第一次调用时初始化
                }
            }
        }
        return RpcConfigInstance;
    }
}