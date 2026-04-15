package com.feilong.im.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.feilong.im.context.TraceIdContext;
import com.feilong.im.exception.BasicException;
import com.feilong.im.properties.ApiLogProperties;
import com.feilong.im.util.AssertUtil;
import com.feilong.im.util.IpUtil;
import com.feilong.im.util.StringUtil;
import com.google.common.collect.Lists;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.catalina.connector.RequestFacade;
import org.apache.catalina.connector.ResponseFacade;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Api日志切面
 * @author cfl 2026/4/15
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
@ConditionalOnClass(name = {"org.aspectj.lang.JoinPoint"})
@ConditionalOnProperty(prefix = "api-log", name = "enabled", havingValue = "true")
public class ApiLogAspect {

    /**
     * 不打印指定开头的类参数
     */
    private static final List<String> APL_LOG_IGNORE_CLASS = List.of(
            "javax.servlet",
            "jakarta.servlet",
            "org.apache",
            "org.springframework"
    );

    /**
     * 请求头设置api日志返回结果长度的key
     */
    public static final String X_API_LOG_RESULT_LENGTH = "X-Api-Log-Result-Length";

    private final Environment env;
    private final ObjectMapper objectMapper;
    private final ApiLogProperties apiLogProperties;

    /**
     * 控制器
     */
    @Pointcut(
            "@within(org.springframework.web.bind.annotation.RestController)" +
                    " || @within(org.springframework.stereotype.Controller)"
    )
    public void springBeanPointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }

    /**
     * Retrieves the {@link Logger} associated to the given {@link JoinPoint}.
     *
     * @param joinPoint join point we want the logger for.
     * @return {@link Logger} associated to the given {@link JoinPoint}.
     */
    private Logger logger(JoinPoint joinPoint) {
        return LoggerFactory.getLogger(joinPoint.getSignature().getDeclaringTypeName());
    }

    /**
     * 方法进入和退出时打印日志
     * @param joinPoint join point for advice.
     * @return result.
     * @throws Throwable throws {@link IllegalArgumentException}.
     */
    @Around("springBeanPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        // 创建计时器
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ip = IpUtil.getStringIp(request);
        String requestUri = request.getRequestURI();
        String method = request.getMethod();
        Map<String, String> requestHead = getRequestHead(request);
        Object params = Objects.equals("GET", method) ? request.getParameterMap() : getArgs(joinPoint);

        // 创建apiLog对象
        ApiLog apiLog = new ApiLog();
        apiLog.setIp(ip);
        apiLog.setUri(requestUri);
        apiLog.setMethod(method);
        apiLog.setHeadParams(requestHead);
        apiLog.setParams(params);

        // 接口返回值
        Object result = null;
        // 是否执行成功
        boolean successful = false;
        try {
            // 执行方法
            result = joinPoint.proceed();
            // 设置本次执行成功
            successful = true;
        } catch (BasicException e) {
            log.error("BasicException");
            result = e;
            logger(joinPoint).error(
                    "Exception in {}() with cause = '{}' and exception = '{}'",
                    joinPoint.getSignature().getName(),
                    e.getCause() != null ? e.getCause() : "NULL",
                    e.getMessage()
            );
            throw e;
        } catch (Exception ex) {
            log.error("Exception");
            result = ex.getMessage();
            logger(joinPoint).error(
                    "Exception in {}() with cause = '{}' and exception = '{}'",
                    joinPoint.getSignature().getName(),
                    ex.getCause() != null ? ex.getCause() : "NULL",
                    ex.getMessage()
            );
            throw ex;
        } finally {
            stopWatch.stop();
            long time = stopWatch.getTotalTimeMillis();
            apiLog.setResults(result);
            apiLog.setTranceId(TraceIdContext.get());
            apiLog.setSuccessful(successful);
            apiLog.setTime((int)time);
            // 输出接口日志
            apiLog.printLogString(apiLogProperties, objectMapper);
        }

        return result;
    }

    /**
     * 获取请求参数
     * @param invocation MethodInvocation
     * @return List<Object> 请求参数
     */
    private List<Object> getArgs(MethodInvocation invocation) {
        List<Class<?>> filter = Lists.newArrayList(
                RequestFacade.class,
                ResponseFacade.class
        );
        Object[] argsArr = invocation.getArguments();
        // Stream.of(null).collect(Collectors.toList()) 会出现NPE
        if (argsArr.length > 0) {
            log.debug("argsArr ＝{}", argsArr);
            // 过滤掉大对象，避免转json报错
            return Stream.of(argsArr)
                    // 过滤掉
                    .filter(f -> f != null && !filter.contains(f.getClass()))
                    .collect(Collectors.toList());
        }

        return List.of();
    }

    /**
     * 获取请求参数
     * @param joinPoint ProceedingJoinPoint
     * @return List<Object> 请求参数
     */
    private List<Object> getArgs(ProceedingJoinPoint joinPoint) {
        Object[] argsArr = joinPoint.getArgs();
        if (argsArr != null && argsArr.length > 0) {
            log.debug("argsArr ＝{}", argsArr);
            // 过滤掉大对象，避免转json报错
            return Stream.of(argsArr)
                    // 过滤掉不能序列化的对象
                    .filter(f -> {
                        for (String aplLogIgnoreClass : APL_LOG_IGNORE_CLASS) {
                            if (f == null || f.getClass().getName().startsWith(aplLogIgnoreClass)) {
                                return false;
                            }
                        }
                        return true;
                    })
                    .collect(Collectors.toList());
        }

        return List.of();
    }

    /**
     * 获取x-开头的head参数和Authorization参数
     * @param request HttpServletRequest
     * @return Map<String, String> 请求头参数
     */
    private Map<String, String> getRequestHead(HttpServletRequest request){
        //获取请求参数
        Enumeration<String> headerNames = request.getHeaderNames();
        Map<String, String> data = new HashMap<>();
        ApiLogProperties.PrintLogLimit printLogLimit = apiLogProperties.getPrintLogLimit();
        // 自定义配置的请求头限制
        Map<String, Boolean> headParams = printLogLimit.getHeadParams();
        while (headerNames.hasMoreElements()) {
            // 请求头转小写
            String name = headerNames.nextElement().toLowerCase();
            if (headParams.containsKey(name) && headParams.get(name)) {
                data.put(name, request.getHeader(name));
                continue;
            }
            if (name.contains("x-") && !headParams.containsKey(name)) {
                data.put(name, request.getHeader(name));
                continue;
            }

        }
        return data;
    }


    /**
     * ApiLog
     * @author cfl 2026/4/15
     */
    @Slf4j
    @Data
    @NoArgsConstructor
    public static class ApiLog {
        //~fields
        //==================================================================================================================
        /**
         * ip地址
         */
        private String ip;
        /**
         * uri
         */
        private String uri;
        /**
         * uri 对应的方法
         */
        private String method;
        /**
         * 自定义的请求头
         */
        private Map<String, String> headParams;
        /**
         * 请求参数对象
         */
        private Object params;
        /**
         * 响应结果对象
         */
        private Object results;
        /**
         * 全局唯一id
         */
        private String tranceId;
        /**
         * 接口成功或失败
         */
        private Boolean successful;
        /**
         * 耗时，单位毫秒
         */
        private Integer time;

        /**
         * 转成日志字符串
         * @param apiLogProperties 接口日志配置
         * @param objectMapper objectMapper对象
         * @return 日志字符串
         */
        public String toLogString(ApiLogProperties apiLogProperties, ObjectMapper objectMapper) {
            AssertUtil.isTrue(apiLogProperties.getEnabled(), () -> new RuntimeException("未开启接口日志打印"));
            // 字符串追加
            StringBuilder sb = new StringBuilder();
            sb.append("\n----------------------------------------------------------------------------------------------------\n");
            // 01部分追加
            part01(objectMapper, apiLogProperties, sb);
            // 02部分追加
            part02(objectMapper, apiLogProperties, sb);
            sb.append("----------------------------------------------------------------------------------------------------\n");
            return sb.toString();
        }

        /**
         * 打印日志
         * @param apiLogProperties apiLogProperties
         * @param objectMapper objectMapper
         */
        public void printLogString(ApiLogProperties apiLogProperties, ObjectMapper objectMapper) {
            String logStr = toLogString(apiLogProperties, objectMapper);
            Level level = apiLogProperties.getLevel();
            switch (level) {
                case TRACE:
                    log.trace(logStr);
                    break;
                case INFO:
                    log.info(logStr);
                    break;
                case WARN:
                    log.warn(logStr);
                    break;
                case ERROR:
                    log.error(logStr);
                    break;
                default:
                    log.debug(logStr);
            }
        }

        /**
         * 追加 IP、URI、Method、HeadParams、Params 参数
         * @param objectMapper 对象映射器
         * @param apiLogProperties apiLog配置属性
         * @param sb StringBuilder追加字符串后面
         */
        private void part01(ObjectMapper objectMapper, ApiLogProperties apiLogProperties, StringBuilder sb) {
            // 类型开关
            ApiLogProperties.TypeEnabled typeEnabled = apiLogProperties.getTypeEnabled();
            if (typeEnabled.getIp() && ip != null) {
                sb.append("IP        : ").append(ip).append("\n");
            }

            if (typeEnabled.getUri() && uri != null) {
                sb.append("URI       : ").append(uri).append("\n");
            }

            if (typeEnabled.getMethod() && method != null) {
                sb.append("Method    : ").append(method).append("\n");
            }

            if (typeEnabled.getHeadParams() && headParams != null) {
                sb.append("HeadParams: ").append(headParams).append("\n");
            }

            if (typeEnabled.getParams() && params != null) {
                String paramsStr;
                try {
                    paramsStr = objectMapper.writeValueAsString(params);
                } catch (Exception e) {
                    log.warn("接口参数序列化json失败：{}", e.getMessage());
                    paramsStr = params.toString();
                }
                sb.append("Params    : ").append(paramsStr).append("\n");
            }
        }

        /**
         * 追加 Results、TraceId、Successful、Time 参数
         * @param objectMapper 对象映射器
         * @param apiLogProperties apiLog配置属性
         * @param sb StringBuilder追加字符串后面
         */
        private void part02(ObjectMapper objectMapper, ApiLogProperties apiLogProperties, StringBuilder sb) {
            // 类型开关
            ApiLogProperties.TypeEnabled typeEnabled = apiLogProperties.getTypeEnabled();
            // 限制
            ApiLogProperties.PrintLogLimit printLogLimit = apiLogProperties.getPrintLogLimit();

            if (typeEnabled.getResults() && results != null) {
                // 获取需要打印得长度限制
                int maxResultStrLength = printLogLimit.getResultsLength();
                if (headParams != null) {
                    try {
                        String xApiLogResultLength = headParams.get(X_API_LOG_RESULT_LENGTH);
                        maxResultStrLength = StringUtil.isNotBlank(xApiLogResultLength) ? Integer.parseInt(xApiLogResultLength) : maxResultStrLength;
                    } catch (NumberFormatException e) {
                        log.warn("请求头 {} 的内容只能是整数类型", X_API_LOG_RESULT_LENGTH);
                    }
                }

                // 打印接口返回值
                String resultStr;
                try {
                    if (results instanceof String) {
                        resultStr = results.toString();
                    } else {
                        resultStr = objectMapper.writeValueAsString(Optional.ofNullable(results).orElseGet(() -> "null"));
                    }
                } catch (Exception e) {
                    log.warn("序列化results json失败：{}", e.getMessage());
                    resultStr = results.toString();
                }

                // 获取最终需要打印得返回值
                if (maxResultStrLength >= 0) {
                    resultStr = resultStr.length() > maxResultStrLength ? resultStr.substring(0, maxResultStrLength) : resultStr;
                }
                sb.append("Results   : ").append(resultStr).append("\n");
            }

            if (typeEnabled.getTraceId() && tranceId != null) {
                sb.append("TraceId   : ").append(Optional.of(tranceId).orElseGet(() -> "null")).append("\n");
            }

            // 接口响应
            if (typeEnabled.getSuccessful() && successful != null) {
                sb.append("Successful: ").append(successful).append("\n");
            }

            // 接口时间
            if (typeEnabled.getTime() && time != null) {
                boolean msFlag = time < 1000;
                if (msFlag) {
                    sb.append("Time      : ").append(time).append("ms").append("\n");
                } else {
                    double doubleValue = BigDecimal.valueOf(time).divide(new BigDecimal("1000"), 4, RoundingMode.UP).doubleValue();
                    sb.append("Time      : ").append(doubleValue).append("s").append("\n");
                }
            }
        }
    }

}
