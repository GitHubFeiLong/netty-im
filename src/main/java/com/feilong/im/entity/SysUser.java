package com.feilong.im.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 系统用户
 * @author cfl 2026/04/16
 */
@Data
@TableName("sys_user")
@Accessors(chain = true)
public class SysUser implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 系统用户ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    @TableField("username")
    private String username;

    /**
     * 密码
     */
    @TableField("password")
    private String password;

    /**
     * 用户角色列表，逗号分隔（如：ADMIN,USER）
     */
    @TableField("roles")
    private String roles;

    /**
     * 用户头像
     */
    @TableField("avatar")
    private String avatar;

    /**
     * 昵称
     */
    @TableField("nickname")
    private String nickname;

    /**
     * 联系方式
     */
    @TableField("mobile")
    private String mobile;

    /**
     * 用户邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 性别(1-男 2-女 0-保密)
     */
    @TableField("gender")
    private Integer gender;

    /**
     * 过期时间，不填永久有效
     */
    @TableField("valid_time")
    private LocalDateTime validTime;

    /**
     * 激活状态：true 激活；false 未激活
     */
    @TableField("enabled")
    private Boolean enabled;

    /**
     * 锁定状态：true 锁定；false 未锁定
     */
    @TableField("locked")
    private Boolean locked;

    /**
     * 最近登录时间
     */
    @TableField("last_login_time")
    private LocalDateTime lastLoginTime;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 版本号
     */
    @Version
    @TableField("version")
    private Integer version;

    /**
     * 删除状态
     */
    @TableLogic
    @TableField("deleted")
    private Boolean deleted;

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

