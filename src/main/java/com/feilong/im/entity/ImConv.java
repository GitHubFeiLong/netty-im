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
 * im会话表
 * @author cfl 2026/04/16
 */
@Data
@TableName("im_conv")
@Accessors(chain = true)
public class ImConv implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 会话ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 会话类型（1-单聊，2-群聊）
     */
    @TableField("type")
    private Byte type;

    /**
     * 单聊时：用户1 ID（与user2_id组合唯一，需满足user1_id < user2_id）
     */
    @TableField("user1_id")
    private Long user1Id;

    /**
     * 单聊时：用户2 ID（单聊必填，群聊为空）
     */
    @TableField("user2_id")
    private Long user2Id;

    /**
     * 群聊时：关联group_id（群聊必填，单聊为空）
     */
    @TableField("group_id")
    private Long groupId;

    /**
     * 最后一条消息ID
     */
    @TableField("last_msg_id")
    private Long lastMsgId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 最后更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}

