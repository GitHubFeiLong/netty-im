package com.feilong.im.context;

import org.slf4j.MDC;
import org.springframework.util.StringUtils;

import java.util.UUID;
import java.util.concurrent.Callable;

/**
 * traceId 上下文
 * @author cfl 2026/3/26
 */
public class TraceIdContext {

    private static final String TRACE_ID_KEY = "traceId";

    /**
     * 生成新的TraceId
     */
    public static String generateTraceId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16).toUpperCase();
    }

    /**
     * 生成新的TraceId
     */
    public static String generateTraceId(int length) {
        return UUID.randomUUID().toString().replace("-", "").substring(0, length).toUpperCase();
    }

    /**
     * 设置TraceId到MDC
     */
    public static void set(String traceId) {
        if (!StringUtils.hasText(traceId)) {
            traceId = generateTraceId();
        }
        MDC.put(TRACE_ID_KEY, traceId);
    }

    /**
     * 获取当前TraceId
     */
    public static String get() {
        return MDC.get(TRACE_ID_KEY);
    }

    /**
     * 清除TraceId
     */
    public static void remove() {
        MDC.remove(TRACE_ID_KEY);
    }

    /**
     * 包装Runnable，确保MDC上下文传递
     */
    public static Runnable wrap(Runnable runnable) {
        String traceId = get();
        return () -> {
            try {
                // 在新线程中恢复MDC上下文
                if (traceId != null) {
                    MDC.put(TRACE_ID_KEY, traceId);
                }
                runnable.run();
            } finally {
                // 清理MDC避免内存泄漏
                MDC.clear();
            }
        };
    }

    /**
     * 包装Callable，确保MDC上下文传递
     */
    public static <T> Callable<T> wrap(Callable<T> callable) {
        String traceId = get();
        return () -> {
            try {
                // 在新线程中恢复MDC上下文
                if (traceId != null) {
                    MDC.put(TRACE_ID_KEY, traceId);
                }
                return callable.call();
            } finally {
                // 清理MDC避免内存泄漏
                MDC.clear();
            }
        };
    }

    /**
     * 在当前线程中执行带TraceId的操作
     */
    public static void executeWithTraceId(String traceId, Runnable runnable) {
        String oldTraceId = get();
        try {
            set(traceId);
            runnable.run();
        } finally {
            if (oldTraceId != null) {
                MDC.put(TRACE_ID_KEY, oldTraceId);
            } else {
                MDC.remove(TRACE_ID_KEY);
            }
        }
    }
}
