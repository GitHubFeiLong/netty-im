package com.feilong.im.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.feilong.im.core.Result;
import com.feilong.im.exception.BasicException;
import com.feilong.im.exception.ClientException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;


/**
 * 统一处理 Spring Security 认证失败响应
 * @author cfl 2026/4/10
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // 打印错误日志
        BasicException basicException = ClientException.of(authException, "请登录认证")
                .setCode(String.valueOf(HttpStatus.UNAUTHORIZED.value()))
                .setStatus(HttpStatus.UNAUTHORIZED.value())
                ;

        Result<BasicException> result =  Result.ofFail(basicException);
        switch (authException) {
            case UsernameNotFoundException c -> result.setClientMessage("用户名或密码错误");
            case BadCredentialsException c -> result.setClientMessage("用户名或密码错误");
            case AccountExpiredException c -> result.setClientMessage("用户已失效");
            case DisabledException c -> result.setClientMessage("用户未激活");
            case LockedException c -> result.setClientMessage("用户已锁定");
            case InsufficientAuthenticationException c -> result.setClientMessage("认证信息不足，请重新登录");
            default -> result.clientMessage(authException.getMessage());
        }

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader(HttpHeaders.WWW_AUTHENTICATE, "Bearer realm=\"API Realm\"");
        String json = objectMapper.writeValueAsString(result);
        response.getWriter().write(json);
    }

}
