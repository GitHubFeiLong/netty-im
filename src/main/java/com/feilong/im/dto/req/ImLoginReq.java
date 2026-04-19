package com.feilong.im.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @author cfl 2026/04/13
 */
@Data
@Schema(description = "IM登录请求")
public class ImLoginReq {

    @Schema(description="用户名", example="admin", requiredMode=Schema.RequiredMode.REQUIRED)
    @Size(min = 2, max = 20, message = "用户名长度必须在3-20个字符之间")
    private String username;

    @Schema(description="密码", example = "123456", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 30, message = "密码长度必须在6-30个字符之间")
    private String password;
}
