package com.feilong.im.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 用户注册请求
 * @author cfl 2026/04/09
 */
@Data
public class ImUserSignUpReq {

    /**
     * 用户名
     */
    @NotBlank
    private String username;

    /**
     * 密码
     */
    @NotBlank
    private String password;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String avatar;
}
