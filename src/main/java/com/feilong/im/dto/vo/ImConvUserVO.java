package com.feilong.im.dto.vo;

import java.time.LocalDateTime;

import java.io.Serializable;
import java.io.Serial;
import lombok.Data;
import lombok.experimental.Accessors;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * im用户会话表 VO
 * @see com.feilong.im.entity.ImConvUser
 * @author cfl 2026/04/16
 */
@Data
@Accessors(chain = true)
@Schema(description = "ImConvUserVO")
public class ImConvUserVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "")
    private Long id;

    /**
     * 会话ID
     */
    @Schema(description = "会话ID")
    private Long convId;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    private Long userId;

    /**
     * 该用户在此会话的未读消息数（初始0，新消息+1，阅读后清零）
     */
    @Schema(description = "该用户在此会话的未读消息数（初始0，新消息+1，阅读后清零）")
    private Integer unreadCount;

    /**
     * 该用户最后阅读的消息ID（用于定位未读起点）
     */
    @Schema(description = "该用户最后阅读的消息ID（用于定位未读起点）")
    private Long lastReadId;

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
