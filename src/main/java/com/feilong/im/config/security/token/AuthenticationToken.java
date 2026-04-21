package com.feilong.im.config.security.token;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 认证令牌响应对象
 * @author cfl 2026/4/21
 */
@Schema(description = "认证令牌响应对象")
@Data
@Builder
public class AuthenticationToken {

    @Schema(description = "令牌类型", example = "Bearer")
    private String tokenType;

    @Schema(description = "访问令牌")
    private String accessToken;

    @Schema(description = "刷新令牌")
    private String refreshToken;

    @Schema(description = "访问令牌过期时间")
    private LocalDateTime accessExpires;

    @Schema(description = "刷新令牌过期时间")
    private LocalDateTime refreshExpires;

}
