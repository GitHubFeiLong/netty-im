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
 * 用户好友表 PageQuery
 * @see com.feilong.im.entity.ImFriend
 * @author cfl 2026/04/16
 */
@Data
@ToString(callSuper=true)
@EqualsAndHashCode(callSuper=true)
@Schema(description = "ImFriendPageQuery")
public class ImFriendPageQuery extends BasePageQuery implements Serializable {

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
