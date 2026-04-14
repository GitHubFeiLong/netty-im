package com.feilong.im.config.security.authentication.imuser;

import lombok.Data;

/**
 * ImUser认证信息
 * @author cfl 2026/04/10
 */
@Data
public class ImUserDetails {
    /**
     * tokenId
     */
    private String tokenId;

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;
}
