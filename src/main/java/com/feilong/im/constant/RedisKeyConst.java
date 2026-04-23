package com.feilong.im.constant;

/**
 * @author cfl 2026/03/30
 */
public interface RedisKeyConst {

    /**
     * 缓存用户信息
     */
    String IM_USER_KEY = "cache:im:user:{userId}";
    /**
     * 缓存会话信息
     */
    String IM_CONV_KEY = "cache:im:conv:{type}:{user1Id}:{userOrGroupId}";

    /**
     * 获取完整的key
     * @param key key模板
     * @param args 参数
     * @return 完整key
     */
    static String getKey(String key, Object... args) {
        for (Object arg : args) {
            key = key.replaceFirst("\\{.*?}", String.valueOf(arg));
        }
        return key;
    }
}
