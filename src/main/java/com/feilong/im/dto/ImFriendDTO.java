package com.feilong.im.dto;

import java.time.LocalDateTime;

import java.io.Serializable;
import java.io.Serial;
import lombok.Data;
import lombok.experimental.Accessors;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 用户好友表 DTO
 * @see com.feilong.im.entity.ImFriend
 * @author cfl 2026/04/16
 */
@Data
@Accessors(chain = true)
@Schema(description = "ImFriendDTO")
public class ImFriendDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "")
    private Long id;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    private Long userId;

    /**
     * 好友ID
     */
    @Schema(description = "好友ID")
    private Long friendId;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
