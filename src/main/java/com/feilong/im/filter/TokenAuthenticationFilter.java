package com.feilong.im.filter;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.feilong.im.config.security.token.TokenManager;
import com.feilong.im.constant.SecurityConstants;
import com.feilong.im.context.CurrentTimeContext;
import com.feilong.im.context.CurrentTokenContext;
import com.feilong.im.exception.BasicException;
import com.feilong.im.exception.ClientException;
import com.feilong.im.exception.ServerException;
import com.feilong.im.util.ResponseUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author cfl 2026/04/10
 */
@Slf4j
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    /**
     * Token 管理器
     */
    private final TokenManager tokenManager;

    /**
     * 校验 Token ，包括验签和是否过期
     * 如果 Token 有效，将 Token 解析为 Authentication 对象，并设置到 Spring Security 上下文中
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws IOException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        try {
            CurrentTimeContext.set();
            if (StrUtil.isNotBlank(authorizationHeader) && authorizationHeader.startsWith(SecurityConstants.BEARER_TOKEN_PREFIX)) {

                // 剥离Bearer前缀获取原始令牌
                String rawToken = authorizationHeader.substring(SecurityConstants.BEARER_TOKEN_PREFIX.length());
                CurrentTokenContext.set(rawToken);

                // 执行令牌有效性检查（包含密码学验签和过期时间验证）
                tokenManager.validateToken(rawToken);

                // 将令牌解析为 Spring Security 上下文认证对象
                Authentication authentication = tokenManager.parseToken(rawToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            // 继续后续过滤器链执行
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            log.error("Token 已过期", e);
            ResponseUtil.writeErrorResponse(response, e,"Token 已过期", HttpStatus.UNAUTHORIZED);
        } catch (BasicException e) {
            log.error("BasicException：{}", e.getMessage(), e);
            ResponseUtil.writeErrorResponse(response, e);
        } catch (Exception ex) {
            log.error("token认证失败：{}", ex.getMessage(), ex);
            ResponseUtil.writeErrorResponse(response, ex,"token认证失败", HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            // 安全上下文清除保障（防止上下文残留）
            SecurityContextHolder.clearContext();
            CurrentTokenContext.remove();
            CurrentTimeContext.remove();
        }
    }

}
