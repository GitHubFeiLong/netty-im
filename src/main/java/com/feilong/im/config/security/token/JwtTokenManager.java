package com.feilong.im.config.security.token;

import cn.hutool.core.collection.CollectionUtil;
import com.feilong.im.config.security.authentication.imuser.ImUserAuthenticationToken;
import com.feilong.im.config.security.authentication.imuser.ImUserDetails;
import com.feilong.im.config.security.authentication.sysuser.SysUserAuthenticationToken;
import com.feilong.im.config.security.authentication.sysuser.SysUserDetails;
import com.feilong.im.constant.RedisKeyConst;
import com.feilong.im.constant.SecurityConstants;
import com.feilong.im.context.CurrentTimeContext;
import com.feilong.im.enums.status.AuthTokenStatusEnum;
import com.feilong.im.exception.ClientException;
import com.feilong.im.properties.SecurityProperties;
import com.feilong.im.util.JsonUtil;
import com.feilong.im.util.StringUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * JWT Token 管理器
 * @author cfl 2026/4/15
 */
@Slf4j
@Component
@ConditionalOnProperty(value = "security.session.type", havingValue = "jwt")
public class JwtTokenManager implements TokenManager {

    private final SecretKey secretKey;
    private final SecurityProperties securityProperties;
    private final StringRedisTemplate stringRedisTemplate;

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
     * 解析令牌
     * @param token JWT Token
     * @return Claims
     */
    public Claims parseTokenToClaims(String token) {
        if (token.startsWith(SecurityConstants.BEARER_TOKEN_PREFIX)) {
            token = token.substring(SecurityConstants.BEARER_TOKEN_PREFIX.length());
        }
        return Jwts.parser()
                // 设置验证密钥
                .verifyWith(secretKey)
                .build()
                // 解析并验证
                .parseSignedClaims(token)
                // 获取载荷
                .getPayload();
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
        // 判断用户类型
        Object jwtUserType = claims.get(SecurityConstants.JWT_KEY_JWT_USER_TYPE);

        // IM 用户类型
        if (Objects.equals(jwtUserType, SecurityConstants.JWT_VALUE_JWT_USER_TYPE_IM_USER)) {
            ImUserDetails imUserDetails = JsonUtil.toObject(claims.get(SecurityConstants.JWT_KEY_USER_DETAIL).toString(), ImUserDetails.class);
            imUserDetails.setTokenId(jwtId);
            return new ImUserAuthenticationToken(imUserDetails,null);
        }

        // SYS 用户类型
        if (Objects.equals(jwtUserType, SecurityConstants.JWT_VALUE_JWT_USER_TYPE_SYS_USER)) {
            SysUserDetails sysUserDetails = JsonUtil.toObject(claims.get(SecurityConstants.JWT_KEY_USER_DETAIL).toString(), SysUserDetails.class);
            sysUserDetails.setTokenId(jwtId);
            if (CollectionUtil.isNotEmpty(sysUserDetails.getRoles())) {
                List<SimpleGrantedAuthority> authorities = sysUserDetails.getRoles().stream().map(role -> new SimpleGrantedAuthority(SecurityConstants.ROLE_PREFIX + role)).toList();
                return new SysUserAuthenticationToken(sysUserDetails, authorities);
            }
            return new SysUserAuthenticationToken(sysUserDetails, List.of());
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
        Claims claims = parseTokenToClaims(token);;

        String tokenId = claims.getId();
        log.debug("校验tokenId: {} 是否有效", tokenId);
        String key = RedisKeyConst.getKey(RedisKeyConst.AUTH_TOKEN_STATUS_KEY, tokenId);
        if (stringRedisTemplate.hasKey(key)) {
            log.debug("redis中存在token id信息");
            String status = stringRedisTemplate.opsForValue().get(key);
            if (Objects.equals(status, AuthTokenStatusEnum.VALID.getId())) {
                log.debug("token有效");
                return true;
            }
            log.debug("token无效");
            throw ClientException.of("登录失效，请重新登录", claims.getId());
        }
        log.debug("redis中不存在token id信息");
        synchronized (this) {
            if (stringRedisTemplate.hasKey(key)) {
                log.debug("redis中存在token id信息");
                String status = stringRedisTemplate.opsForValue().get(key);
                if (Objects.equals(status, AuthTokenStatusEnum.VALID.getId())) {
                    log.debug("token有效");
                    return true;
                }
                log.debug("token无效");
                throw ClientException.of("登录失效，请重新登录", claims.getId());
            }

            // boolean exists = sysAuthTokenBlacklistService.lambdaQuery().select(SysAuthTokenBlacklist::getId).eq(SysAuthTokenBlacklist::getId, claims.getId()).exists();
            boolean exists = true;

            AuthTokenStatusEnum authTokenStatusEnum = exists ? AuthTokenStatusEnum.INVALID : AuthTokenStatusEnum.VALID;
            // 设置缓存
            stringRedisTemplate.opsForValue().set(key, authTokenStatusEnum.getId(), 2, TimeUnit.HOURS);
            if (exists) {
                log.debug("token无效");
                throw ClientException.of("登录失效，请重新登录", claims.getId());
            }
        }
        return true;
    }

    /**
     * 将令牌加入黑名单
     *
     * @param token JWT Token
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void invalidateToken(String token) {
        if (StringUtil.isBlank(token)) {
            log.warn("token为空");
            return;
        }
        Claims claims = parseTokenToClaims(token);
        String tokenId = claims.getId();

        Date expiration = claims.getExpiration();
        if (expiration != null) {
            if (CurrentTimeContext.getDate().after(expiration)) {
                // Token已过期，直接返回
                return;
            }
        }

        // 黑名单Token Key
        String key = RedisKeyConst.getKey(RedisKeyConst.AUTH_TOKEN_STATUS_KEY, tokenId);
        if (stringRedisTemplate.hasKey(key)) {
            // 本身无效，就不需要再设置无效
            if (Objects.equals(stringRedisTemplate.opsForValue().get(key), AuthTokenStatusEnum.INVALID.getId())) {
                return;
            }
        }

        synchronized (this) {
            if (stringRedisTemplate.hasKey(key)) {
                // 本身无效，就不需要再设置无效
                if (Objects.equals(stringRedisTemplate.opsForValue().get(key), AuthTokenStatusEnum.INVALID.getId())) {
                    return;
                }
            }

            // 保存
            // sysAuthTokenBlacklistService.save(tokenId, token);
            // 设置缓存
            stringRedisTemplate.opsForValue().set(key, AuthTokenStatusEnum.INVALID.getId(), 2, TimeUnit.HOURS);
        }
    }

    /**
     * 生成 JWT Token
     *
     * @param authentication 认证信息
     * @param ttl           过期时间 ，单位秒, 小于0”永久“，大于0指定时间
     * @return JWT Token
     */
    @Override
    public String generateToken(Authentication authentication, int ttl) {
        Map<String, Object> payload = new HashMap<>();
        Object principal = authentication.getPrincipal();
        String tokenId = UUID.randomUUID().toString().replace("-", "");

        String subject = principal.toString();
        if (principal instanceof ImUserDetails userDetails) {
            subject = SecurityConstants.JWT_VALUE_JWT_USER_TYPE_IM_USER + "_" + userDetails.getId();
            userDetails.setTokenId(tokenId);
            payload.put(SecurityConstants.JWT_KEY_USER_DETAIL, JsonUtil.toJsonString(userDetails));
            payload.put(SecurityConstants.JWT_KEY_JWT_USER_TYPE, SecurityConstants.JWT_VALUE_JWT_USER_TYPE_IM_USER);
        } else if (principal instanceof SysUserDetails userDetails) {
            subject = SecurityConstants.JWT_VALUE_JWT_USER_TYPE_SYS_USER + "_" + userDetails.getId();
            userDetails.setTokenId(tokenId);
            payload.put(SecurityConstants.JWT_KEY_USER_DETAIL, JsonUtil.toJsonString(userDetails));
            payload.put(SecurityConstants.JWT_KEY_JWT_USER_TYPE, SecurityConstants.JWT_VALUE_JWT_USER_TYPE_SYS_USER);
        }

        // 过期时间
        Date expiration;
        if (ttl <= 0) {
            // 表示永不过期
            expiration = new Date(System.currentTimeMillis() + 36500L * 24 * 60 * 60 * 1000);
        } else {
            expiration = new Date(System.currentTimeMillis() + ttl * 1000L);
        }

        return Jwts.builder()
                .claims(payload)
                .subject(subject)
                .id(tokenId)
                .issuedAt(new Date())
                .expiration(expiration)
                .signWith(secretKey)
                .compact();
    }
}
