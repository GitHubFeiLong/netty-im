package com.feilong.im.context;

import java.util.Date;

/**
 * 当前时间上下文
 * @author cfl 2026/3/26
 */
public class CurrentTimeContext {

    private static final InheritableThreadLocal<Date> THREAD_LOCAL = new InheritableThreadLocal<>();

    public static void set() {
        THREAD_LOCAL.set(new Date());
    }

    public static void set(Date date) {
        THREAD_LOCAL.set(date);
    }

    public static Date get() {
        Date date = THREAD_LOCAL.get();
        if (date == null) {
            set();
        }

        return THREAD_LOCAL.get();
    }

    public static void remove() {
        THREAD_LOCAL.remove();
    }
}
