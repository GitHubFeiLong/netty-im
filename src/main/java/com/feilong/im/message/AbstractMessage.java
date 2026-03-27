package com.feilong.im.message;

import com.feilong.im.enums.MessageTypeEnum;
import lombok.Data;

/**
 * @author cfl 2026/03/27
 */
@Data
public class AbstractMessage {

    // /**
    //  * 错误码
    //  * @see MessageErrorEnum#getErrCode()
    //  */
    // private String errCode;
    // /**
    //  * 错误信息
    //  * @see MessageErrorEnum#getErrMsg()
    //  */
    // private String errMsg;

    private MessageTypeEnum type;

    private MessageCmdEnum cmd;

    private String data;

    private String reqId;

    private Long timestamp;
}
