package com.feilong.im.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 认证令牌响应对象DTO
 *
 * @author Ray.Hao
 * @since 0.0.1
 */
@Schema(description = "认证令牌响应对象")
@Data
@Builder
public class AuthenticationTokenDTO {

    @Schema(description = "令牌类型", example = "Bearer")
    private String tokenType;

    @Schema(description = "访问令牌")
    private String accessToken;

    @Schema(description = "访问令牌过期时间")
    private LocalDateTime accessExpires;
}
