package com.feilong.im.enums.cmd;

import com.feilong.im.message.MessageReq;
import com.feilong.im.message.MessageResp;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author cfl 2026/03/27
 */
public interface IMessageCmdEnum {

    /**
     * 获取子命令枚举名称
     * @return 子命令枚举名称
     */
    String name();

    /**
     * 处理子命令
     * @param ctx 上下文
     * @param req 请求参数
     */
    void handler(ChannelHandlerContext ctx, MessageReq req);
}
