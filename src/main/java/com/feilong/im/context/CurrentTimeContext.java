package com.feilong.im.context;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * 当前时间上下文
 * @author cfl 2026/3/26
 */
public class CurrentTimeContext {

    private static final InheritableThreadLocal<LocalDateTime> THREAD_LOCAL = new InheritableThreadLocal<>();

    public static void set() {
        THREAD_LOCAL.set(LocalDateTime.now());
    }

    public static void set(LocalDateTime localDateTime) {
        THREAD_LOCAL.set(localDateTime);
    }

    public static LocalDateTime get() {
        LocalDateTime localDateTime = THREAD_LOCAL.get();
        if (localDateTime == null) {
            set();
        }

        return THREAD_LOCAL.get();
    }

    public static void remove() {
        THREAD_LOCAL.remove();
    }

    /**
     * 获取当前时间戳
     * @return 时间戳
     */
    public static long getTimestamp() {
        return get().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * 获取当前时间
     * @return  时间
     */
    public static Date getDate() {
        return Date.from(get().atZone(ZoneId.systemDefault()).toInstant());
    }
}
