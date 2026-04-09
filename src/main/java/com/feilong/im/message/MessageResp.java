package com.feilong.im.message;

import com.feilong.im.context.CurrentTimeContext;
import com.feilong.im.context.TraceIdContext;
import com.feilong.im.enums.MessageErrorEnum;
import com.feilong.im.enums.MessageTypeEnum;
import com.feilong.im.enums.cmd.IMessageCmdEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 响应
 * @author cfl 2026/03/27
 */
@Data
public class MessageResp<T> {

    /**
     * 错误码
     * @see MessageErrorEnum#getErrCode()
     */
    private String errCode;

    /**
     * 错误信息
     * @see MessageErrorEnum#getErrMsg()
     */
    private String errMsg;

    /**
     * 消息类型
     */
    private MessageTypeEnum type;

    /**
     * 消息子命令
     */
    private String cmd;

    /**
     * 消息数据
     */
    private T data;

    /**
     * 请求id
     */
    private String reqId;

    /**
     * 时间戳
     */
    private LocalDateTime timestamp;

    public MessageResp() {
        this.errCode = MessageErrorEnum.SUCCESSFUL.getErrCode();
        this.errMsg = MessageErrorEnum.SUCCESSFUL.getErrMsg();
        this.timestamp = CurrentTimeContext.get();
    }

    public MessageResp(MessageReq messageReq, IMessageCmdEnum cmdEnum, T data) {
        this.errCode = MessageErrorEnum.SUCCESSFUL.getErrCode();
        this.errMsg = MessageErrorEnum.SUCCESSFUL.getErrMsg();
        this.type = messageReq.getType();
        this.cmd = cmdEnum.name();
        this.data = data;
        this.reqId = messageReq.getReqId();
        this.timestamp = CurrentTimeContext.get();
    }

    public MessageResp(MessageTypeEnum typeEnum, IMessageCmdEnum cmdEnum, T data) {
        this.errCode = MessageErrorEnum.SUCCESSFUL.getErrCode();
        this.errMsg = MessageErrorEnum.SUCCESSFUL.getErrMsg();
        this.type = typeEnum;
        this.cmd = cmdEnum.name();
        this.data = data;
        this.reqId = TraceIdContext.get();
        this.timestamp = CurrentTimeContext.get();
    }

    public MessageResp(MessageReq messageReq, MessageTypeEnum typeEnum, IMessageCmdEnum cmdEnum, T data) {
        this.errCode = MessageErrorEnum.SUCCESSFUL.getErrCode();
        this.errMsg = MessageErrorEnum.SUCCESSFUL.getErrMsg();
        this.type = typeEnum;
        this.cmd = cmdEnum.name();
        this.data = data;
        this.reqId = messageReq.getReqId();
        this.timestamp = CurrentTimeContext.get();
    }

    public MessageResp(MessageReq messageReq, MessageTypeEnum typeEnum, IMessageCmdEnum cmdEnum, MessageErrorEnum errorEnum) {
        this.errCode = errorEnum.getErrCode();
        this.errMsg = errorEnum.getErrMsg();
        this.type = typeEnum;
        this.cmd = cmdEnum.name();
        this.reqId = messageReq.getReqId();
        this.timestamp = CurrentTimeContext.get();
    }

    public MessageResp(MessageReq messageReq, MessageTypeEnum typeEnum, IMessageCmdEnum cmdEnum, MessageErrorEnum errorEnum, String errMsg) {
        this.errCode = errorEnum.getErrCode();
        this.errMsg = errMsg;
        this.type = typeEnum;
        this.cmd = cmdEnum.name();
        this.reqId = messageReq.getReqId();
        this.timestamp = CurrentTimeContext.get();
    }
}
