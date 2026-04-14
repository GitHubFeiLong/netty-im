package com.feilong.im.util;

import com.feilong.im.core.Result;
import com.feilong.im.exception.BasicException;
import com.feilong.im.exception.ClientException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 响应工具类
 * @author cfl 2026/04/14
 */
public class ResponseUtil {

    /**
     * 写入错误响应
     * @param response 响应对象
     * @param exception 异常
     */
    public static void writeErrorResponse(HttpServletResponse response, BasicException exception) throws IOException {
        response.setStatus(exception.getStatus());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        Result<Object> result = Result.ofFail(exception);
        response.getWriter().write(JsonUtil.toJsonString(result));
    }

    /**
     * 写入错误响应
     * @param response 响应对象
     * @param exception 异常
     * @param clientMessage 错误消息
     * @param status HTTP 状态码
     */
    public static void writeErrorResponse(HttpServletResponse response, Exception exception, String clientMessage, HttpStatus status) throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        BasicException basicException = ClientException.of(exception, clientMessage).setStatus(status.value()).setCode(String.valueOf(status.value())).setServerMessage(exception.getMessage());
        Result<Object> result = Result.ofFail(basicException);
        response.getWriter().write(JsonUtil.toJsonString(result));
    }
}
