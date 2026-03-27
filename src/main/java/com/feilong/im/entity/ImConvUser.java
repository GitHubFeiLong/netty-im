package com.feilong.im.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serial;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * im用户会话表
 * </p>
 *
 * @author author
 * @since 2026-03-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("im_conv_user")
public class ImConvUser implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 会话ID
     */
    @TableField("conv_id")
    private Long convId;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 该用户在此会话的未读消息数（初始0，新消息+1，阅读后清零）
     */
    @TableField("unread_count")
    private Integer unreadCount;

    /**
     * 该用户最后阅读的消息ID（用于定位未读起点）
     */
    @TableField("last_read_id")
    private Long lastReadId;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 最后更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;


}
