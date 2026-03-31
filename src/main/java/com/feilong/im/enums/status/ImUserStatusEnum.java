package com.feilong.im.enums.status;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * IM用户状态枚举
 * @author cfl 2026/3/2
 */
@Getter
@AllArgsConstructor
public enum ImUserStatusEnum {
    // 用户状态（0-离线，1-在线，2-忙碌，3-隐身）
    OFFLINE(0, "离线"),
    ONLINE(1, "在线"),
    BUSY(2, "忙碌"),
    INVISIBLE(3, "隐身"),
    ;
    private final Integer id;
    private final String name;
}
