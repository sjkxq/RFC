package com.sjkxq.config;

import com.sjkxq.client.servicecenter.balance.impl.ConsistencyHashBalance;
import com.sjkxq.server.serviceRegister.impl.ZKServiceRegister;
import common.serializer.myserializer.Serializer;
import lombok.*;

/**
 * @ClassName RpcConfig
 * @Description 配置文件
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class RpcConfig {
    //名称
    private String name = "RPC";
    //端口
    private Integer port = 9999;
    //主机名
    private String host = "localhost";
    //版本号
    private String version = "1.0.0";
    //注册中心
    private String registry = new ZKServiceRegister().toString();
    //序列化器
    private String serializer = Serializer.getSerializerByCode(3).toString();
    //负载均衡
    private String loadBalance = new ConsistencyHashBalance().toString();
    //服务治理配置
    private ServiceGovernanceConfig governance;

    public ServiceGovernanceConfig getGovernance() {
        return governance;
    }

}