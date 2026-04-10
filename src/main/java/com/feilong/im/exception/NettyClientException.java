package com.feilong.im.exception;

import com.feilong.im.enums.MessageErrorEnum;
import lombok.Getter;

/**
 * @author cfl 2026/04/08
 */
@Getter
public class NettyClientException extends RuntimeException{

    private final MessageErrorEnum errorEnum;

    public NettyClientException(MessageErrorEnum errorEnum) {
        super(errorEnum.getErrMsg());
        this.errorEnum = errorEnum;
    }

    public NettyClientException(String message, MessageErrorEnum errorEnum) {
        super(message);
        this.errorEnum = errorEnum;
    }
}
