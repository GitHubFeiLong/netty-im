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
 * 应用表
 * @author cfl 2026/04/14
 */
@Data
@TableName("sys_app")
@Accessors(chain = true)
public class SysApp implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 应用ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 应用密钥
     */
    @TableField("secret")
    private String secret;

    /**
     * 应用名称
     */
    @TableField("name")
    private String name;

    /**
     * 登录成功后，重定向到该地址，处理登录逻辑
     */
    @TableField("index_url")
    private String indexUrl;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 状态(1-正常 0-禁用)
     */
    @TableField("status")
    private Byte status;

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
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    /**
     * 创建人ID
     */
    @TableField("created_by")
    private Long createdBy;

    /**
     * 更新时间
     */
    @TableField(value = "updated_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;

    /**
     * 更新人ID
     */
    @TableField("updated_by")
    private Long updatedBy;
}

