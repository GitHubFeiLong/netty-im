package com.feilong.im.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * 认证token黑名单
 * @author cfl 2026/04/13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_auth_token_blacklist")
public class SysAuthTokenBlacklist extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * tokenId
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    /**
     * token
     */
    @TableField(value = "token")
    private String token;

    /**
     * 删除状态（0-未删除；>0删除时的时间戳）
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Long deleted;
}
