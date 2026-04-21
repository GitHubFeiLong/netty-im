package com.feilong.im.util;

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
 * @author cfl 2026/03/30
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SpringBeanUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        SpringBeanUtil.applicationContext = applicationContext;
    }


    /**
     * 根据类型获取 Spring Bean
     * @param clazz bean 类型
     * @return Spring Bean 实例
     */
    public static <T> T getBean(Class<T> clazz) {
        if (applicationContext == null) {
            log.warn("ApplicationContext 未初始化");
            return null;
        }
        return applicationContext.getBean(clazz);
    }

    /**
     * 根据名称和类型获取 Spring Bean
     * @param name bean 名称
     * @param clazz bean 类型
     * @return Spring Bean 实例
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        if (applicationContext == null) {
            log.warn("ApplicationContext 未初始化");
            return null;
        }
        return applicationContext.getBean(name, clazz);
    }
}
