package com.feilong.im.enums;

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
    FETCH_LIST_REQ("拉取会话列表请求"),
    FETCH_LIST_RESP("拉取会话列表响应"),
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
}
