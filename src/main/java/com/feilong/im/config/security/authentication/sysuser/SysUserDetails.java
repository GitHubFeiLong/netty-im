package com.feilong.im.config.security.authentication.sysuser;

import lombok.Data;

import java.util.List;

/**
 * SysUser认证信息
 * @author cfl 2026/04/10
 */
@Data
public class SysUserDetails {
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
     * 角色列表
     */
    private List<String> roles;
}
