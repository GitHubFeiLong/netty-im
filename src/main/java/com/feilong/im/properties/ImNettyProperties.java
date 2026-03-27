package com.feilong.im.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Netty相关配置
 * @author cfl 2026/3/26
 */
@Data
@Component
@ConfigurationProperties(prefix = "im.netty")
public class ImNettyProperties {
    /**
     * 服务端端口（默认 8081）
     */
    private int port = 8081;
    /**
     * socket 路径
     */
    private String path = "/ws";
    /**
     * Boss 线程数（默认 CPU 核心数）
     */
    private int bossThreadCount = Runtime.getRuntime().availableProcessors();
    /**
     * Worker 线程数（默认 CPU 核心数 * 2）
     */
    private int workerThreadCount = Runtime.getRuntime().availableProcessors() * 2;

    /**
     * TCP 连接超时时间（毫秒，默认 30 秒）
     */
    private int connectTimeout = 30000;

    /**
     * 接收缓冲区大小（字节，默认 64KB）
     */
    private int recvBufSize = 65536;

    /**
     * 发送缓冲区大小（字节，默认 64KB）
     */
    private int sendBufSize = 65536;
}
