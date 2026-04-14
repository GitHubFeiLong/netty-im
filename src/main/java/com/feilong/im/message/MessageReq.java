package com.feilong.im.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.feilong.im.constant.CommonConst;
import com.feilong.im.context.CurrentTimeContext;
import com.feilong.im.enums.MessageTypeEnum;
import com.feilong.im.enums.cmd.IMessageCmdEnum;
import com.google.common.base.Strings;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.function.Supplier;

/**
 * 请求
 * @author cfl 2026/03/27
 */
@Data
@Accessors(chain=true)
public class MessageReq {

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
    private String data;

    /**
     * 请求id
     */
    private String reqId;

    /**
     * 时间戳
     */
    private LocalDateTime timestamp;

    /**
     * 获取解析后的子命令枚举
     * @return 子命令枚举
     */
    public IMessageCmdEnum getCmdEnum() {
        if (type == null || Strings.isNullOrEmpty(cmd)) {
            return null;
        }
        return type.getCmdEnumByName(cmd);
    }

    /**
     * 检查参数
     */
    public void check() {
        if (type == null) {
            throw new IllegalArgumentException("消息类型不能为空");
        }
        if (cmd == null) {
            throw new IllegalArgumentException("消息子命令不能为空");
        }
        IMessageCmdEnum cmdEnum = getCmdEnum();
        if (!type.getCmds().contains(cmdEnum)) {
            throw new IllegalArgumentException("消息类型" + type.getName() + "不支持子命令" + cmd);
        }
        if (Strings.isNullOrEmpty(reqId)) {
            throw new IllegalArgumentException("请求ID不能为空");
        }
        if (timestamp == null) {
            throw new IllegalArgumentException("时间戳不能为空");
        }

        if (timestamp.plusMinutes(CommonConst.TIMESTAMP_RANGE_MINUTE).isBefore(timestamp)) {
            throw new IllegalArgumentException("时间戳已过期");
        }
    }
}
