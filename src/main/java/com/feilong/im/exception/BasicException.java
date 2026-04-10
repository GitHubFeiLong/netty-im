package com.feilong.im.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.Serial;
import java.util.HashMap;
import java.util.Map;



/**
 * 自定义异常的基类，其它模块的异常继承进行扩展
 * @author cfl 2026/4/10
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@Slf4j
public class BasicException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * http 响应码
     */
    protected int status;

    /**
     * 错误代码
     */
    protected String code;

    /**
     * 客户端状态码对应信息
     */
    protected String clientMessage;

    /**
     * 服务器状态码对应信息
     */
    protected String serverMessage;

    /**
     * 额外信息
     */
    protected Map<Object, Object> dataMap = new HashMap<>();


    public BasicException() {
    }

    public BasicException(Throwable cause) {
        super(cause);
    }

    //~ 常用静态方法
    //==================================================================================================================
}

