package com.feilong.im.enums.cmd;

import com.feilong.im.handler.netty.cmd.CmdHandler;
import com.feilong.im.handler.netty.cmd.NoneHandler;
import com.feilong.im.handler.netty.cmd.SystemAuthReqHandler;
import com.feilong.im.handler.netty.cmd.SystemHeartbeatReqHandler;
import com.feilong.im.message.MessageReq;
import com.feilong.im.util.SpringUtil;
import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;

/**
 * 系统类型消息的子命令枚举
 * @author cfl 2026/03/27
 */
@Getter
public enum MessageCmdSystemEnum implements IMessageCmdEnum {
    /**
     * 客户端→服务端
     * 连接后发起身份认证（首次必发）
     */
    AUTH_REQ("认证请求", SpringUtil.getBean(SystemAuthReqHandler.class)),

    /**
     * 服务端→客户端
     * 返回认证结果（成功/失败）
     */
    AUTH_RESP("认证响应"),

    /**
     * 服务端→客户端
     */
    ONLINE_RESP("上线通知"),

    /**
     * 服务端→客户端
     */
    OFFLINE_RESP("下线通知"),

    /**
     * 客户端→服务端
     * 心跳包（维持连接，防断开）
     */
    HEARTBEAT_REQ("心跳请求", SpringUtil.getBean(SystemHeartbeatReqHandler.class)),
    /**
     * 服务端→客户端	心跳响应（确认存活）
     */
    HEARTBEAT_RESP("心跳响应"),

    /**
     * 服务端→客户端	错误通知（如认证失败、参数错误）
     */
    ERROR("错误通知"),
    ;

    /**
     * 子命令名称
     */
    private final String name;

    /**
     * 子命令处理对象
     */
    private final CmdHandler cmdHandler;

    MessageCmdSystemEnum(String name) {
        this.name = name;
        this.cmdHandler = SpringUtil.getBean(NoneHandler.class);
    }

    MessageCmdSystemEnum(String name, CmdHandler cmdHandler) {
        this.name = name;
        this.cmdHandler = cmdHandler;
    }

    /**
     * 处理子命令
     *
     * @param ctx 上下文
     * @param req 请求参数
     */
    @Override
    public void handler(ChannelHandlerContext ctx, MessageReq req) {
        cmdHandler.handle(ctx, req);
    }
}
