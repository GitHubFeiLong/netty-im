package com.feilong.im.filter;

import com.feilong.im.context.CurrentTimeContext;
import com.feilong.im.context.TraceIdContext;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

/**
 * 拦截器，记录当前请求的时间
 * @author cfl 2026/4/15
 */
public class CurrentTimeFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            CurrentTimeContext.set();
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            CurrentTimeContext.remove();
        }
    }
}
