package com.feilong.im.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * im消息表
 * @author cfl 2026/3/26
 */
@Data
public class ImMessageDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 所属会话ID
     */
    private Long convId;

    /**
     * 发送者ID
     */
    private Long senderId;

    /**
     * 接收者类型（1-用户，2-群），非空
     */
    private Integer receiverType;

    /**
     * 接收者ID（用户ID或群ID，与receiver_type对应）
     */
    private Long receiverId;

    /**
     * 消息类型（video  order  image  text），非空
     */
    private String msgType;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 发送时间（精确到毫秒）
     */
    private LocalDateTime sendTime;

    /**
     * 消息状态（0-发送中，1-已送达服务器，2-已送达接收方，3-已读），默认0
     */
    private Integer status;

    /**
     * 扩展字段（如语音时长、文件大小、@用户列表）
     */
    private String extra;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

}
