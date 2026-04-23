package com.feilong.im.dto.form;

import java.time.LocalDateTime;

import java.io.Serializable;
import java.io.Serial;
import lombok.Data;
import lombok.experimental.Accessors;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 认证用户TOKEN Form
 * @see com.feilong.im.entity.SysToken
 * @author cfl 2026/04/23
 */
@Data
@Accessors(chain = true)
@Schema(description = "SysTokenForm")
public class SysTokenForm implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * TOKEN ID
     */
    @Schema(description = "TOKEN ID")
    private String id;

    /**
     * TOKEN 内容
     */
    @Schema(description = "TOKEN 内容")
    private String token;

    /**
     * 类型（1-IM用户；2-SYS用户）
     */
    @Schema(description = "类型（1-IM用户；2-SYS用户）")
    private Byte type;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    private Long userId;

    /**
     * 状态（0-可用；1-不可用）
     */
    @Schema(description = "状态（0-可用；1-不可用）")
    private Byte status;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
