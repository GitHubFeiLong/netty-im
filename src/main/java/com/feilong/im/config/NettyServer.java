package com.feilong.im.config;

import com.feilong.im.properties.ImNettyProperties;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


/**
 * Netty服务器
 * @author cfl 2026/02/26
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NettyServer {
    private final ImNettyProperties imNettyProperties;
    private final NettyServerHandler nettyServerHandler;
    private final TraceIdHandler traceIdHandler;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private ChannelFuture channelFuture;

    @PostConstruct
    @SuppressWarnings("all")
    public void start() throws Exception {
        new Thread(() -> {
            try {
                log.info("Netty服务器启动中...");
                bossGroup = new NioEventLoopGroup(imNettyProperties.getBossThreadCount());
                workerGroup = new NioEventLoopGroup(imNettyProperties.getWorkerThreadCount());
                // 服务端启动器，负责组装netty组件，启动服务器
                ServerBootstrap bootstrap = new ServerBootstrap();
                // 设置线程组 boss负责处理 accept 事件，worker负责socket上的读写事件
                bootstrap.group(bossGroup, workerGroup)
                        // 选择服务器的NIO 模式
                        .channel(NioServerSocketChannel.class)
                        // 连接队列大小
                        .option(ChannelOption.SO_BACKLOG, 10000)
                        // 连接超时
                        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, imNettyProperties.getConnectTimeout())
                        // 接收缓冲区
                        .childOption(ChannelOption.SO_RCVBUF, imNettyProperties.getRecvBufSize())
                        // 发送缓冲区
                        .childOption(ChannelOption.SO_SNDBUF, imNettyProperties.getSendBufSize())
                        // 长连接 保持连接
                        .childOption(ChannelOption.SO_KEEPALIVE, true)
                        // 服务端日志处理器
                        .handler(new LoggingHandler(LogLevel.INFO))
                        // 快速重启
                        .option(ChannelOption.SO_REUSEADDR, true)
                        // 禁用Nagle算法
                        .childOption(ChannelOption.TCP_NODELAY, true)
                        // 添加处理器 连接建立后，被调用
                        .childHandler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel ch) {
                                // 获取管道,给管道添加处理器
                                ChannelPipeline pipeline = ch.pipeline();
                                // 添加TraceId入站Handler（最先添加）
                                pipeline.addFirst(traceIdHandler);
                                // 添加日志
                                // pipeline.addLast(new LoggingHandler(LogLevel.INFO));
                                // HTTP请求解码器
                                pipeline.addLast(new HttpServerCodec());
                                // 主要用于处理大数据流
                                pipeline.addLast(new ChunkedWriteHandler());
                                // 将HTTP消息聚合为FullHttpRequest或FullHttpResponse
                                pipeline.addLast(new HttpObjectAggregator(8192));
                                // WebSocket处理器
                                pipeline.addLast(new WebSocketServerProtocolHandler(imNettyProperties.getPath()));
                                // 心跳检测
                                pipeline.addLast(new IdleStateHandler(0, 0, 30, TimeUnit.SECONDS));
                                // 自定义处理器
                                pipeline.addLast(nettyServerHandler);
                            }
                        });

                // 绑定监听端口
                channelFuture = bootstrap.bind(imNettyProperties.getHost(), imNettyProperties.getPort()).sync();
                log.info("Netty WebSocket服务器启动成功，地址：ws://127.0.0.1:{}{}", imNettyProperties.getPort(), imNettyProperties.getPath());

                channelFuture.channel().closeFuture().sync();
            } catch (Exception e) {
                log.error("Netty服务器启动失败", e);
            }
        }).start();
    }

    /**
     * Netty关闭
     */
    @PreDestroy
    public void stop() {
        if (bossGroup != null) {
            bossGroup.shutdownGracefully();
        }
        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
        }
        log.info("Netty服务器已关闭");
    }
}
