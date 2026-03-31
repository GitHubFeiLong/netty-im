package com.feilong.im.enums.status;

import lombok.Getter;

/**
 * message状态枚举
 * @author cfl 2026/2/28
 */
@Getter
public enum ImMessageStatusEnum {
    /**
     * 发送中
     */
    SENDING(0),
    /**
     * 已送达服务器
     */
    DELIVERED_TO_SERVER(1),
    /**
     * 已送达接收方
     */
    DELIVERED_TO_RECEIVER(2),
    /**
     * 已读
     */
    READ(3),
    ;

    private final Integer id;

    ImMessageStatusEnum(Integer id) {
        this.id = id;
    }
}
