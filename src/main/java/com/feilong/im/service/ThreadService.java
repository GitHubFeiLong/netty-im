package com.feilong.im.service;

import com.feilong.im.message.MessageReq;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author cfl 2026/03/26
 */
public interface ThreadService {
    /**
     * 具有上下文的环境下，执行任务，执行完任务并清理上下文
     * @param ctx 上下文
     * @param messageReq 请求消息内容
     * @param runnable 任务
     */
    void execute(ChannelHandlerContext ctx, MessageReq messageReq, Runnable runnable);

}
