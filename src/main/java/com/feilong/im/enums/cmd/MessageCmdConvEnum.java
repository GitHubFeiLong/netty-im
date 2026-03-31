package com.feilong.im.enums.cmd;

import com.feilong.im.message.MessageReq;
import com.feilong.im.message.MessageResp;
import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;

/**
 * 会话类型消息的子命令枚举
 * @author cfl 2026/03/27
 */
@Getter
public enum MessageCmdConvEnum implements IMessageCmdEnum {
    /**
     * 拉取会话列表请求
     */
    PAGE_REQ("拉取会话列表请求"),
    PAGE_RESP("拉取会话列表响应"),
    CREATE_REQ("创建会话请求"),
    CREATE_RESP("创建会话请求"),
    DELETE_REQ("删除会话请求"),
    DELETE_RESP("删除会话响应"),
    UPDATE_REQ("更新会话请求"),
    ;

    private final String name;

    MessageCmdConvEnum(String name) {
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
