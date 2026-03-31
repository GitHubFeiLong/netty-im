package com.feilong.im.handler.cmd;

import com.feilong.im.message.MessageReq;
import io.netty.channel.ChannelHandlerContext;

/**
 * 定义子命令处理器接口
 * @author cfl 2026/03/30
 */
public interface CmdHandler {
    /**
     * 处理子命令
     * @param ctx 上下文
     * @param messageReq 请求参数
     */
    void handle(ChannelHandlerContext ctx, MessageReq messageReq);
}
