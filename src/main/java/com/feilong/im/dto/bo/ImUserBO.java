package com.feilong.im.dto.bo;

import java.time.LocalDateTime;

import java.io.Serializable;
import java.io.Serial;
import lombok.Data;
import lombok.experimental.Accessors;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * im账户表 BO
 * @see com.feilong.im.entity.ImUser
 * @author cfl 2026/04/16
 */
@Data
@Accessors(chain = true)
@Schema(description = "ImUserBO")
public class ImUserBO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户唯一ID
     */
    @Schema(description = "用户唯一ID")
    private Long id;

    /**
     * 用户名
     */
    @Schema(description = "用户名")
    private String username;

    /**
     * 密码
     */
    @Schema(description = "密码")
    private String password;

    /**
     * 昵称
     */
    @Schema(description = "昵称")
    private String nickname;

    /**
     * 头像URL
     */
    @Schema(description = "头像URL")
    private String avatar;

    /**
     * 用户状态（0-离线，1-在线，2-忙碌，3-隐身），默认0
     */
    @Schema(description = "用户状态（0-离线，1-在线，2-忙碌，3-隐身），默认0")
    private Byte status;

    /**
     * 版本号
     */
    @Schema(description = "版本号")
    private Integer version;

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
