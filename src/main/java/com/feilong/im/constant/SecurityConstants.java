package com.feilong.im.constant;

/**
 * 安全模块常量
 *
 * @author Ray.Hao
 * @since 2023/11/24
 */
public interface SecurityConstants {
    /**
     * 登出路径后缀，拦截器不校验token是否有效
     */
    String SIGN_OUT_API_SUFFIX = "/sign-out";

    /**
     * 令牌模式
     */
    String TOKEN_MODEL_BEARER = "Bearer";

    /**
     * JWT Token 前缀
     */
    String BEARER_TOKEN_PREFIX  = "Bearer ";

    /**
     * 角色前缀，用于区分 authorities 角色和权限， ROLE_* 角色 、没有前缀的是权限
     */
    String ROLE_PREFIX = "ROLE_";

    /**
     * JWT 载荷字段 用户详情json的key
     */
    String JWT_KEY_USER_DETAIL = "jwtUserDetail";

    /**
     * JWT 载荷字段 jwtUserType
     */
    String JWT_KEY_JWT_USER_TYPE = "jwtUserType";

    /**
     * JWT 载荷字段 jwtUserType 的值，表示 IM 用户
     */
    String JWT_VALUE_JWT_USER_TYPE_IM_USER = "imUser";

    /**
     * JWT 载荷字段 jwtUserType 的值，表示 SYS 用户
     */
    String JWT_VALUE_JWT_USER_TYPE_SYS_USER = "sysUser";
}
