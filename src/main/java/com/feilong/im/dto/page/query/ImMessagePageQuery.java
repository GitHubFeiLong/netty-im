package com.feilong.im.dto.page.query;

import java.time.LocalDateTime;
import com.feilong.im.dto.page.query.BasePageQuery;
import java.time.LocalTime;

import java.io.Serializable;
import java.io.Serial;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * im消息表 PageQuery
 * @see com.feilong.im.entity.ImMessage
 * @author cfl 2026/04/16
 */
@Data
@ToString(callSuper=true)
@EqualsAndHashCode(callSuper=true)
@Schema(description = "ImMessagePageQuery")
public class ImMessagePageQuery extends BasePageQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "")
    private Long id;

    /**
     * 所属会话ID
     */
    @Schema(description = "所属会话ID")
    private Long convId;

    /**
     * 发送者ID
     */
    @Schema(description = "发送者ID")
    private Long senderId;

    /**
     * 接收者类型（1-用户，2-群），非空
     */
    @Schema(description = "接收者类型（1-用户，2-群），非空")
    private Byte receiverType;

    /**
     * 接收者ID（用户ID或群ID，与receiver_type对应）
     */
    @Schema(description = "接收者ID（用户ID或群ID，与receiver_type对应）")
    private Long receiverId;

    /**
     * 消息类型（video  order  image  text），非空
     */
    @Schema(description = "消息类型（video  order  image  text），非空")
    private String msgType;

    /**
     * 消息内容
     */
    @Schema(description = "消息内容")
    private String content;

    /**
     * 发送时间（精确到毫秒）
     */
    @Schema(description = "发送时间（精确到毫秒）")
    private LocalDateTime sendTime;

    /**
     * 消息状态（0-发送中，1-已送达服务器，2-已送达接收方，3-已读），默认0
     */
    @Schema(description = "消息状态（0-发送中，1-已送达服务器，2-已送达接收方，3-已读），默认0")
    private Byte status;

    /**
     * 扩展字段（如语音时长、文件大小、@用户列表）
     */
    @Schema(description = "扩展字段（如语音时长、文件大小、@用户列表）")
    private String extra;

    /**
     * 修改时间
     */
    @Schema(description = "修改时间")
    private LocalDateTime updateTime;

    @Schema(description = "分页开始时间")
    private LocalDateTime startTime;

    @Schema(description = "分页结束时间")
    private LocalDateTime endTime;

    /**
     * 获取开始时间
     *
     * @return LocalDateTime 当日0点
     */
    public LocalDateTime getStartTime() {
        return startTime == null ? null : startTime.toLocalDate().atStartOfDay();
    }

    /**
     * 获取截止时间
     *
     * @return LocalDateTime 当日最晚点
     */
    public LocalDateTime getEndTime() {
        return endTime == null ? null : endTime.toLocalDate().atTime(LocalTime.MAX);
    }
}
