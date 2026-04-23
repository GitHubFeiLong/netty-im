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
 * 认证用户TOKEN
 * @author cfl 2026/04/23
 */
@Data
@TableName("sys_token")
@Accessors(chain = true)
public class SysToken implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * TOKEN ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    /**
     * TOKEN 内容
     */
    @TableField("token")
    private String token;

    /**
     * 类型（1-IM用户；2-SYS用户）
     */
    @TableField("type")
    private Byte type;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 状态（0-可用；1-不可用）
     */
    @TableField("status")
    private Byte status;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}

