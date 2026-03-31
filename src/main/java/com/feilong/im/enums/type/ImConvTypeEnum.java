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
    SINGLE(1, "单聊"),
    /**
     * 群聊
     */
    GROUP(2, "群聊"),

    ;
    private final Integer id;
    private final String name;
}
