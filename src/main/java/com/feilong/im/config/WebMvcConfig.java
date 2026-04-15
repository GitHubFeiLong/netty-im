package com.feilong.im.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Web MVC 配置
 * @author cfl 2026/04/15
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/favicon.ico")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    /**
     * 添加自定义格式化器，支持 GET 请求参数的日期时间转换
     * @param registry
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        // LocalDateTime 格式：yyyy-MM-dd HH:mm:ss
        registry.addConverter(String.class, LocalDateTime.class, source -> {
            if (source.trim().isEmpty()) {
                return null;
            }
            return LocalDateTime.parse(source.trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        });

        // LocalDate 格式：yyyy-MM-dd
        registry.addConverter(String.class, LocalDate.class, source -> {
            if (source.trim().isEmpty()) {
                return null;
            }
            return LocalDate.parse(source.trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        });

        // LocalTime 格式：HH:mm:ss
        registry.addConverter(String.class, LocalTime.class, source -> {
            if (source.trim().isEmpty()) {
                return null;
            }
            return LocalTime.parse(source.trim(), DateTimeFormatter.ofPattern("HH:mm:ss"));
        });
    }
}
