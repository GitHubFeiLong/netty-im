package com.feilong.im.context;

import java.time.LocalDateTime;
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
}
