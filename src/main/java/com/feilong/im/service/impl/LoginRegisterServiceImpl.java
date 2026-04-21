package com.feilong.im.service.impl;

import com.feilong.im.config.security.authentication.imuser.ImUserAuthenticationToken;
import com.feilong.im.config.security.authentication.sysuser.SysUserAuthenticationToken;
import com.feilong.im.config.security.token.AuthenticationToken;
import com.feilong.im.config.security.token.TokenManager;
import com.feilong.im.context.CurrentTimeContext;
import com.feilong.im.context.CurrentTokenContext;
import com.feilong.im.dto.AuthenticationTokenDTO;
import com.feilong.im.dto.ImUserDTO;
import com.feilong.im.dto.form.SysLoginForm;
import com.feilong.im.dto.req.ImLoginReq;
import com.feilong.im.dto.req.ImSignUpReq;
import com.feilong.im.entity.ImUser;
import com.feilong.im.enums.status.ImUserStatusEnum;
import com.feilong.im.exception.ClientException;
import com.feilong.im.mapstruct.ImUserEntityMapper;
import com.feilong.im.service.ImUserService;
import com.feilong.im.service.LoginRegisterService;
import com.feilong.im.util.AssertUtil;
import com.feilong.im.util.CurrentUserUtil;
import com.feilong.im.util.SpringBeanUtil;
import com.feilong.im.util.SpringEnvUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;

/**
 * @author cfl 2026/04/13
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LoginRegisterServiceImpl implements LoginRegisterService {

    private final ImUserService imUserService;
    private final AuthenticationManager authenticationManager;
    private final TokenManager tokenManager;
    private final PasswordEncoder passwordEncoder;
    private final ImUserEntityMapper imUserEntityMapper;
    private final HttpServletRequest request;
    private final HttpServletResponse response;

    /**
     * IM登录
     *
     * @param req 登录参数
     * @return 登录结果
     */
    @Override
    public AuthenticationTokenDTO imLogin(ImLoginReq req) {
        log.info("IM登录");
        // 1. 创建用于IM认证的令牌（未认证）
        Authentication authenticationToken = new ImUserAuthenticationToken(req.getUsername().trim(), req.getPassword().trim());
        // 2. 执行认证（认证中）
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        // 3. 认证成功后生成 JWT 令牌，并存入 Security 上下文，供登录日志 AOP 使用（已认证）
        AuthenticationToken token = tokenManager.generateToken(authentication);

        setRefreshCookie(token.getRefreshToken(), token.getRefreshExpires());
        return AuthenticationTokenDTO.builder()
                .tokenType(token.getTokenType())
                .accessToken(token.getAccessToken())
                .accessExpires(token.getAccessExpires())
                .build();
    }

    /**
     * IM注册
     *
     * @param req 注册参数
     * @return 注册结果
     */
    @Override
    public ImUserDTO imSignUp(ImSignUpReq req) {
        log.info("用户注册：{}", req);
        Long count = imUserService.lambdaQuery().eq(ImUser::getUsername, req.getUsername()).count();
        if (count > 0) {
            throw new IllegalArgumentException("用户已存在，请直接进行登录");
        }
        ImUser imUser = new ImUser();
        imUser.setUsername(req.getUsername());
        imUser.setPassword(passwordEncoder.encode(req.getPassword()));
        imUser.setStatus(ImUserStatusEnum.OFFLINE.getId());
        imUser.setDeleted(0L);

        imUserService.save(imUser);
        return imUserEntityMapper.toDto(imUser);
    }

    /**
     * 退出登录，将token置为无效
     *
     * @return 退出结果
     */
    @Override
    public Boolean logout() {
        log.info("退出登录");
        tokenManager.invalidateToken(CurrentTokenContext.get());
        return true;
    }

    /**
     * 系统用户登录
     *
     * @param req 登录参数
     * @return 登录结果
     */
    @Override
    public AuthenticationTokenDTO sysLogin(SysLoginForm req) {
        log.info("SYS登录");
        // 1. 创建用于SYS认证的令牌（未认证）
        Authentication authenticationToken = new SysUserAuthenticationToken(req.getUsername().trim(), req.getPassword().trim());
        // 2. 执行认证（认证中）
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        // 3. 认证成功后生成 JWT 令牌，并存入 Security 上下文，供登录日志 AOP 使用（已认证）
        AuthenticationToken token = tokenManager.generateToken(authentication);

        // 记住我
        if (req.getRememberMe() != null && req.getRememberMe()) {
            // 7天有效期
            String refreshToken = tokenManager.generateToken(authentication, 7 * 24 * 60 * 60);
            token.setRefreshToken(refreshToken);
            token.setRefreshExpires(LocalDateTime.now().plusDays(7));
        }

        setRefreshCookie(token.getRefreshToken(), token.getRefreshExpires());
        return AuthenticationTokenDTO.builder()
                .tokenType(token.getTokenType())
                .accessToken(token.getAccessToken())
                .accessExpires(token.getAccessExpires())
                .build();
    }

    /**
     * 刷新TOKEN
     *
     * @return 刷新结果
     */
    @Override
    public AuthenticationTokenDTO refresh() {

        // 1. 从 Cookie 中取 RefreshToken
        String refreshToken = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refresh_token".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        AssertUtil.isNotBlank(refreshToken, () -> ClientException.of(HttpStatus.UNAUTHORIZED, "RefreshToken 未找到"));

        // 2. 校验 RefreshToken
        boolean validated = tokenManager.validateToken(refreshToken);
        AssertUtil.isTrue(validated, () -> ClientException.of(HttpStatus.UNAUTHORIZED, "RefreshToken 已失效"));

        Authentication authentication = tokenManager.parseToken(refreshToken);
        AuthenticationToken token = tokenManager.generateToken(authentication);

        setRefreshCookie(token.getRefreshToken(), token.getRefreshExpires());
        return AuthenticationTokenDTO.builder()
                .tokenType(token.getTokenType())
                .accessToken(token.getAccessToken())
                .accessExpires(token.getAccessExpires())
                .build();
    }

    /**
     * 设置 RefreshToken Cookie
     *
     * @param refreshToken RefreshToken
     * @param refreshExpires 过期时间
     */
    private void setRefreshCookie(String refreshToken, LocalDateTime refreshExpires) {
        // 3. ✅ RefreshToken 写入 HttpOnly Cookie
        Cookie refreshCookie = new Cookie("refresh_token", refreshToken);
        // JS 无法读取（防 XSS）
        refreshCookie.setHttpOnly(true);
        // 仅 HTTPS（生产环境必须）
        refreshCookie.setSecure(SpringEnvUtil.isProd());
        // 只允许刷新接口使用
        refreshCookie.setPath("/**/auth/refresh");
        // 过期时间
        refreshCookie.setMaxAge((int)(refreshExpires.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() - CurrentTimeContext.getTimestamp()));
        // 防 CSRF
        refreshCookie.setAttribute("SameSite", "Strict");

        response.addCookie(refreshCookie);
    }
}
