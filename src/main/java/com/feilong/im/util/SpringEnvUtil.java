package com.feilong.im.util;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * Spring 环境工具类
 * @author cfl 2026/03/30
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SpringEnvUtil {

    private static Environment environment;
    private final Environment env;

    /**
     * Bean 初始化完成后，将实例变量赋值给静态变量
     */
    @PostConstruct
    public void init() {
        SpringEnvUtil.environment = this.env;
    }

    /**
     * 获取当前激活的环境
     * @return 环境名称，如果没有激活的环境则返回 "default"
     */
    public static String getActiveProfile() {
        String[] activeProfiles = environment.getActiveProfiles();
        return activeProfiles.length > 0 ? activeProfiles[0] : "default";
    }

    /**
     * 判断是否是开发环境
     * @return true-开发环境
     */
    public static boolean isDev() {
        return environment.acceptsProfiles(Profiles.of("dev"));
    }

    /**
     * 判断是否是测试环境
     * @return true-测试环境
     */
    public static boolean isTest() {
        return environment.acceptsProfiles(Profiles.of("test"));
    }

    /**
     * 判断是否是生产环境
     * @return true-生产环境
     */
    public static boolean isProd() {
        return environment.acceptsProfiles(Profiles.of("prod"));
    }

    /**
     * 判断是否包含指定环境
     * @param profiles 环境列表
     * @return true-包含指定环境
     */
    public static boolean acceptsProfiles(String... profiles) {
        return environment.acceptsProfiles(Profiles.of(profiles));
    }

    /**
     * 获取配置属性
     * @param key 配置键
     * @return 配置值，如果不存在则返回 null
     */
    public static String getProperty(String key) {
        return environment.getProperty(key);
    }

    /**
     * 获取配置属性，带默认值
     * @param key 配置键
     * @param defaultValue 默认值
     * @return 配置值
     */
    public static String getProperty(String key, String defaultValue) {
        return environment.getProperty(key, defaultValue);
    }

}
