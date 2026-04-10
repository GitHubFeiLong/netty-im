package com.feilong.im.config.security.token;

import com.feilong.im.config.security.authentication.imuser.ImUserAuthenticationToken;
import com.feilong.im.config.security.authentication.imuser.ImUserDetails;
import com.feilong.im.constant.RedisKeyConst;
import com.feilong.im.constant.SecurityConstants;
import com.feilong.im.context.CurrentTimeContext;
import com.feilong.im.exception.ClientException;
import com.feilong.im.properties.SecurityProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * JWT Token 管理器
 * <p>
 * 用于生成、解析、校验、刷新 JWT Token
 *
 * @author Ray.Hao
 * @since 2024/11/15
 */
@ConditionalOnProperty(value = "security.session.type", havingValue = "jwt")
@Service
public class JwtTokenManager implements TokenManager {

    private final SecurityProperties securityProperties;
    private final StringRedisTemplate stringRedisTemplate;
    private final SecretKey secretKey;

    public JwtTokenManager(SecurityProperties securityProperties, StringRedisTemplate stringRedisTemplate) {
        this.securityProperties = securityProperties;
        this.stringRedisTemplate = stringRedisTemplate;
        this.secretKey = Keys.hmacShaKeyFor(securityProperties.getSession().getJwt().getSecretKey().getBytes());
    }

    /**
     * 生成令牌
     *
     * @param authentication 认证信息
     * @return 令牌响应对象
     */
    @Override
    public AuthenticationToken generateToken(Authentication authentication) {
        int accessTokenTimeToLive = securityProperties.getSession().getAccessTokenTimeToLive();
        int refreshTokenTimeToLive = securityProperties.getSession().getRefreshTokenTimeToLive();

        String newAccessToken = generateToken(authentication, accessTokenTimeToLive);
        String refreshToken = generateToken(authentication, refreshTokenTimeToLive);

        return AuthenticationToken.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .tokenType(SecurityConstants.TOKEN_MODEL_BEARER)
                .accessExpires(CurrentTimeContext.get().plusSeconds(accessTokenTimeToLive))
                .refreshExpires(CurrentTimeContext.get().plusSeconds(refreshTokenTimeToLive))
                .build();
    }

    /**
     *
     * @param token
     * @return
     */
    public Claims parseTokenToClaims(String token) {
        if (token.startsWith(SecurityConstants.BEARER_TOKEN_PREFIX)) {
            token = token.substring(SecurityConstants.BEARER_TOKEN_PREFIX.length());
        }
        return Jwts.parser()
                .verifyWith(secretKey)  // 设置验证密钥
                .build()
                .parseSignedClaims(token) // 解析并验证
                .getPayload();            // 获取载荷
    }

    /**
     * 解析令牌
     *
     * @param token JWT Token
     * @return Authentication 对象
     */
    @Override
    public Authentication parseToken(String token) {
        Claims claims = parseTokenToClaims(token);

        String jwtId = claims.getId();

        Object jwtUserType = claims.get(SecurityConstants.JWT_KEY_JWT_USER_TYPE);

        if (Objects.equals(jwtUserType, SecurityConstants.JWT_VALUE_JWT_USER_TYPE_IM_USER)) {
            ImUserDetails imUserDetails = new ImUserDetails();
            imUserDetails.setId((Long)claims.get(SecurityConstants.JWT_KEY_ID));
            imUserDetails.setUsername((String)claims.get(SecurityConstants.JWT_KEY_USERNAME));
            imUserDetails.setNickname((String)claims.get(SecurityConstants.JWT_KEY_NICKNAME));

            claims.get(SecurityConstants.JWT_KEY_JWT_USER_TYPE);

            return new ImUserAuthenticationToken(imUserDetails,null);
        }

        throw new RuntimeException("认证方式错误");
    }

