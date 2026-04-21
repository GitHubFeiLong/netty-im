package com.feilong.im.dto.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 系统用户登录表单
 * @author cfl 2026/04/16
 */
@Data
@Schema(description = "系统用户登录表单")
public class SysSignInForm {

    @Schema(description="用户名", example="admin", requiredMode=Schema.RequiredMode.REQUIRED)
    @Size(min = 3, max = 20, message = "用户名长度必须在3-20个字符之间")
    private String username;

    @Schema(description="密码", example = "123456", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 30, message = "密码长度必须在6-30个字符之间")
    private String password;

    @Schema(description="记住我，默认false", example = "true")
    private Boolean rememberMe = false;
}
