package com.feilong.im.config.security.token;

import org.springframework.security.core.Authentication;

/**
 * Token 管理器
 * 用于生成、解析、校验、刷新 Token
 * @author cfl 2026/4/21
 */
public interface TokenManager {

    /**
     * 生成认证 Token
     *
     * @param authentication 用户认证信息
     * @return 认证 Token 响应
     */
    AuthenticationToken generateToken(Authentication authentication);

    /**
     * 生成 JWT Token
     *
     * @param authentication 认证信息
     * @param ttl           过期时间 ，单位秒, 小于0”永久“，大于0指定时间
     * @return JWT Token
     */
    String generateToken(Authentication authentication, int ttl);

    /**
     * 解析 Token 获取认证信息
     *
     * @param token  Token
     * @return 用户认证信息
     */
    Authentication parseToken(String token);

    /**
     * 校验 Token 是否有效
     *
     * @param token JWT Token
     * @return 是否有效
     */
    boolean validateToken(String token);

    /**
     * 令 Token 失效
     *
     * @param token Token
     */
    default void invalidateToken(String token) {
        // 默认实现可以是空的，或者抛出不支持的操作异常
        // throw new UnsupportedOperationException("Not implemented");
    }


}
