package com.feilong.im.enums.status;

import lombok.Getter;

/**
 * 认证Token状态枚举
 * @author cfl 2026/04/13
 */
@Getter
public enum AuthTokenStatusEnum {
    /**
     * 有效
     */
    VALID("0"),

    /**
     * 无效
     */
    INVALID("1");

    private final String id;

    AuthTokenStatusEnum(String id) {
        this.id = id;
    }
}
