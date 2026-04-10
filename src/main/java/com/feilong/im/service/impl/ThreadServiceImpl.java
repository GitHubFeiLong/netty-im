package com.feilong.im.service.impl;

import com.feilong.im.config.NettyServerHandler;
import com.feilong.im.config.TraceIdHandler;
import com.feilong.im.context.CurrentTimeContext;
import com.feilong.im.context.CurrentUserContext;
import com.feilong.im.context.TraceIdContext;
import com.feilong.im.enums.MessageTypeEnum;
import com.feilong.im.enums.cmd.MessageCmdSystemEnum;
import com.feilong.im.exception.NettyClientException;
import com.feilong.im.message.MessageReq;
import com.feilong.im.message.MessageResp;
import com.feilong.im.service.ThreadService;
import com.feilong.im.util.CommonUtil;
import com.feilong.im.util.JsonUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executor;

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
     * @param messageReq 请求消息内容
     * @param runnable 任务
     */
    @Override
    public void execute(ChannelHandlerContext ctx, MessageReq messageReq, Runnable runnable) {
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
                CurrentUserContext.set(ctx);
                runnable.run();
            } catch (NettyClientException e) {
                log.warn("客户端异常", e);
                // 发送错误信息给客户端
                MessageResp<Object> resp = new MessageResp<>(messageReq, MessageTypeEnum.SYSTEM, MessageCmdSystemEnum.ERROR, e.getErrorEnum(), e.getMessage());
                ctx.writeAndFlush(new TextWebSocketFrame(JsonUtil.toJsonString(resp)));
            } catch (Exception e) {
                log.error("异步执行出现异常");
                NettyServerHandler.exceptionCaughtStatic(ctx, e);
            } finally {
                // 清除当前用户上下文
                CurrentUserContext.remove();
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
