package com.feilong.im.dto.bo;

import java.time.LocalDateTime;

import java.io.Serializable;
import java.io.Serial;
import lombok.Data;
import lombok.experimental.Accessors;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 认证token 黑名单 BO
 * @see com.feilong.im.entity.SysAuthTokenBlacklist
 * @author cfl 2026/04/16
 */
@Data
@Accessors(chain = true)
@Schema(description = "SysAuthTokenBlacklistBO")
public class SysAuthTokenBlacklistBO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 认证token ID
     */
    @Schema(description = "认证token ID")
    private String id;

    /**
     * token内容
     */
    @Schema(description = "token内容")
    private String token;

    /**
     * 删除状态（0-未删除；>0删除时的时间戳）
     */
    @Schema(description = "删除状态（0-未删除；>0删除时的时间戳）")
    private Long deleted;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    /**
     * 最后更新时间
     */
    @Schema(description = "最后更新时间")
    private LocalDateTime updateTime;
}