    /**
     * 校验令牌
     *
     * @param token JWT Token
     * @return 是否有效
     */
    @Override
    public boolean validateToken(String token) {
        Claims claims = parseTokenToClaims(token);
        // 校验黑名单
        if (stringRedisTemplate.hasKey(RedisKeyConst.getKey(RedisKeyConst.JWT_TOKEN_BLACKLIST, claims.getId()))) {
            throw ClientException.of("Token %s 已被加入黑名单", claims.getId());
        }
        return true;
    }

    @Override
    public boolean validateRefreshToken(String refreshToken) {
        return this.validateToken(refreshToken);
    }

    /**
     * 将令牌加入黑名单
     *
     * @param token JWT Token
     */
    @Override
    public void invalidateToken(String token) {
        Claims claims = parseTokenToClaims(token);
        String jwtId = claims.getId();

        // 黑名单Token Key
        String key = RedisKeyConst.getKey(RedisKeyConst.JWT_TOKEN_BLACKLIST, jwtId);
        if (stringRedisTemplate.hasKey(key)) {
            return;
        }

        Date expiration = claims.getExpiration();
        if (expiration != null) {
            if (CurrentTimeContext.getDate().after(expiration)) {
                // Token已过期，直接返回
                return;
            }
        }
        // 将其加入黑名单
        stringRedisTemplate.opsForValue().set(key, "", 3600, TimeUnit.SECONDS);
    }

    /**
     * 刷新令牌
     *
     * @param refreshToken 刷新令牌
     * @return 令牌响应对象
     */
    @Override
    public AuthenticationToken refreshToken(String refreshToken) {

        // boolean isValid = validateToken(refreshToken);
        // if (!isValid) {
        //     throw new BusinessException(ResultCode.REFRESH_TOKEN_INVALID);
        // }
        //
        // Authentication authentication = parseToken(refreshToken);
        // int accessTokenTimeToLive = securityProperties.getSession().getAccessTokenTimeToLive();
        // int refreshTokenTimeToLive = securityProperties.getSession().getRefreshTokenTimeToLive();
        // String newAccessToken = generateToken(authentication, accessTokenTimeToLive);
        // refreshToken = generateToken(authentication, refreshTokenTimeToLive);
        //
        // return AuthenticationToken.builder()
        //         .accessToken(newAccessToken)
        //         .refreshToken(refreshToken)
        //         .tokenType(CommonConst.TOKEN_MODEL_BEARER)
        //         .accessExpires(CurrentDateTimeUtil.get().plusSeconds(accessTokenTimeToLive))
        //         .refreshExpires(CurrentDateTimeUtil.get().plusSeconds(refreshTokenTimeToLive))
        //         .build();

        return null;
    }

    /**
     * 生成 JWT Token
     *
     * @param authentication 认证信息
     * @param ttl           过期时间, 小于0”永久“，大于0指定时间
     * @return JWT Token
     */
    private String generateToken(Authentication authentication, int ttl) {
        Map<String, Object> payload = new HashMap<>();
        Object principal = authentication.getPrincipal();
        if (principal instanceof ImUserDetails imUserDetails) {
            payload.put(SecurityConstants.JWT_KEY_ID, imUserDetails.getId());
            payload.put(SecurityConstants.JWT_KEY_USERNAME, imUserDetails.getUsername());
            payload.put(SecurityConstants.JWT_KEY_NICKNAME, imUserDetails.getNickname());
            payload.put(SecurityConstants.JWT_KEY_JWT_USER_TYPE, SecurityConstants.JWT_VALUE_JWT_USER_TYPE_IM_USER);
        } else {

        }

        // 过期时间
        Date expiration;
        if (ttl <= 0) {
            // 表示永不过期
            expiration = new Date(CurrentTimeContext.getTimestamp() + 36500L * 24 * 60 * 60 * 1000);
        } else {
            expiration = new Date(CurrentTimeContext.getTimestamp() + ttl * 1000L);
        }

        return Jwts.builder()
                .claims(payload)
                .subject(authentication.getName())
                .id(UUID.randomUUID().toString())
                .issuedAt(CurrentTimeContext.getDate())
                .expiration(expiration)
                .signWith(secretKey)
                .compact();
    }
}
