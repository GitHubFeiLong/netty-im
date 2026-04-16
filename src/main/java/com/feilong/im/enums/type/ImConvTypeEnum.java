package com.feilong.im.enums.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author cfl 2026/03/31
 */
@Getter
@AllArgsConstructor
public enum ImConvTypeEnum {

    /**
     * 单聊
     */
    SINGLE((byte)1, "单聊"),
    /**
     * 群聊
     */
    GROUP((byte)2, "群聊"),

    ;
    private final Byte id;
    private final String name;
}
