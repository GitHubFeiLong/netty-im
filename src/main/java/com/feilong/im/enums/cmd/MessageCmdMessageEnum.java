package com.feilong.im.enums.cmd;

import com.feilong.im.message.MessageReq;
import com.feilong.im.message.MessageResp;
import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;

/**
 * @author cfl 2026/03/27
 */
@Getter
public enum MessageCmdMessageEnum implements IMessageCmdEnum {

    ;

    private final String name;

    MessageCmdMessageEnum(String name) {
        this.name = name;
    }

    /**
     * 处理子命令
     *
     * @param ctx 上下文
     * @param req 请求参数
     */
    @Override
    public void handler(ChannelHandlerContext ctx, MessageReq req) {
    }
}
