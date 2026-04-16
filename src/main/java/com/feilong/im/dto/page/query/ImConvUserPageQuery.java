package com.feilong.im.dto.page.query;

import java.time.LocalDateTime;
import com.feilong.im.dto.page.query.BasePageQuery;
import java.time.LocalTime;

import java.io.Serializable;
import java.io.Serial;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * im用户会话表 PageQuery
 * @see com.feilong.im.entity.ImConvUser
 * @author cfl 2026/04/16
 */
@Data
@ToString(callSuper=true)
@EqualsAndHashCode(callSuper=true)
@Schema(description = "ImConvUserPageQuery")
public class ImConvUserPageQuery extends BasePageQuery implements Serializable {

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

    @Schema(description = "分页开始时间")
    private LocalDateTime startTime;

    @Schema(description = "分页结束时间")
    private LocalDateTime endTime;

    /**
     * 获取开始时间
     *
     * @return LocalDateTime 当日0点
     */
    public LocalDateTime getStartTime() {
        return startTime == null ? null : startTime.toLocalDate().atStartOfDay();
    }

    /**
     * 获取截止时间
     *
     * @return LocalDateTime 当日最晚点
     */
    public LocalDateTime getEndTime() {
        return endTime == null ? null : endTime.toLocalDate().atTime(LocalTime.MAX);
    }
}
