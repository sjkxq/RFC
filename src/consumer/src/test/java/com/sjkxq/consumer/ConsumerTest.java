package com.sjkxq.consumer;

import com.sjkxq.client.proxy.ClientProxy;
import com.sjkxq.pojo.User;
import com.sjkxq.service.UserService;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName ConsumerTest
 * @Description 客户端测试
 */
@Slf4j
public class ConsumerTest {
    private static final int THREAD_POOL_SIZE = 30;
    private static final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

    public static void main(String[] args) throws InterruptedException {
        ClientProxy clientProxy = new ClientProxy();
        UserService proxy = clientProxy.getProxy(UserService.class);
        for (int i = 0; i < 120; i++) {
            final Integer i1 = i;
            if (i % 30 == 0) {
                // Simulate delay for every 30 requests
                Thread.sleep(10000);
            }

            // Submit tasks to executor service (thread pool)
            executorService.submit(() -> {
                try {
                    User user = proxy.getUserByUserId(i1);
                    if (user != null) {
                        log.info("从服务端得到的user={}", user);
                    } else {
                        log.warn("获取的 user 为 null, userId={}", i1);
                    }

                    Integer id = proxy.insertUserId(User.builder()
                            .id(i1)
                            .userName("User" + i1)
                            .gender(true)
                            .build());

                    if (id != null) {
                        log.info("向服务端插入user的id={}", id);
                    } else {
                        log.warn("插入失败，返回的id为null, userId={}", i1);
                    }
                } catch (Exception e) {
                    log.error("调用服务时发生异常，userId={}", i1, e);
                }
            });
        }

        // Gracefully shutdown the executor service
        executorService.shutdown();
        try {
            // 等待最多30秒，确保所有任务完成
            if (!executorService.awaitTermination(30, TimeUnit.SECONDS)) {
                log.warn("线程池未能在30秒内完成所有任务，强制关闭");
                executorService.shutdownNow();
            } else {
                log.info("所有任务已成功完成");
            }
        } catch (InterruptedException e) {
            log.error("等待任务完成时被中断", e);
            // 重新设置中断标志
            Thread.currentThread().interrupt();
            // 强制关闭线程池
            executorService.shutdownNow();
        }
        
        // 关闭Zipkin跟踪报告器
        try {
            log.info("正在关闭Zipkin跟踪报告器...");
            com.sjkxq.trace.ZipkinReporter.close();
            log.info("Zipkin跟踪报告器已关闭");
        } catch (Exception e) {
            log.error("关闭Zipkin跟踪报告器时发生错误", e);
        }
        
        // 所有任务完成后关闭客户端代理
        clientProxy.close();
    }

}