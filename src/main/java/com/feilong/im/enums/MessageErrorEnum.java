package com.feilong.im.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 消息错误枚举
 * @author cfl 2026/2/28
 */
@Getter
@AllArgsConstructor
public enum MessageErrorEnum {
    /**
     * 成功
     */
    SUCCESSFUL("0", "成功"),

    /**
     * 客户端未认证
     */
    CLIENT_UNAUTHORIZED_ERROR("401", "请登录认证"),
    /**
     * 客户端参数错误
     */
    CLIENT_PARAM_ERROR("1000", "参数错误"),

    /**
     * 服务器错误
     */
    SERVER_ERROR("9999", "服务器错误"),
    ;

    /**
     * 错误码
     */
    private final String errCode;
    /**
     * 错误信息
     */
    private final String errMsg;

}
