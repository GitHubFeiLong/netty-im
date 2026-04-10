package com.feilong.im.config.security;// package com.feilong.acapi.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.feilong.im.core.Result;
import com.feilong.im.exception.ClientException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;


/**
 * 自定义权限不足，访问被拒绝处理器
 * @author cfl 2026/4/10
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    /**
     * 请求被拒绝处理方法
     * @param httpServletRequest    请求对象
     * @param httpServletResponse   响应对象
     * @param e                     访问拒绝异常对象
     */
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException {
        ClientException clientException = ClientException.of(e, "访问被拒绝");
        clientException.setStatus(HttpStatus.FORBIDDEN.value());
        clientException.setCode(String.valueOf(HttpStatus.FORBIDDEN.value()));
        Result<Object> result = Result.ofFail(clientException);
        httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        String json = objectMapper.writeValueAsString(result);
        httpServletResponse.getWriter().write(json);
    }
}
