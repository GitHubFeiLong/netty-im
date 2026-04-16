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
 * 认证token 黑名单 PageQuery
 * @see com.feilong.im.entity.SysAuthTokenBlacklist
 * @author cfl 2026/04/16
 */
@Data
@ToString(callSuper=true)
@EqualsAndHashCode(callSuper=true)
@Schema(description = "SysAuthTokenBlacklistPageQuery")
public class SysAuthTokenBlacklistPageQuery extends BasePageQuery implements Serializable {

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
