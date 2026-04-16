package com.feilong.im.service.impl;

import com.feilong.im.config.security.authentication.imuser.ImUserAuthenticationToken;
import com.feilong.im.config.security.authentication.sysuser.SysUserAuthenticationToken;
import com.feilong.im.config.security.token.AuthenticationToken;
import com.feilong.im.config.security.token.TokenManager;
import com.feilong.im.context.CurrentTokenContext;
import com.feilong.im.dto.ImUserDTO;
import com.feilong.im.dto.form.SysLoginForm;
import com.feilong.im.dto.req.ImLoginReq;
import com.feilong.im.dto.req.ImSignUpReq;
import com.feilong.im.entity.ImUser;
import com.feilong.im.enums.status.ImUserStatusEnum;
import com.feilong.im.mapstruct.ImUserEntityMapper;
import com.feilong.im.service.ImUserService;
import com.feilong.im.service.LoginRegisterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    /**
     * IM登录
     *
     * @param req 登录参数
     * @return 登录结果
     */
    @Override
    public AuthenticationToken imLogin(ImLoginReq req) {
        log.info("IM登录");
        // 1. 创建用于IM认证的令牌（未认证）
        Authentication authenticationToken = new ImUserAuthenticationToken(req.getUsername().trim(), req.getPassword().trim());
        // 2. 执行认证（认证中）
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        // 3. 认证成功后生成 JWT 令牌，并存入 Security 上下文，供登录日志 AOP 使用（已认证）
        return tokenManager.generateToken(authentication);
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
    public AuthenticationToken sysLogin(SysLoginForm req) {
        log.info("SYS登录");
        // 1. 创建用于SYS认证的令牌（未认证）
        Authentication authenticationToken = new SysUserAuthenticationToken(req.getUsername().trim(), req.getPassword().trim());
        // 2. 执行认证（认证中）
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        // 3. 认证成功后生成 JWT 令牌，并存入 Security 上下文，供登录日志 AOP 使用（已认证）
        return tokenManager.generateToken(authentication);
    }
}
