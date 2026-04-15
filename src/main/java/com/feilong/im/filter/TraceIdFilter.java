package com.feilong.im.filter;

import com.feilong.im.context.TraceIdContext;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

/**
 * 拦截器，给请求日志加上追踪id
 * @author cfl 2026/4/15
 */
public class TraceIdFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            // 添加请求日志的全局唯一id
            String traceId = ((HttpServletRequest) request).getHeader("X-Trace-Id");
            TraceIdContext.set(traceId);
            chain.doFilter(request, response);
        } finally {
            // 清除请求的全局日志id
            TraceIdContext.remove();
        }
    }

}
