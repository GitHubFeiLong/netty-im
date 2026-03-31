package com.feilong.im.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.feilong.im.config.JacksonConfig;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：
 *
 * @Author Administrator
 * @Version 1.0
 */
@Slf4j
public class JsonUtil {
    //~fields
    //==================================================================================================================
    /**
     * objectMapper实例
     */
    private static volatile ObjectMapper objectMapper;

    //~methods
    //==================================================================================================================
    /**
     * 获取objectMapper实例
     * @return objectMapper
     */
    public static ObjectMapper getObjectMapper() {
        if (objectMapper == null) {
            synchronized (JsonUtil.class) {
                if (objectMapper == null) {
                    objectMapper = SpringUtil.getBean(ObjectMapper.class);
                    if (objectMapper == null) {
                        objectMapper = new JacksonConfig().objectMapper();
                    }
                }
            }
        }

        return objectMapper;
    }

    /**
     * 获取json字符串
     * @param obj 对象
     * @return obj的json字符串
     */
    public static String toJsonString(Object obj) {
        ObjectMapper objectMapper = getObjectMapper();
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("对象转json字符串失败：{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * json字符串转对象
     * @param json 对象
     * @param clazz 需要转成的目标类型对象
     * @return {@code clazz}类型的对象
     */
    public static <T> T toObject(String json, Class<T> clazz) {
        ObjectMapper objectMapper = getObjectMapper();
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            log.error("json字符串转对象失败：{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * json字符串转list对象
     * @param json json字符串
     * @param clazz 需要转成的目标类型对象
     * @return  {@code clazz}类型的对象
     */
    public static <T> List<T> toList(String json, Class<T> clazz){
        try {
            log.info("json:{}, class:{}", json, clazz);
            CollectionType listType = getObjectMapper()
                    .getTypeFactory()
                    .constructCollectionType(ArrayList.class, clazz);
            return getObjectMapper().readValue(json, listType);
        } catch (JsonProcessingException e) {
            log.error("json字符串转对象失败：{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * json字符串转对象
     * @param json 对象
     * @param typeReference 需要转成的目标类型对象
     * @return {@code clazz}类型的对象
     */
    public static <T> T toObject(String json, TypeReference<T> typeReference) {
        ObjectMapper objectMapper = getObjectMapper();
        try {
            return objectMapper.readValue(json, typeReference);
        } catch (JsonProcessingException e) {
            log.error("json字符串转对象失败：{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
