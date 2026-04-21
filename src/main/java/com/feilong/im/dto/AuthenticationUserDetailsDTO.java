package com.feilong.im.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 认证用户的信息
 * @author cfl 2026/4/21
 */
@Schema(description = "认证用户信息响应对象")
@Data
public class AuthenticationUserDetailsDTO {

    @Schema(description = "IM认证信息")
    private Im im;

    @Schema(description = "系统认证信息")
    private Sys sys;

    /**
     * Im登录用户信息
     * @author cfl 2026/4/21
     */
    @Data
    @Accessors(chain = true)
    public static class Im {
        @Schema(description = "用户ID")
        private Long id;

        @Schema(description = "用户名")
        private String username;

        @Schema(description = "昵称")
        private String nickname;
    }

    /**
     * 系统登录用户信息
     * @author cfl 2026/4/21
     */
    @Data
    @Accessors(chain = true)
    public static class Sys {
        @Schema(description = "用户ID")
        private Long id;

        @Schema(description = "用户名")
        private String username;

        @Schema(description = "角色列表")
        private List<String> roles;
    }
}
