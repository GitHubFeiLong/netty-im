package com.feilong.im.enums;

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
    AUTH_REQ("认证请求"),

    /**
     * 服务端→客户端
     * 返回认证结果（成功/失败）
     */
    AUTH_RESP("认证响应"),

    /**
     * 客户端→服务端
     * 心跳包（维持连接，防断开）
     */
    HEARTBEAT_REQ("心跳请求"),
    /**
     * 服务端→客户端	心跳响应（确认存活）
     */
    HEARTBEAT_RESP("心跳响应"),

    /**
     * 服务端→客户端	错误通知（如认证失败、参数错误）
     */
    ERROR("错误通知"),
    ;

    private final String name;

    MessageCmdSystemEnum(String name) {
        this.name = name;
    }
}
