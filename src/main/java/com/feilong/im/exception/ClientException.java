package com.feilong.im.exception;

import com.feilong.im.util.AssertUtil;
import com.feilong.im.util.StringUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.io.Serial;
import java.util.Map;


/**
 * 自定义客户端异常
 * @author cfl 2026/4/10
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@Slf4j
@Accessors(chain=true)
public class ClientException extends BasicException {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;
    //~constructors
    //==================================================================================================================

    public ClientException() {
        super();
    }

    /**
     * 无参构造器
     */
    public ClientException(Throwable cause) {
        super(cause);
    }

    public static ClientException of(String clientMessage) {
        ClientException clientException = new ClientException();
        clientException.setStatus(HTTP_STATUS.value());
        clientException.setCode(String.valueOf(HTTP_STATUS.value()));
        clientException.setClientMessage(clientMessage);
        clientException.setServerMessage(HTTP_STATUS.getReasonPhrase());
        return clientException;
    }

    public static ClientException of(String clientMessageFormat, Object... args) {
        ClientException clientException = new ClientException();
        clientException.setStatus(HTTP_STATUS.value());
        clientException.setCode(String.valueOf(HTTP_STATUS.value()));
        clientException.setClientMessage(String.format(clientMessageFormat, args));
        clientException.setServerMessage(HTTP_STATUS.getReasonPhrase());
        return clientException;
    }

    public static ClientException of(Throwable cause, String clientMessage) {
        ClientException exception = new ClientException(cause);
        exception.setStatus(HTTP_STATUS.value());
        exception.setCode(String.valueOf(HTTP_STATUS.value()));
        exception.setClientMessage(clientMessage);
        exception.setServerMessage(HTTP_STATUS.getReasonPhrase());
        return exception;
    }

    public static ClientException of(Throwable cause, String clientMessageFormat, Object... args) {
        ClientException exception = new ClientException(cause);
        exception.setStatus(HTTP_STATUS.value());
        exception.setCode(String.valueOf(HTTP_STATUS.value()));
        exception.setClientMessage(String.format(clientMessageFormat, args));
        exception.setServerMessage(HTTP_STATUS.getReasonPhrase());
        return exception;
    }

    public static ClientException of(HttpStatus status) {
        ClientException exception = new ClientException();
        exception.setStatus(status.value());
        exception.setCode(String.valueOf(status.value()));
        exception.setClientMessage("权限不足");
        exception.setServerMessage("权限不足");
        return exception;
    }

    public static ClientException of(HttpStatus status, String clientMessageFormat, Object... args) {
        ClientException exception = new ClientException();
        exception.setStatus(status.value());
        exception.setCode(String.valueOf(status.value()));
        exception.setClientMessage(String.format(clientMessageFormat, args));
        exception.setServerMessage(status.getReasonPhrase());
        return exception;
    }
}
