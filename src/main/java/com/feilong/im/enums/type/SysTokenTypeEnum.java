package com.feilong.im.enums.type;

import lombok.Getter;

/**
 * 系统TOKEN类型枚举
 * @author cfl 2026/04/23
 */
@Getter
public enum SysTokenTypeEnum {

    /**
     * IM用户
     */
    IM_USER((byte)1, "IM用户"),
    /**
     * 系统用户
     */
    SYS_USER((byte)2, "SYS用户"),
    ;
    private final Byte id;
    private final String name;

    SysTokenTypeEnum(Byte id, String name) {
        this.id = id;
        this.name = name;
    }
}
