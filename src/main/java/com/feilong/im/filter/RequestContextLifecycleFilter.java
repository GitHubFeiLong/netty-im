package com.feilong.im.filter;

import com.feilong.im.context.CurrentTimeContext;
import com.feilong.im.context.TraceIdContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.lang.NonNullApi;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 请求上下文生命周期过滤器
 * @author cfl 2026/4/15 星期三
 */
@Slf4j
@RequiredArgsConstructor
public class RequestContextLifecycleFilter extends OncePerRequestFilter {

    /**
     * 设置请求的一些上下文
     * @param request 请求
     * @param response 响应
     * @param filterChain 过滤器链
     * @throws ServletException Servlet异常
     * @throws IOException IO异常
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            CurrentTimeContext.set();
            TraceIdContext.set();
            filterChain.doFilter(request, response);
        } finally {
            TraceIdContext.remove();
            CurrentTimeContext.remove();
        }
    }

}
