package com.sjkxq.server.server.impl;

import com.sjkxq.server.netty.NettyServerInitializer;
import com.sjkxq.server.provider.ServiceProvider;
import com.sjkxq.server.server.RpcServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName NettyRpcServer
 * @Description Netty服务端
 * */
public class NettyRpcServer implements RpcServer {
    private static final Logger log = LoggerFactory.getLogger(NettyRpcServer.class);
    private final ServiceProvider serviceProvider;  // 只需要 ServiceProvider
    private ChannelFuture channelFuture;  // ChannelFuture 在 start 方法内初始化
    public NettyRpcServer(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    @Override
    public void start(int port) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        log.info("Netty服务端启动了");

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new NettyServerInitializer(serviceProvider));

            // 同步阻塞，绑定端口启动服务
            channelFuture = serverBootstrap.bind(port).sync();
            log.info("Netty服务端已绑定端口：{}", port);

            // 阻塞，等待服务关闭
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Netty服务端启动中断：{}", e.getMessage(), e);
        } finally {
            shutdown(bossGroup, workGroup);  // 集中管理线程组资源
            log.info("Netty服务端关闭了");
        }
    }

    @Override
    public void stop() {
        if (channelFuture != null) {
            try {
                channelFuture.channel().close().sync();
                log.info("Netty服务端主通道已关闭");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("关闭Netty服务端主通道时中断：{}", e.getMessage(), e);
            }
        } else {
            log.warn("Netty服务端主通道尚未启动，无法关闭");
        }
    }

    private void shutdown(NioEventLoopGroup bossGroup, NioEventLoopGroup workGroup) {
        if (bossGroup != null) {
            bossGroup.shutdownGracefully().syncUninterruptibly();
        }
        if (workGroup != null) {
            workGroup.shutdownGracefully().syncUninterruptibly();
        }
    }
}