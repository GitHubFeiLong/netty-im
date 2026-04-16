package com.feilong.im.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 认证token 黑名单
 * @author cfl 2026/04/16
 */
@Data
@Accessors(chain = true)
@TableName("sys_auth_token_blacklist")
public class SysAuthTokenBlacklist implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 认证token ID
     */
    @TableField("id")
    private String id;

    /**
     * token内容
     */
    @TableField("token")
    private String token;

    /**
     * 删除状态（0-未删除；>0删除时的时间戳）
     */
    @TableLogic
    @TableField("deleted")
    private Long deleted;

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

