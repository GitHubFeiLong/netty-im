package com.feilong.im.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.io.Serial;

/**
 * 自定义服务器内部错误
 * @author cfl 2026/4/10
 */
@Slf4j
public class ServerException extends BasicException {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final HttpStatus HTTP_STATUS = HttpStatus.INTERNAL_SERVER_ERROR;

    public ServerException() {
        super();
    }

    public ServerException(Throwable cause) {
        super(cause);
    }

    public static ServerException of(String serverMessage) {
        return ServerException.of(null, serverMessage);
    }

    public static ServerException of(String serverMessageFormat, Object... args) {
        return ServerException.of(null, serverMessageFormat, args);
    }

    public static ServerException of(Throwable cause, String serverMessage) {
        ServerException serverException = new ServerException(cause);
        serverException.setStatus(HTTP_STATUS.value());
        serverException.setCode(String.valueOf(HTTP_STATUS.value()));
        serverException.setClientMessage(HTTP_STATUS.getReasonPhrase());
        serverException.setServerMessage(serverMessage);
        return serverException;
    }

    public static ServerException of(Throwable cause, String serverMessageFormat, Object... args) {
        ServerException serverException = new ServerException(cause);
        serverException.setStatus(HTTP_STATUS.value());
        serverException.setCode(String.valueOf(HTTP_STATUS.value()));
        serverException.setServerMessage(String.format(serverMessageFormat, args));
        serverException.setClientMessage(HTTP_STATUS.getReasonPhrase());
        return serverException;
    }
}
