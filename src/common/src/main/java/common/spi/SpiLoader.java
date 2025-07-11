package common.spi;


import cn.hutool.core.io.resource.ResourceUtil;
import common.serializer.myserializer.Serializer;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.lang.reflect.InvocationTargetException;

/**
 * @ClassName SpiLoader
 * @Description spi实现
 **/
@Slf4j
public class SpiLoader {

    // 存储已加载的 SPI 实现类的映射
    private static final Map<String, Map<String, Class<? extends Serializer>>> loadedSpiMap = new ConcurrentHashMap<>();

    // 缓存实例，避免重复实例化
    private static final Map<String, Object> instanceCache = new ConcurrentHashMap<>();

    // SPI 配置文件的路径
    private static final String SPI_CONFIG_DIR = "META-INF/serializer/";

    /**
     * 加载指定接口的 SPI 实现类
     *
     * @param serviceInterface 接口类
     */
    public static void loadSpi(Class<?> serviceInterface) {
        String interfaceName = serviceInterface.getName();

        // 如果已经加载过该接口的 SPI 实现，直接返回
        if (loadedSpiMap.containsKey(interfaceName)) {
            return;
        }

        Map<String, Class<? extends Serializer>> keyClassMap = new HashMap<>();

        // 读取配置文件，获取所有实现类
        List<URL> resources = ResourceUtil.getResources(SPI_CONFIG_DIR + serviceInterface.getName());
        for (URL resource : resources) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.openStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.trim().isEmpty() && !line.startsWith("#")) {
                        String[] parts = line.split("=");
                        if (parts.length == 2) {
                            String key = parts[0].trim();
                            String className = parts[1].trim();
                            Class<?> implClass = Class.forName(className);
                            if (serviceInterface.isAssignableFrom(implClass)) {
                                Class<? extends Serializer> serializerClass = implClass.asSubclass(Serializer.class);
                                keyClassMap.put(key, serializerClass);
                            }
                        }
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                log.error("Failed to load SPI resource: " + resource, e);
            }
        }

        // 将该接口的 SPI 实现类存入缓存
        loadedSpiMap.put(interfaceName, keyClassMap);
    }

    /**
     * 根据接口和 key 获取 SPI 实现类实例
     *
     * @param serviceInterface 接口类
     * @param key 序列化器的 key
     * @param <T> 接口类型
     * @return 对应的 SPI 实现类实例
     */
    public static <T> T getInstance(Class<T> serviceInterface, String key) {
        String interfaceName = serviceInterface.getName();
        Map<String, Class<? extends Serializer>> keyClassMap = loadedSpiMap.get(interfaceName);

        if (keyClassMap == null) {
            throw new RuntimeException("SPI not loaded for " + interfaceName);
        }

        Class<? extends Serializer> implClass = keyClassMap.get(key);
        if (implClass == null) {
            throw new RuntimeException("No SPI implementation found for key " + key);
        }

        // 原子性地获取或创建实例
        String implClassName = implClass.getName();
        try {
            return serviceInterface.cast(instanceCache.computeIfAbsent(implClassName, className -> {
                try {
                    return implClass.getDeclaredConstructor().newInstance();
                } catch (InstantiationException e) {
                    throw new SpiException("Failed to instantiate SPI implementation (Instantiation failed): " + implClassName, e);
                } catch (IllegalAccessException e) {
                    throw new SpiException("Failed to instantiate SPI implementation (Illegal access): " + implClassName, e);
                } catch (NoSuchMethodException e) {
                    throw new SpiException("Failed to instantiate SPI implementation (No valid constructor): " + implClassName, e);
                } catch (InvocationTargetException e) {
                    throw new SpiException("Failed to instantiate SPI implementation (Constructor threw exception): " + implClassName, e.getTargetException());
                }
            }));
        } catch (RuntimeException e) {
            if (e.getCause() instanceof SpiException) {
                throw (SpiException) e.getCause();
            }
            throw e;
        }
    }
}