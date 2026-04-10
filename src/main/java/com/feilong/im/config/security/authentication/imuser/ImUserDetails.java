package com.feilong.im.config.security.authentication.imuser;

import lombok.Data;

/**
 * ImUser认证信息
 * @author cfl 2026/04/10
 */
@Data
public class ImUserDetails {
    private Long id;

    private String username;

    private String nickname;
}
