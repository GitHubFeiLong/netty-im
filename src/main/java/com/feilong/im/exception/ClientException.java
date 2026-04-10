package com.feilong.im.exception;

import com.feilong.im.util.AssertUtil;
import com.feilong.im.util.StringUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
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
        return ClientException.of(null, clientMessage);
    }

    public static ClientException of(String clientMessageFormat, Object... args) {
        return ClientException.of(null, clientMessageFormat, args);
    }

    public static ClientException of(Throwable cause, String clientMessage) {
        ClientException clientException = new ClientException(cause);
        clientException.setStatus(HTTP_STATUS.value());
        clientException.setCode(String.valueOf(HTTP_STATUS.value()));
        clientException.setClientMessage(clientMessage);
        clientException.setServerMessage(HTTP_STATUS.getReasonPhrase());
        return clientException;
    }

    public static ClientException of(Throwable cause, String clientMessageFormat, Object... args) {
        ClientException clientException = new ClientException(cause);
        clientException.setStatus(HTTP_STATUS.value());
        clientException.setCode(String.valueOf(HTTP_STATUS.value()));
        clientException.setClientMessage(String.format(clientMessageFormat, args));
        clientException.setServerMessage(HTTP_STATUS.getReasonPhrase());
        return clientException;
    }


}
