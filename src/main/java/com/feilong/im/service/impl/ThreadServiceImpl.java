package com.feilong.im.service.impl;

import com.feilong.im.config.NettyServerHandler;
import com.feilong.im.config.TraceIdHandler;
import com.feilong.im.context.CurrentTimeContext;
import com.feilong.im.context.TraceIdContext;
import com.feilong.im.service.ThreadService;
import com.feilong.im.util.CommonUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author cfl 2026/03/26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ThreadServiceImpl implements ThreadService {

    private final Executor processLogicThreadPool;
    private final TraceIdHandler traceIdHandler;

    /**
     * 具有上下文的环境下，执行任务，执行完任务并清理上下文
     * @param ctx 上下文
     * @param runnable 任务
     */
    @Override
    public void execute(ChannelHandlerContext ctx, Runnable runnable) {
        Channel channel = ctx.channel();
        String traceId = CommonUtil.genSubThreadTraceId(channel);
        // try (ExecutorService virtualExecutor = Executors.newVirtualThreadPerTaskExecutor()){
        //     try {
        //         // 设置TraceId
        //         TraceIdContext.set(traceId);
        //         // 设置当前请求时间
        //         CurrentTimeContext.set();
        //         // 提交任务
        //         virtualExecutor.submit(runnable);
        //     } catch (Exception e) {
        //         log.error("异步执行出现异常");
        //         NettyServerHandler.exceptionCaughtStatic(ctx, e);
        //     } finally {
        //         // 清除 channel 属性
        //         traceIdHandler.handlerRemoved(ctx);
        //         // 清理时间
        //         CurrentTimeContext.remove();
        //         // 清除TraceId
        //         TraceIdContext.remove();
        //     }
        //
        // }
        processLogicThreadPool.execute(() -> {
            try {
                // 设置TraceId
                TraceIdContext.set(traceId);
                // 设置当前请求时间
                CurrentTimeContext.set();
                runnable.run();
            } catch (Exception e) {
                log.error("异步执行出现异常");
                NettyServerHandler.exceptionCaughtStatic(ctx, e);
            } finally {
                // 清除 channel 属性
                traceIdHandler.handlerRemoved(ctx);
                // 清理时间
                CurrentTimeContext.remove();
                // 清除TraceId
                TraceIdContext.remove();
            }
        });
    }
}
