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
 * 系统用户 PageQuery
 * @see com.feilong.im.entity.SysUser
 * @author cfl 2026/04/16
 */
@Data
@ToString(callSuper=true)
@EqualsAndHashCode(callSuper=true)
@Schema(description = "SysUserPageQuery")
public class SysUserPageQuery extends BasePageQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 系统用户ID
     */
    @Schema(description = "系统用户ID")
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
     * 用户角色列表，逗号分隔（如：ADMIN,USER）
     */
    @Schema(description = "用户角色列表，逗号分隔（如：ADMIN,USER）")
    private String roles;

    /**
     * 用户头像
     */
    @Schema(description = "用户头像")
    private String avatar;

    /**
     * 昵称
     */
    @Schema(description = "昵称")
    private String nickname;

    /**
     * 联系方式
     */
    @Schema(description = "联系方式")
    private String mobile;

    /**
     * 用户邮箱
     */
    @Schema(description = "用户邮箱")
    private String email;

    /**
     * 性别(1-男 2-女 0-保密)
     */
    @Schema(description = "性别(1-男 2-女 0-保密)")
    private Boolean gender;

    /**
     * 过期时间，不填永久有效
     */
    @Schema(description = "过期时间，不填永久有效")
    private LocalDateTime validTime;

    /**
     * 激活状态：true 激活；false 未激活
     */
    @Schema(description = "激活状态：true 激活；false 未激活")
    private Boolean enabled;

    /**
     * 锁定状态：true 锁定；false 未锁定
     */
    @Schema(description = "锁定状态：true 锁定；false 未锁定")
    private Boolean locked;

    /**
     * 最近登录时间
     */
    @Schema(description = "最近登录时间")
    private LocalDateTime lastLoginTime;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;

    /**
     * 版本号
     */
    @Schema(description = "版本号")
    private Integer version;

    /**
     * 删除状态
     */
    @Schema(description = "删除状态")
    private Boolean deleted;

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
