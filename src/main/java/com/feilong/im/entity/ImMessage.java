package com.feilong.im.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * im消息表
 * @author cfl 2026/04/16
 */
@Data
@Accessors(chain = true)
@TableName("im_message")
public class ImMessage implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 所属会话ID
     */
    @TableField("conv_id")
    private Long convId;

    /**
     * 发送者ID
     */
    @TableField("sender_id")
    private Long senderId;

    /**
     * 接收者类型（1-用户，2-群），非空
     */
    @TableField("receiver_type")
    private Byte receiverType;

    /**
     * 接收者ID（用户ID或群ID，与receiver_type对应）
     */
    @TableField("receiver_id")
    private Long receiverId;

    /**
     * 消息类型（video  order  image  text），非空
     */
    @TableField("msg_type")
    private String msgType;

    /**
     * 消息内容
     */
    @TableField("content")
    private String content;

    /**
     * 发送时间（精确到毫秒）
     */
    @TableField("send_time")
    private LocalDateTime sendTime;

    /**
     * 消息状态（0-发送中，1-已送达服务器，2-已送达接收方，3-已读），默认0
     */
    @TableField("status")
    private Byte status;

    /**
     * 扩展字段（如语音时长、文件大小、@用户列表）
     */
    @TableField("extra")
    private String extra;

    /**
     * 修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}

