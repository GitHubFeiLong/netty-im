package com.feilong.im.service.impl;

import com.feilong.im.config.security.authentication.imuser.ImUserAuthenticationToken;
import com.feilong.im.config.security.authentication.imuser.ImUserDetails;
import com.feilong.im.config.security.authentication.sysuser.SysUserAuthenticationToken;
import com.feilong.im.config.security.authentication.sysuser.SysUserDetails;
import com.feilong.im.config.security.token.AuthenticationToken;
import com.feilong.im.config.security.token.TokenManager;
import com.feilong.im.constant.SecurityConstants;
import com.feilong.im.context.CurrentTimeContext;
import com.feilong.im.context.CurrentTokenContext;
import com.feilong.im.dto.AuthenticationTokenDTO;
import com.feilong.im.dto.AuthenticationUserDetailsDTO;
import com.feilong.im.dto.ImUserDTO;
import com.feilong.im.dto.form.ImSignInForm;
import com.feilong.im.dto.form.SysSignInForm;
import com.feilong.im.dto.req.ImSignUpReq;
import com.feilong.im.entity.ImUser;
import com.feilong.im.enums.status.ImUserStatusEnum;
import com.feilong.im.exception.ClientException;
import com.feilong.im.mapstruct.ImUserEntityMapper;
import com.feilong.im.service.ImUserService;
import com.feilong.im.service.AuthService;
import com.feilong.im.util.AssertUtil;
import com.feilong.im.util.CurrentUserUtil;
import com.feilong.im.util.SpringEnvUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * @author cfl 2026/04/13
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final ImUserService imUserService;
    private final AuthenticationManager authenticationManager;
    private final TokenManager tokenManager;
    private final PasswordEncoder passwordEncoder;
    private final ImUserEntityMapper imUserEntityMapper;
    private final HttpServletRequest request;
    private final HttpServletResponse response;

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
     * IM登录
     *
     * @param req 登录参数
     * @return 登录结果
     */
    @Override
    public AuthenticationTokenDTO imSignIn(ImSignInForm req) {
        log.info("IM登录");
        // 1. 创建用于IM认证的令牌（未认证）
        Authentication authenticationToken = new ImUserAuthenticationToken(req.getUsername().trim(), req.getPassword().trim());
        // 2. 执行认证（认证中）
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        // 3. 认证成功后生成 JWT 令牌，并存入 Security 上下文，供登录日志 AOP 使用（已认证）
        AuthenticationToken token = tokenManager.generateToken(authentication);

        // 记住我
        if (req.getRememberMe() != null && req.getRememberMe()) {
            // 7天有效期
            String refreshToken = tokenManager.generateToken(authentication, SecurityConstants.JWT_REFRESH_TOKEN_TTL);
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
     * 系统用户登录
     *
     * @param req 登录参数
     * @return 登录结果
     */
    @Override
    public AuthenticationTokenDTO sysSignIn(SysSignInForm req) {
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
            String refreshToken = tokenManager.generateToken(authentication, SecurityConstants.JWT_REFRESH_TOKEN_TTL);
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
     * 获取当前登录用户信息
     *
     * @return 用户信息
     */
    @Override
    public AuthenticationUserDetailsDTO getUserDetails() {
        Authentication authentication = CurrentUserUtil.getAuthentication();
        Object principal = authentication.getPrincipal();

        AuthenticationUserDetailsDTO dto = new AuthenticationUserDetailsDTO();
        if (principal instanceof ImUserDetails userDetails) {
            AuthenticationUserDetailsDTO.Im im = new AuthenticationUserDetailsDTO.Im()
                    .setId(userDetails.getId())
                    .setUsername(userDetails.getUsername())
                    .setNickname(userDetails.getNickname())
                    ;

            dto.setIm(im);
        } else if (principal instanceof SysUserDetails userDetails) {
            AuthenticationUserDetailsDTO.Sys sys = new AuthenticationUserDetailsDTO.Sys()
                    .setId(userDetails.getId())
                    .setUsername(userDetails.getUsername())
                    .setRoles(userDetails.getRoles())
                    ;
            dto.setSys(sys);
        }

        return dto;
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
     * 退出登录，将token置为无效
     *
     * @return 退出结果
     */
    @Override
    public Boolean signOut() {
        log.info("退出登录");
        tokenManager.invalidateToken(CurrentTokenContext.get());
        return true;
    }

    /**
     * 设置 RefreshToken Cookie
     *
     * @param refreshToken RefreshToken
     * @param refreshExpires 过期时间
     */
    private void setRefreshCookie(String refreshToken, LocalDateTime refreshExpires) {
        boolean isProd = SpringEnvUtil.isProd();
        // RefreshToken 写入 HttpOnly Cookie
        Cookie refreshCookie = new Cookie("refresh_token", refreshToken);
        if (isProd) {
            // 设为公共父域名（例如 .yourdomain.com）,如果前端是 admin.site.com, 后端是 api.site.com 那么父域名为 .site.com才能共享。
            refreshCookie.setDomain("");
            // 仅 HTTPS（生产环境必须, 开发测试非必须）
            refreshCookie.setSecure(isProd);
        }
        // JS 无法读取（防 XSS）
        refreshCookie.setHttpOnly(true);
        // 生产环境只允许刷新接口使用，其他环境允许所有接口使用（/api 是前端的代理前缀 前端请求刷新接口时的url是 http://192.168.31.1:3005/api/im/auth/refresh，那么需要允许的值是/api/im/auth/refresh）
        String path = isProd ? "/api" + SpringEnvUtil.getProperty("server.servlet.context-path") + "/auth/refresh" : "/";
        refreshCookie.setPath(path);
        // 过期时间 单位秒(浏览器显示 Cookie 过期时间使用的是 UTC 时间,所以会小8个小时)
        int maxAge = (int) Duration.between(CurrentTimeContext.get(), refreshExpires).getSeconds();
        refreshCookie.setMaxAge(maxAge);
        // 防 CSRF, 设置 SameSite 属性，生产环境必须是 "Strict"，开发测试环境可以设置为 "Lax" （设置为 “None” 时，Secure必须设置true）
        refreshCookie.setAttribute("SameSite", isProd ? "Strict" : "Lax");

        response.addCookie(refreshCookie);
    }
}
