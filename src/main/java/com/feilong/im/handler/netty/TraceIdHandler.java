package com.feilong.im.handler.netty;

import com.feilong.im.context.TraceIdContext;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 给Channel设置TraceId
 * @author cfl 2026/02/28
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class TraceIdHandler extends ChannelDuplexHandler {

    public static final AttributeKey<String> TRACE_ID_KEY = AttributeKey.valueOf("traceId");

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            String traceId = TraceIdContext.generateTraceId();
            // 设置TraceId到MDC
            TraceIdContext.set(traceId);
            ctx.channel().attr(TRACE_ID_KEY).set(traceId);
            // 继续处理下一个Handler
            super.channelRead(ctx, msg);
        } finally {
            ctx.channel().attr(TRACE_ID_KEY).set(null);
            TraceIdContext.remove();
        }
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        try {
            // 从 Channel 属性获取 TraceId
            String traceId = ctx.channel().attr(TRACE_ID_KEY).get();
            if (traceId != null) {
                TraceIdContext.set(traceId);
            }
            super.write(ctx, msg, promise);
        } finally {
            TraceIdContext.remove();
        }
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        try {
            // 清理Channel属性避免内存泄漏
            ctx.channel().attr(TRACE_ID_KEY).set(null);
            super.handlerRemoved(ctx);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
