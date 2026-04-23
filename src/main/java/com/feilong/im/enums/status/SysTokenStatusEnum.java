package com.feilong.im.enums.status;

import lombok.Getter;

/**
 * 系统TOKEN状态枚举
 * @author cfl 2026/04/23
 */
@Getter
public enum SysTokenStatusEnum {
    /**
     * 可用
     */
    AVAILABLE((byte)0, "可用"),
    /**
     * 不可用
     */
    UNAVAILABLE((byte)1, "不可用"),
    ;
    private final Byte id;
    private final String name;

    SysTokenStatusEnum(Byte id, String name) {
        this.id = id;
        this.name = name;
    }
}
